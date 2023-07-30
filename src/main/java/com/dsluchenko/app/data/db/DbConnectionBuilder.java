package com.dsluchenko.app.data.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbConnectionBuilder {
    Connection getConnection() throws SQLException;
}
