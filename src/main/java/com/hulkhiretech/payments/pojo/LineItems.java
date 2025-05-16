package com.hulkhiretech.payments.pojo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data

public class LineItems {
    private int quantity;
    private String currency;
    private String productName;
    private int unitAmount;

}
