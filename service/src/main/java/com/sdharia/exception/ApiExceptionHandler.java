package com.sdharia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintExceptions(HttpServletResponse response, ConstraintViolationException ex) throws IOException {
        String message = ex.getLocalizedMessage();
        String valueError = message.substring(message.lastIndexOf('.')+1);
        response.sendError(HttpStatus.BAD_REQUEST.value(), valueError);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public void handleAuthorizationExceptions(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized request to GitHub API. Add credentials to application.properties file");
    }
}