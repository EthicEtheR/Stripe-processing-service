package com.hulkhiretech.payments.util;

import com.hulkhiretech.payments.Constant.PaymentMethodEnum;
import com.hulkhiretech.payments.Constant.PaymentTypeEnum;
import org.modelmapper.AbstractConverter;

public class PaymentTypeEnumIdToStringConverter extends AbstractConverter<Integer,String> {

    @Override
    protected String convert(Integer s) {

        return (PaymentTypeEnum.getById(s)).getName();
    }
}
