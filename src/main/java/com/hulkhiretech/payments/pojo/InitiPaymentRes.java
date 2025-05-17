package com.hulkhiretech.payments.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Builder
public class InitiPaymentRes {

        private String id;
        private String paymentStatus;

        private String url;

}
