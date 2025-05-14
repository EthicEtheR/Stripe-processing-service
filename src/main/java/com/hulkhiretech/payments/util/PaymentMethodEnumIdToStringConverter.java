package com.hulkhiretech.payments.util;

import com.hulkhiretech.payments.Constant.PaymentMethodEnum;
import org.modelmapper.AbstractConverter;

public class PaymentMethodEnumIdToStringConverter extends AbstractConverter<Integer,String> {

    @Override
    protected String convert(Integer s) {
        return (PaymentMethodEnum.getById(s)).getName();
    }
}
