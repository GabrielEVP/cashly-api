package com.cashly.cashly_api.shared.exceptions;

/**
 * Exception thrown when a token has expired.
 */
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
