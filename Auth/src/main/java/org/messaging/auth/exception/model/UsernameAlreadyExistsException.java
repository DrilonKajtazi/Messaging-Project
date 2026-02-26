package org.messaging.auth.exception.model;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException() {
        super("This username is already taken!");
    }

    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
