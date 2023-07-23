package com.flavourfit.Exceptions;

public class WeightHistoryException extends  RuntimeException{
    public WeightHistoryException(String message) {
        super(message);
    }

    public WeightHistoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeightHistoryException(Throwable cause) {
        super(cause);
    }
}
