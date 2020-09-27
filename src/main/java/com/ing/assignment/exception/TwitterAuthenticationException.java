package com.ing.assignment.exception;

/**
 * @author Jagrati
 * Authentication exception
 */
public class TwitterAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TwitterAuthenticationException(String message, Exception e) {
        super(message, e);
    }

    public TwitterAuthenticationException(final String message) {
        super(message);
    }

}
