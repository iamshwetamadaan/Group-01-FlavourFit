
package com.flavourfit.Exceptions;

public class FeedsException extends RuntimeException{
    public FeedsException(String message) {
        super(message);
    }

    public FeedsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeedsException(Throwable cause) {
        super(cause);
    }
}

