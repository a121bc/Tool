package com.ltj.tool.exception;

/**
 * File operation exception.
 *
 * @author Liu Tian Jun
 * @date 2021-03-09 09:09
 */
public class FileOperationException extends ServiceException {
    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
