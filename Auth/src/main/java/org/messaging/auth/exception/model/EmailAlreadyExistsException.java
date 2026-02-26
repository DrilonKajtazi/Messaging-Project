package org.messaging.auth.exception.model;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("An account with this email already exists!");
    }

    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
