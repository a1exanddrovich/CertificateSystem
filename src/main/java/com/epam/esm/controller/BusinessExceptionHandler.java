package com.epam.esm.controller;

import com.epam.esm.exception.*;
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

    private static final String NOT_EXIST_EXCEPTION_MESSAGE = "exception.notExist";
    private static final String BAD_ENTITY_MESSAGE = "exception.badEntity";
    private static final String ALREADY_EXIST_EXCEPTION_MESSAGE = "exception.alreadyExist";
    private static final String PAGINATION_EXCEPTION_MESSAGE = "exception.badPagination";
    private static final String GIFT_CERTIFICATE_QUERY_EXCEPTION_MESSAGE = "exception.badCertificateQuery";

    private final ReloadableResourceBundleMessageSource messageSource;

    @Autowired
    public BusinessExceptionHandler(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotExistException(Locale locale) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND.value(), messageSource.getMessage(NOT_EXIST_EXCEPTION_MESSAGE, new Object[]{}, locale)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadEntityException.class)
    public ResponseEntity<ExceptionResponse> handleBadEntityException(Locale locale) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), messageSource.getMessage(BAD_ENTITY_MESSAGE, new Object[]{}, locale)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEntityAlreadyExistException(Locale locale) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.CONFLICT.value(), messageSource.getMessage(ALREADY_EXIST_EXCEPTION_MESSAGE, new Object[]{}, locale)), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaginationException.class)
    public ResponseEntity<ExceptionResponse> handlePaginationException(Locale locale) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.NOT_FOUND.value(), messageSource.getMessage(PAGINATION_EXCEPTION_MESSAGE, new Object[] {}, locale)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GiftCertificateQueryException.class)
    public ResponseEntity<ExceptionResponse> handleGiftCertificateQueryException(Locale locale) {
        return new ResponseEntity<>(new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), messageSource.getMessage(GIFT_CERTIFICATE_QUERY_EXCEPTION_MESSAGE, new Object[] {}, locale)), HttpStatus.BAD_REQUEST);
    }

}
