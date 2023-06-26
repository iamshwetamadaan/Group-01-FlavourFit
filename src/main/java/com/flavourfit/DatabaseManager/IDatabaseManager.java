package com.flavourfit.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseManager {
    public boolean connect() throws RuntimeException;

    public boolean disconnect() throws RuntimeException;

    public Connection getConnection();
}
