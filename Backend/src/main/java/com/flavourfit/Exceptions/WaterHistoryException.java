package com.flavourfit.Exceptions;

public class WaterHistoryException extends RuntimeException{
    public WaterHistoryException(String message) {
        super(message);
    }

    public WaterHistoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public WaterHistoryException(Throwable cause) {
        super(cause);
    }


}
