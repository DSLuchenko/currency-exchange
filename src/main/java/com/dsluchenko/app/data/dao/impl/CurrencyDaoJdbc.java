package com.dsluchenko.app.data.dao.impl;

import com.dsluchenko.app.data.dao.CurrencyDao;
import com.dsluchenko.app.data.dao.exception.ConstraintViolationException;
import com.dsluchenko.app.data.dao.exception.DaoApplicationException;
import com.dsluchenko.app.model.Currency;
import com.dsluchenko.app.data.db.DbConnectionBuilder;
import com.dsluchenko.app.data.db.PgSqlErrorCode;
import com.dsluchenko.app.data.db.impl.PoolConnectionBuilder;
import org.postgresql.util.PSQLException;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CurrencyDaoJdbc implements CurrencyDao {
    private static final Logger logger = Logger.getLogger(CurrencyDaoJdbc.class.getName());
    private final DbConnectionBuilder connBuilder;

    public CurrencyDaoJdbc() {
        connBuilder = new PoolConnectionBuilder();
    }

    @Override
    public Optional<Currency> get(int id) {
        String sql = "SELECT * FROM currency where id = ?";
        Optional<Currency> currency = Optional.empty();
        try (Connection connection = connBuilder.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = createCurrency(resultSet);
                }
            }
            logger.info(String.format("Currency by id: %d not received", id));
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            throw new DaoApplicationException(e.getMessage());
        }
        return currency;
    }

    @Override
    public List<Currency> getAll() {
        String sql = "SELECT * FROM currency";
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = connBuilder.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                createCurrency(resultSet).ifPresent(currencies::add);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            throw new DaoApplicationException(e.getMessage());
        }
        return currencies;
    }

    @Override
    public int save(Currency currency) {
        String sql = "INSERT INTO currency (code,full_name,sign) values(?,?,?)";
        try (Connection connection = connBuilder.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());

            statement.execute();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                int id = rs.getInt("id");
                return id;
            }

        } catch (PSQLException e) {
            if (e.getSQLState().equals(PgSqlErrorCode.UNIQUE_VIOLATION)) {
                logger.info(String.format(
                        "Currency with code: %s not saved, constraint has been violated",
                        currency.getCode()));
                throw new ConstraintViolationException();
            }

            logger.log(Level.WARNING, e.getMessage(), e);
            throw new DaoApplicationException();
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            throw new DaoApplicationException();
        }
    }

    @Override
    public Optional<Currency> getByCode(String code) {
        String sql = "SELECT * FROM currency where code = ?";
        Optional<Currency> currency = Optional.empty();
        try (Connection connection = connBuilder.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, code.toUpperCase(Locale.ROOT));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = createCurrency(resultSet);
                }

                logger.info(String.format(
                        "Currency with code: %s not found",
                        code));
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            throw new DaoApplicationException();
        }
        return currency;
    }


    private Optional<Currency> createCurrency(ResultSet resultSet) throws SQLException {
        Currency currency = Currency.builder()
                                    .id(resultSet.getInt("id"))
                                    .code(resultSet.getString("code"))
                                    .fullName(resultSet.getString("full_name"))
                                    .sign(resultSet.getString("sign"))
                                    .build();

        return Optional.of(currency);
    }
}
