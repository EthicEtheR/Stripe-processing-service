package com.hulkhiretech.payments.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@ToString
@AllArgsConstructor
public class ProccessingException extends RuntimeException{

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

}
