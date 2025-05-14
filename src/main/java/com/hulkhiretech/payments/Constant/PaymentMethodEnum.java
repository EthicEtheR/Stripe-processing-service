package com.hulkhiretech.payments.Constant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum PaymentMethodEnum {
    APM(1,"APM");

    private final int id;
    private final String name;
    PaymentMethodEnum(int id, String name){
        this.id=id;
        this.name=name;
    }

    public static PaymentMethodEnum getByName(String txnStatus) {
        for (PaymentMethodEnum status : PaymentMethodEnum.values()) {
            if (status.name.equalsIgnoreCase(txnStatus)) {
                return status;
            }
        }
        log.info("No statusEnum found:{}",txnStatus);
        return null;
    }
    public static PaymentMethodEnum getById(int id) {
        for (PaymentMethodEnum status : PaymentMethodEnum.values()) {
            if (status.id == id) {
                return status;
            }
        }
        log.info("No statusEnum found :{}",id);
        //TODO exception handling
        return null;
    }


}
