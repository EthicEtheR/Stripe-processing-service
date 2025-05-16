package com.hulkhiretech.payments.dto;

import lombok.Data;

import java.util.List;

@Data
public class InitiatePaymentDTO {
    private String successUrl;
    private String cancelUrl;
    private List<LineItems> lineItems;
}
