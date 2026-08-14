package com.hulkhiretech.payments.StripeProviderPojo;

import lombok.Data;

import java.util.List;


@Data
public class CreatePaymentDto {
    private String successUrl;
    private String cancelUrl;
    private List<LineItems> lineItems;



}
