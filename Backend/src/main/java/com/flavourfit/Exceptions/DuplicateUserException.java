package com.flavourfit.Exceptions;

public class DuplicateUserException extends RuntimeException{
    /**
     * @param message
     */
    public DuplicateUserException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public DuplicateUserException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public DuplicateUserException(Throwable cause) {
        super(cause);
    }
}
