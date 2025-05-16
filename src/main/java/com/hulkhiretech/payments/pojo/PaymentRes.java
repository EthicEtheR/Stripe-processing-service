package com.hulkhiretech.payments.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class PaymentRes {
    private String id;
    @SerializedName("payment_status")
    private String paymentStatus ;
    private String status;
    private String url;

}
