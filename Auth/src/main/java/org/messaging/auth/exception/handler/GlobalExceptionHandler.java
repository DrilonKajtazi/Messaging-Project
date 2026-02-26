package org.messaging.auth.exception.handler;

import org.messaging.auth.exception.model.EmailAlreadyExistsException;
import org.messaging.auth.exception.model.ErrorDTO;
import org.messaging.auth.exception.model.UserNotLoggedInException;
import org.messaging.auth.exception.model.UsernameAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotLoggedInException.class)
    public ResponseEntity<ErrorDTO> generateNotLoggedInException(UserNotLoggedInException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        errorDTO.setTime(new Date().toString());
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        errorDTO.setTime(new Date().toString());
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(ex.getMessage());
        errorDTO.setStatus(String.valueOf(ex.getStatus().value()));
        errorDTO.setTime(new Date().toString());
        return new ResponseEntity<>(errorDTO, ex.getStatus());
    }
}
