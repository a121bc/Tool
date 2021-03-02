package com.ltj.tool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base exception of the project
 *
 * @author Liu Tian Jun
 * @date 2021-03-01 09:13
 */
public abstract class AbstractToolException extends RuntimeException {

    /**
     * Error errorData.
     */
    private Object errorData;

    public AbstractToolException(String message) {
        super(message);
    }

    public AbstractToolException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Http status code
     *
     * @return {@link HttpStatus}
     */
    @NonNull
    public abstract HttpStatus getStatus();

    @Nullable
    public Object getErrorData() {
        return errorData;
    }

    @NonNull
    public AbstractToolException setErrorData(@Nullable Object errorData) {
        this.errorData = errorData;
        return this;
    }
}
