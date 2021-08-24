package com.epam.esm.controller;

import com.epam.esm.entity.ExceptionResponse;
import com.epam.esm.exception.BadEntityException;
import com.epam.esm.exception.EntityNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.Locale;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {

    private final ReloadableResourceBundleMessageSource messageSource;

    @Autowired
    public BusinessExceptionHandler(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotExistException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotExistException(Locale locale) {
        String errorMessage = messageSource.getMessage("exception.notExist", new Object[]{}, locale);
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadEntityException.class)
    public ResponseEntity<ExceptionResponse> handleBadEntityException(Locale locale) {
        String errorMessage = messageSource.getMessage("exception.badEntity", new Object[]{}, locale);
        ExceptionResponse exception = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }

}
