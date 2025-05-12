package com.hulkhiretech.payments.StripeProviderPojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data

public class CreatePaymentDto {
    private String successUrl;
    private String cancelUrl;
    private List<LineItems> lineItems;



}
