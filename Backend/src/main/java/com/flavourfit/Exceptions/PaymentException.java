package com.flavourfit.Exceptions;

public class PaymentException extends RuntimeException{
    /**
     * @param message
     */
    public PaymentException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public PaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public PaymentException(Throwable cause) {
        super(cause);
    }
}
