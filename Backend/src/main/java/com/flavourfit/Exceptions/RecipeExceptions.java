package com.flavourfit.Exceptions;

public class RecipeExceptions extends RuntimeException{
    public RecipeExceptions(String message) {
        super(message);
    }

    public RecipeExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public RecipeExceptions(Throwable cause) {
        super(cause);
    }
}
