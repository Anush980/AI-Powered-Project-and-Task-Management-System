package com.anush.aiproject.shared.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.resend.Resend;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableConfigurationProperties(ResendProperties.class)
@RequiredArgsConstructor
public class ResendEmailConfig {

    private final ResendProperties props;

    @Bean
    public Resend resendApiKey() {
        return new Resend(props.getApiKey());
    }
}
