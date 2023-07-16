package com.dsluchenko.app.util.impl;

import com.dsluchenko.app.util.DbConnectionBuilder;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class PoolConnectionBuilder implements DbConnectionBuilder {
    private final static Logger logger = Logger.getLogger(PoolConnectionBuilder.class.getName());
    private DataSource dataSource;

    public PoolConnectionBuilder() {
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/postgres");
        }
        catch (Exception e){
            logger.log(Level.SEVERE, "Data source not create", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            logger.severe("Datasource not create, will be use AppConnectionBuilder");
            return new AppConnectionBuilder().getConnection();
        }

        return dataSource.getConnection();

    }

}
