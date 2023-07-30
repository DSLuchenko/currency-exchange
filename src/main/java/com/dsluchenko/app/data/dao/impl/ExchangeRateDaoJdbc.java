package com.dsluchenko.app.data.dao.impl;

import com.dsluchenko.app.data.dao.ExchangeRateDao;
import com.dsluchenko.app.data.dao.exception.DaoConstraintViolationRuntimeException;
import com.dsluchenko.app.data.dao.exception.DaoRuntimeException;
import com.dsluchenko.app.model.Currency;
import com.dsluchenko.app.model.ExchangeRate;
import com.dsluchenko.app.data.db.DbConnectionBuilder;
import com.dsluchenko.app.data.db.PgSqlErrorCode;
import com.dsluchenko.app.data.db.impl.PoolConnectionBuilder;
import org.postgresql.util.PSQLException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExchangeRateDaoJdbc implements ExchangeRateDao {
    private static final Logger logger = Logger.getLogger(ExchangeRateDaoJdbc.class.getName());
    private final DbConnectionBuilder connBuilder;

    public ExchangeRateDaoJdbc() {
        connBuilder = new PoolConnectionBuilder();
    }

    @Override
    public Optional<ExchangeRate> get(int id) {
        return Optional.empty();
    }

    @Override
    public List<ExchangeRate> getAll() {
        String sql = "SELECT rate.id id," +
                " rate.rate rate," +
                " base.id base_id," +
                " base.code base_code," +
                " base.full_name base_name," +
                " base.sign base_sign," +
                " target.id target_id," +
                " target.code target_code," +
                " target.full_name target_name," +
                " target.sign target_sign" +
                " FROM exchange_rate rate " +
                "join currency base on rate.base_currency_id = base.id " +
                "join currency target on rate.target_currency_id = target.id";

        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection connection = connBuilder.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                createExchangeRate(resultSet).ifPresent(exchangeRates::add);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "List of all exchange rates not received",
                    e);
            throw new DaoRuntimeException(e.getMessage());
        }
        return exchangeRates;
    }

    @Override
    public int save(ExchangeRate exchangeRate) {
        String sql = "INSERT INTO exchange_rate (base_currency_id, target_currency_id, rate) values (?,?,?)";

        try (Connection connection = connBuilder.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, exchangeRate.getBaseCurrency().getId());
            statement.setInt(2, exchangeRate.getTargetCurrency().getId());
            statement.setBigDecimal(3, exchangeRate.getRate());

            statement.execute();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt("id");
                return id;
            }

        } catch (PSQLException e) {
            if (e.getSQLState().equals(PgSqlErrorCode.UNIQUE_VIOLATION)) {
                logger.log(Level.SEVERE,
                        "Exchange rate not saved, constraint has been violated",
                        e);
                throw new DaoConstraintViolationRuntimeException();
            }

            logger.log(Level.SEVERE,
                    "Exchange rate not saved, sqlError code: " + e.getSQLState(),
                    e);
            throw new DaoRuntimeException(e.getMessage());
        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Exchange rate not saved",
                    e);
            throw new DaoRuntimeException(e.getMessage());
        }

    }

    @Override
    public int update(ExchangeRate exchangeRate, String[] params) {
        return 0;
    }

    @Override
    public int delete(ExchangeRate exchangeRate) {
        return 0;
    }

    @Override
    public Optional<ExchangeRate> getByCurrencyCodes(String baseCurrencyCode, String targetCurrencyCode) {
        String sql = "SELECT rate.id id," +
                " rate.rate rate," +
                " base.id base_id," +
                " base.code base_code," +
                " base.full_name base_name," +
                " base.sign base_sign," +
                " target.id target_id," +
                " target.code target_code," +
                " target.full_name target_name," +
                " target.sign target_sign" +
                " FROM exchange_rate rate " +
                "join currency base on rate.base_currency_id = base.id " +
                "join currency target on rate.target_currency_id = target.id " +
                "where base.code = ?" +
                " and target.code = ?";

        try (Connection connection = connBuilder.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, baseCurrencyCode);
            statement.setString(2, targetCurrencyCode);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createExchangeRate(resultSet);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Exchange rate with base currency code: " +
                                    " %s and target currency code: " +
                                    " %s not received",
                            baseCurrencyCode,
                            targetCurrencyCode),
                    e);
            throw new DaoRuntimeException(e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public Optional<ExchangeRate> updateByCurrencyCodes(String baseCurrencyCode, String
            targetCurrencyCode, BigDecimal rate) {
        String sql = "with er as " +
                "         (update exchange_rate " +
                "             set rate = ? " +
                "             where id = (select er.id " +
                "                         from exchange_rate er " +
                "                                  inner join currency base on base.id = er.base_currency_id " +
                "                                  inner join currency target on target.id = er.target_currency_id " +
                "                         where base.code = ? " +
                "                           and target.code = ?) returning *) " +
                "select er.id            id, " +
                "       er.rate          rate, " +
                "       base.id          base_id, " +
                "       base.code        base_code, " +
                "       base.full_name   base_name, " +
                "       base.sign        base_sign, " +
                "       target.id        target_id, " +
                "       target.code      target_code, " +
                "       target.full_name target_name, " +
                "       target.sign      target_sign " +
                "from er " +
                "         inner join currency base on base.id = er.base_currency_id " +
                "         inner join currency target on target.id = er.target_currency_id";

        try (Connection connection = connBuilder.getConnection();
             PreparedStatement statementUpdate = connection.prepareStatement(sql)
        ) {

            statementUpdate.setBigDecimal(1, rate);
            statementUpdate.setString(2, baseCurrencyCode);
            statementUpdate.setString(3, targetCurrencyCode);

            try (ResultSet resultSet = statementUpdate.executeQuery()) {
                if (resultSet.next()) {
                    return createExchangeRate(resultSet);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, String.format("Exchange rate value: %f with base currency code: " +
                                    " %s and target currency code: " +
                                    " %s not update",
                            rate,
                            baseCurrencyCode,
                            targetCurrencyCode),
                    e);
            throw new DaoRuntimeException(e.getMessage());
        }

        return Optional.empty();
    }

    private Optional<ExchangeRate> createExchangeRate(ResultSet resultSet) throws SQLException {
        Currency base = Currency.builder()
                                .id(resultSet.getInt("base_id"))
                                .code(resultSet.getString("base_code"))
                                .fullName(resultSet.getString("base_name"))
                                .sign(resultSet.getString("base_sign"))
                                .build();
        Currency target = Currency.builder()
                                  .id(resultSet.getInt("target_id"))
                                  .code(resultSet.getString("target_code"))
                                  .fullName(resultSet.getString("target_name"))
                                  .sign(resultSet.getString("target_sign"))
                                  .build();

        int rate_id = resultSet.getInt("id");
        BigDecimal rate = resultSet.getBigDecimal("rate");

        ExchangeRate exchangeRate = ExchangeRate.builder()
                                                .id(rate_id)
                                                .baseCurrency(base)
                                                .targetCurrency(target)
                                                .rate(rate).build();

        return Optional.of(exchangeRate);
    }
}
