package com.hulkhiretech.payments.dto.stripe;

import lombok.Data;

@Data
public class StripeEventDTO {
    private  String id;
    private String type;
    private StripeDataDTO data;
}
