package com.flavourfit.Exceptions;

public class DatabaseException extends Throwable{
    /**
     * @param message
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public DatabaseException(Throwable cause) {
        super(cause);
    }
}
