package com.tracker.common.exception;

/**
 * Custom exception class for handling optimistic locking errors.
 * Used to indicate that an optimistic locking conflict has occurred.
 */
public class OptimisticLockingException extends RuntimeException {
    public OptimisticLockingException(String message) {
        super(message);
    }
}
