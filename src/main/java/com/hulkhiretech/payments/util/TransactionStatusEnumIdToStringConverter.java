package com.hulkhiretech.payments.util;

import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import org.modelmapper.AbstractConverter;

public class TransactionStatusEnumIdToStringConverter extends AbstractConverter<Integer,String> {

    @Override
    protected String convert(Integer s) {

        return (TransactionStatusEnum.getById(s)).getName();
    }
}
