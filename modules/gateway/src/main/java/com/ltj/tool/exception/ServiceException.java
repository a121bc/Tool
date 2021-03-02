package com.ltj.tool.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception caused by service.
 *
 * @author Liu Tian Jun
 * @date 2021-03-01 09:31
 */
public class ServiceException extends AbstractToolException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
