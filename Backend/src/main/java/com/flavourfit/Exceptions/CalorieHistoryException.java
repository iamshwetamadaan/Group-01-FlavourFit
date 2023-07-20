package com.flavourfit.Exceptions;

public class CalorieHistoryException extends RuntimeException {
    public CalorieHistoryException(String message) {
        super(message);
    }

    public CalorieHistoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CalorieHistoryException(Throwable cause) {
        super(cause);
    }
}
