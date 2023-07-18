package com.dsluchenko.app.dao.impl;

import com.dsluchenko.app.dao.ExchangeRateDao;
import com.dsluchenko.app.dao.exception.DaoRuntimeException;
import com.dsluchenko.app.entity.Currency;
import com.dsluchenko.app.entity.ExchangeRate;
import com.dsluchenko.app.util.DbConnectionBuilder;
import com.dsluchenko.app.util.impl.PoolConnectionBuilder;

import java.math.BigDecimal;
import java.sql.Connection;
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
        return 0;
    }

    @Override
    public int update(ExchangeRate exchangeRate, String[] params) {
        return 0;
    }

    @Override
    public int delete(ExchangeRate exchangeRate) {
        return 0;
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
