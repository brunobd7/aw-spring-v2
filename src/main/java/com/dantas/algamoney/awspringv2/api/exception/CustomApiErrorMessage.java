package com.dantas.algamoney.awspringv2.api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomApiErrorMessage {

    private String userMessage;
    private String exceptionMessage;


}
