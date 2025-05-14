package com.hulkhiretech.payments.util;

import com.hulkhiretech.payments.Constant.ProviderEnum;
import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import org.modelmapper.AbstractConverter;

import java.util.Objects;

public class TransactionStatusEnumConvert extends AbstractConverter<String,Integer> {

    @Override
    protected Integer convert(String s) {
        return (TransactionStatusEnum.getByName(s)).getId();
    }
}
