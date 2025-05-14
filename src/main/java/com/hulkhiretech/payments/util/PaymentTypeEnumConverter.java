package com.hulkhiretech.payments.util;

import com.hulkhiretech.payments.Constant.PaymentTypeEnum;
import org.modelmapper.AbstractConverter;

import java.util.Objects;

public class PaymentTypeEnumConverter extends AbstractConverter<String,Integer> {

    @Override
    protected Integer convert(String s) {

        return (PaymentTypeEnum.getByName(s)).getId();
    }
}
