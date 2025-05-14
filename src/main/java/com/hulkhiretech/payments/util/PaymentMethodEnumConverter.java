package com.hulkhiretech.payments.util;

import com.hulkhiretech.payments.Constant.PaymentMethodEnum;
import com.hulkhiretech.payments.Constant.TransactionStatusEnum;
import org.modelmapper.AbstractConverter;

import java.util.Objects;

public class PaymentMethodEnumConverter extends AbstractConverter<String,Integer> {

    @Override
    protected Integer convert(String s) {
        return (PaymentMethodEnum.getByName(s)).getId();
    }
}
