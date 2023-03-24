package com.dantas.algamoney.awspringv2.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
public class ApiCustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        String userMessage = messageSource.getMessage("invalid.message",null, LocaleContextHolder.getLocale());
        String exMessage = Optional.of(ex.getCause().toString()).orElse(ex.getMessage());

        return handleExceptionInternal(ex, new CustomApiErrorMessage(userMessage,exMessage),headers, HttpStatus.BAD_REQUEST, request);
    }



    @Data
    @AllArgsConstructor
    public static class CustomApiErrorMessage{

        private String userMessage;
        private String exceptionMessage;


    }

}
