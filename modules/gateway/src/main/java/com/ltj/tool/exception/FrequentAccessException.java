package com.ltj.tool.exception;

/**
 * Frequent access exception.
 *
 * @author Liu Tian Jun
 * @date 2021-03-18 14:27
 */
public class FrequentAccessException extends BadRequestException {
    public FrequentAccessException(String message) {
        super(message);
    }

    public FrequentAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
