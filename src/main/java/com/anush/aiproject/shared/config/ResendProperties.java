package com.anush.aiproject.shared.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.mail.resend")
public class ResendProperties {
    private String apiKey;
}
