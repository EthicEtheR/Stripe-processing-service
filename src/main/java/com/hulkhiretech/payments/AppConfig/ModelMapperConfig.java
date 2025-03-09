package com.hulkhiretech.payments.AppConfig;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.member.FieldAccess;
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

        return modelMapper;
    }

}
