package com.dsluchenko.app.util.impl;


import com.dsluchenko.app.util.DbConnectionBuilder;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

final class AppConnectionBuilder implements DbConnectionBuilder {
    private final static Logger logger = Logger.getLogger(AppConnectionBuilder.class.getName());
    private final static String DB_PROP_FILE_NAME = "/db.properties";
    private String driver, url, user, password;

    public AppConnectionBuilder() {
        setProps();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void setProps() {
        Properties props = new Properties();

        try (InputStream in = Files.newInputStream(Paths
                .get(AppConnectionBuilder
                        .class
                        .getResource(DB_PROP_FILE_NAME)
                        .toURI()
                )
        )) {
            props.load(in);

            driver = props.getProperty("jdbc.driver");
            Class.forName(driver).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error reading " + DB_PROP_FILE_NAME, e);
        }

        url = props.getProperty("jdbc.url");
        user = props.getProperty("jdbc.user");
        password = props.getProperty("jdbc.password");

    }


}
