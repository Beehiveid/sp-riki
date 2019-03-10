package com.beehive.riki.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    public RestExceptionHandler() {
        super();
    }

    @ExceptionHandler({DataIntegrityViolationException.class })
    public void handleBadRequest(final DataIntegrityViolationException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getCause().getCause().getMessage());
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public void handleResourceNotFound(final ResourceNotFoundException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}
