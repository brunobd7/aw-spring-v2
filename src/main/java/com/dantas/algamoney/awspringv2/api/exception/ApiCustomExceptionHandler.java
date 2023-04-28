package com.dantas.algamoney.awspringv2.api.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ControllerAdvice
public class ApiCustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String userMessage = messageSource.getMessage("invalid.message",null, LocaleContextHolder.getLocale());
        String exMessage = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

        return handleExceptionInternal(ex, List.of(new CustomApiErrorMessage(userMessage, exMessage)),headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<CustomApiErrorMessage> errorsList = this.mapRequestErrors(ex.getBindingResult());
        return handleExceptionInternal(ex, errorsList,headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){

        String userMessage = messageSource.getMessage("resource.notAllowedOperation",null, LocaleContextHolder.getLocale());
        String exMessage = ExceptionUtils.getRootCauseMessage(ex); //USING APACHE COMMONS TO ACCESS DETAILS OF EXCEPTION RELATED A DATABASE

        return handleExceptionInternal(ex, List.of(new CustomApiErrorMessage(userMessage, exMessage)),new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,WebRequest request){

        String userMessage = messageSource.getMessage("resource.notFound",null, LocaleContextHolder.getLocale());
        String exMessage = Optional.ofNullable(ex.getCause()).orElse(ex).toString();

        return handleExceptionInternal(ex, List.of(new CustomApiErrorMessage(userMessage, exMessage)),new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }


    private List<CustomApiErrorMessage> mapRequestErrors(BindingResult bindingResult){

        List<CustomApiErrorMessage> errorsList = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()){

              String userMessage="";
              String exceptionMessage="";

              userMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
              exceptionMessage = fieldError.toString();

              errorsList.add(new CustomApiErrorMessage(userMessage,exceptionMessage));

        }

        return errorsList;
    }
}
