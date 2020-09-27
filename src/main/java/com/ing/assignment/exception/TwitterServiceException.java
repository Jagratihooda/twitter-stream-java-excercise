package com.ing.assignment.exception;

/**
 * @author Jagrati
 * service api exception
 */
public class TwitterServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TwitterServiceException(String message, Exception e) {
        super(message, e);
    }
}
