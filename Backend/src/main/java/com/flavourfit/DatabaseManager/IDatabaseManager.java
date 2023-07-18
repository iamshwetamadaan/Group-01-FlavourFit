package com.flavourfit.DatabaseManager;

import com.flavourfit.Exceptions.DatabaseException;

import java.sql.Connection;

public interface IDatabaseManager {
    public boolean connect() throws DatabaseException;

    public boolean disconnect() throws DatabaseException;

    public Connection getConnection();
}
