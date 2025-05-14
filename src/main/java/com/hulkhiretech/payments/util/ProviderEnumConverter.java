package com.hulkhiretech.payments.util;

import com.hulkhiretech.payments.Constant.ProviderEnum;
import org.modelmapper.AbstractConverter;

import java.util.Objects;

public class ProviderEnumConverter extends AbstractConverter<String,Integer> {

    @Override
    protected Integer convert(String s) {
        return (ProviderEnum.getByName(s)).getId();
    }
}
