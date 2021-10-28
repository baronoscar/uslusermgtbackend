package com.unionsystems.ums.controller;

import com.unionsystems.ums.model.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, Exception exception) {
        AppResponse data = AppResponse.builder().errorMessage(exception.getMessage()).build();
        HttpStatus status = getStatus(request);
        exception.printStackTrace();
        return ResponseEntity.status(status).body(data);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(HttpServletRequest request, UsernameNotFoundException exception) {
        AppResponse data = AppResponse.builder().errorMessage(exception.getMessage()).status(HttpStatus.UNAUTHORIZED.value()).build();
        HttpStatus status = getStatus(request);
        return ResponseEntity.status(status).body(data);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(HttpServletRequest request, BadCredentialsException exception) {
        AppResponse data = AppResponse.builder().errorMessage(exception.getMessage()).status(HttpStatus.UNAUTHORIZED.value()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(data);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExistsException(HttpServletRequest request, EntityExistsException exception) {
        AppResponse data = AppResponse.builder().errorMessage(exception.getMessage()).status(HttpStatus.CONFLICT.value()).build();
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(data);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(HttpServletRequest request, EntityNotFoundException exception) {
        AppResponse data = AppResponse.builder().errorMessage(exception.getMessage()).status(HttpStatus.BAD_REQUEST.value()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(data);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException exception) {
        AppResponse data = AppResponse.builder().errorMessage(exception.getMessage()).status(HttpStatus.UNAUTHORIZED.value()).build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(data);
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
