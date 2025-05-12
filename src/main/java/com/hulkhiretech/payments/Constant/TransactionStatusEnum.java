package com.hulkhiretech.payments.Constant;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum TransactionStatusEnum {
    CREATED(1,"CREATED"),INITIATED(2,"INITIATED"),
    PENDING(3,"PENDING"),SUCCESS(4,"SUCCESS"),
    FAILED(5,"FAILED");

    private final int id;
    private final String name;
    TransactionStatusEnum(int id,String name){
        this.id=id;
        this.name=name;
    }

    public static TransactionStatusEnum getByName(String txnStatus) {
        for (TransactionStatusEnum status : TransactionStatusEnum.values()) {
            if (status.name.equalsIgnoreCase(txnStatus)) {
                return status;
            }
        }
        log.info("No statusEnum found:{}",txnStatus);
        return null;
    }
    public static TransactionStatusEnum getById(int id) {
        for (TransactionStatusEnum status : TransactionStatusEnum.values()) {
            if (status.id == id) {
                return status;
            }
        }
        log.info("No statusEnum found :{}",id);
        //TODO exception handling
        return null;
    }


}
