package com.zadentech.restfulwebservices.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * RepositoryOperationUnsuccessfulException
 */

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DataOperationException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public DataOperationException(String message) {
        super(message);
    }
}