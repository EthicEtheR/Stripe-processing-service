package com.hulkhiretech.payments.Constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
    GENERIC_ERROR("20000","Unable to process the request,Please try again later"),
    UNABLE_TO_CONNECT_TO_STRIPE_PS("20001","Unable to connect StripeProvider"),
    ERROR_AT_STRIPE_PSP("20002","Failed process at Stripe PSP,Please try again later"),
    INVALID_TXN_REFERENCE("20003","Invalid txn reference ,No transaction found"),
    INVALID_PAYMENT_STATUS("20004","Invalid payment status . No configuration found"),
    NO_STATUS_HANDLER_FOUND("20005","Transaction status handler not found"),
    CHECKOUT_SESSION_PAYMENT_FAILED("20006","Unable to make payment successful on hosted page");

    private String errorCode;
    private String errorMessage;
    private ErrorCodeEnum(String errorCode,String errorMessage){
        this.errorCode=errorCode;
        this.errorMessage=errorMessage;
    }

}
