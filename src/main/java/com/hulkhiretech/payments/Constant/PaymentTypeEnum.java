package com.hulkhiretech.payments.Constant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum PaymentTypeEnum {
    SALE(1,"SALE");

    private final int id;
    private final String name;
    PaymentTypeEnum(int id, String name){
        this.id=id;
        this.name=name;
    }

    public static PaymentTypeEnum getByName(String txnStatus) {
        for (PaymentTypeEnum status : PaymentTypeEnum.values()) {
            if (status.name.equalsIgnoreCase(txnStatus)) {
                return status;
            }
        }
        log.info("No statusEnum found:{}",txnStatus);
        return null;
    }
    public static PaymentTypeEnum getById(int id) {
        for (PaymentTypeEnum status : PaymentTypeEnum.values()) {
            if (status.id == id) {
                return status;
            }
        }
        log.info("No statusEnum found :{}",id);
        //TODO exception handling
        return null;
    }


}
