package com.dsluchenko.app.dao.impl;

import com.dsluchenko.app.dao.CurrencyDao;
import com.dsluchenko.app.dao.exception.DaoConstraintViolationRuntimeException;
import com.dsluchenko.app.dao.exception.DaoRuntimeException;
import com.dsluchenko.app.entity.Currency;
import com.dsluchenko.app.util.DbConnectionBuilder;
import com.dsluchenko.app.util.impl.PgSqlErrorCode;
import com.dsluchenko.app.util.impl.PoolConnectionBuilder;
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
            } catch (SQLException e) {
                logger.log(Level.WARNING,
                        "Currency not created",
                        e);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Currency by id: " + id + " not received",
                    e);
            throw new DaoRuntimeException(e.getMessage());
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
            logger.log(Level.SEVERE,
                    "List of all currencies not received",
                    e);
            throw new DaoRuntimeException(e.getMessage());
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
                logger.log(Level.SEVERE,
                        "Currency not saved, constraint has been violated",
                        e);
                throw new DaoConstraintViolationRuntimeException();
            }

            logger.log(Level.SEVERE,
                    "Currency not saved, sqlError code: " + e.getSQLState(),
                    e);
            throw new DaoRuntimeException(e.getMessage());
        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Currency not saved",
                    e);
            throw new DaoRuntimeException(e.getMessage());
        }
    }

    @Override
    public int update(Currency currency, String[] params) {
        return 0;
    }

    @Override
    public int delete(Currency currency) {
        String sql = "DELETE currency WHERE id = ?";
        int id = 0;
        try (Connection connection = connBuilder.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, currency.getId());
            statement.execute();

        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Currency not deleted",
                    e);
            throw new DaoRuntimeException(e.getMessage());
        }
        return id;
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
            } catch (SQLException e) {
                logger.log(Level.WARNING,
                        "Currency not created",
                        e);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE,
                    "Currency by code: " + code + " not received",
                    e);
            throw new DaoRuntimeException(e.getMessage());
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
