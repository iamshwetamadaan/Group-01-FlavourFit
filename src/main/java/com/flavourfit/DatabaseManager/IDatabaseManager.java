package com.flavourfit.DatabaseManager;

import java.sql.Connection;

public interface IDatabaseManager {
    public boolean connect() throws RuntimeException;

    public boolean disconnect() throws RuntimeException;

    public Connection getConnection();
}
