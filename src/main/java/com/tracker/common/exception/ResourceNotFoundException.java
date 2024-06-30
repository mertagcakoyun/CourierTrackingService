package com.tracker.common.exception;

/**
 * Custom exception class for handling resource not found errors.
 * Used to indicate that a requested resource could not be found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

