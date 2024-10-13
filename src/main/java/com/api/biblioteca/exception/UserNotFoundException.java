package com.api.biblioteca.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class UserNotFoundException extends ResponseStatusException {

    public UserNotFoundException(Object identifier) {
        super(HttpStatus.NOT_FOUND, "User with identifier: " + identifier + " not found");
    }


}
