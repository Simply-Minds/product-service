package com.simplyminds.product.exception;

/**
 * Exception representing a bad request due to invalid business logic.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Construct a new BadRequestException with the given message.
     *
     * @param message the error message
     */
    public BadRequestException(String message) {
        super(message);
    }
}