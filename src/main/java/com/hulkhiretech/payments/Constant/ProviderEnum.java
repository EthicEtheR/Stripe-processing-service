package com.hulkhiretech.payments.Constant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum ProviderEnum {
   STRIPE(1,"STRIPE");

    private final int id;
    private final String name;
    ProviderEnum(int id, String name){
        this.id=id;
        this.name=name;
    }

    public static ProviderEnum getByName(String txnStatus) {
        for (ProviderEnum status : ProviderEnum.values()) {
            if (status.name.equalsIgnoreCase(txnStatus)) {
                return status;
            }
        }
        log.info("No statusEnum found:{}",txnStatus);
        return null;
    }
    public static ProviderEnum getById(int id) {
        for (ProviderEnum status : ProviderEnum.values()) {
            if (status.id == id) {
                return status;
            }
        }
        log.info("No statusEnum found :{}",id);
        //TODO exception handling
        return null;
    }


}
