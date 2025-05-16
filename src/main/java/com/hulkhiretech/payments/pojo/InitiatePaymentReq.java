package com.hulkhiretech.payments.pojo;

import lombok.Data;

import java.util.List;

@Data
public class InitiatePaymentReq {
    private String successUrl;
    private String cancelUrl;
    private List<LineItems> lineItems;
}
