package com.hulkhiretech.payments.AppConfig;

import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.entity.TransactionEntity;
import com.hulkhiretech.payments.util.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Enable strict matching
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true) // Match fields explicitly
                .setFieldAccessLevel(PRIVATE) // Allow private fields
                .setMatchingStrategy(MatchingStrategies.STRICT); // Use STRICT strategy

        Converter<String,Integer> paymentMethodEnumConverter=new PaymentMethodEnumConverter();
        Converter<String,Integer> PaymentTypeEnumConverter=new PaymentTypeEnumConverter();
        Converter<String,Integer> ProviderEnumConverter=new ProviderEnumConverter();
        Converter<String,Integer> TransactionStatusEnumConverter=new TransactionStatusEnumConvert();
        PaymentMethodEnumIdToStringConverter paymentMethodEnumIdToStringConverter=
                new PaymentMethodEnumIdToStringConverter();
        PaymentTypeEnumIdToStringConverter paymentTypeEnumIdToStringConverter=
                new PaymentTypeEnumIdToStringConverter();
        ProviderEnumIdToStringConverter providerEnumIdToStringConverter
                =new ProviderEnumIdToStringConverter();
        TransactionStatusEnumIdToStringConverter transactionStatusEnumIdToStringConverter=
                new TransactionStatusEnumIdToStringConverter();

        modelMapper.addMappings(new PropertyMap<TransactionDTO, TransactionEntity>() {

            @Override
            protected void configure() {
                using(paymentMethodEnumConverter).map(source.getPaymentMethod(),destination.getPaymentMethodId());
                using(PaymentTypeEnumConverter).map(source.getPaymentType(),destination.getPaymentTypeId());
                using(ProviderEnumConverter).map(source.getProvider(),destination.getProviderId());
                using(TransactionStatusEnumConverter).map(source.getTxnStatus(),destination.getTxnStatusId());
            }
        });
        modelMapper.addMappings(new PropertyMap<TransactionEntity, TransactionDTO>() {
            @Override
            protected void configure() {
                using(paymentMethodEnumIdToStringConverter)
                        .map(source.getPaymentMethodId(),destination.getPaymentMethod());
                using(paymentTypeEnumIdToStringConverter)
                        .map(source.getPaymentTypeId(),destination.getPaymentType());
                using(providerEnumIdToStringConverter)
                        .map(source.getProviderId(),destination.getProvider());
                using(transactionStatusEnumIdToStringConverter)
                        .map(source.getTxnStatusId(),destination.getTxnStatus());


            }
        });

        return modelMapper;
    }

}
