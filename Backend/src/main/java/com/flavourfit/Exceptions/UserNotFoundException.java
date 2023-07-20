package com.flavourfit.Exceptions;

public class UserNotFoundException extends RuntimeException{
    /**
     * @param message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
