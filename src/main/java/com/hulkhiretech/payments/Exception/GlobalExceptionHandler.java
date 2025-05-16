package com.hulkhiretech.payments.Exception;

import com.hulkhiretech.payments.Constant.ErrorCodeEnum;
import com.hulkhiretech.payments.pojo.ErrorRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProccessingException.class)
    public ResponseEntity<ErrorRes> handleStripeCustomException(ProccessingException ex) {
        ErrorRes errorRes= new ErrorRes();

        errorRes.setErrorCode(ex.getErrorCode());
        errorRes.setErrorMessage(ex.getErrorMessage());

        return new ResponseEntity<>(errorRes, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRes> handleGenericException(Exception ex) {
        log.error("Generic exception handle :{}",ex);
        ErrorRes errorRes= new ErrorRes();

        errorRes.setErrorCode(ErrorCodeEnum.GENERIC_ERROR.getErrorCode());
        errorRes.setErrorMessage(ErrorCodeEnum.GENERIC_ERROR.getErrorMessage());


        return new ResponseEntity<>(errorRes, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
