package com.hulkhiretech.payments.util;

import com.hulkhiretech.payments.Constant.ProviderEnum;
import org.modelmapper.AbstractConverter;

public class ProviderEnumIdToStringConverter extends AbstractConverter<Integer,String> {

    @Override
    protected String convert(Integer s) {

        return (ProviderEnum.getById(s)).getName();
    }
}
