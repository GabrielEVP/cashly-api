package com.cashly.cashly_api.shared.exceptions;

/**
 * Exception thrown when a token is invalid, expired, or revoked.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
