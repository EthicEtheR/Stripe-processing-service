package com.hulkhiretech.payments.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class PaymentResDTO {
    private String id;
    @SerializedName("payment_status")
    private String paymentStatus ;
    private String status;
    private String url;

}
