package com.anush.aiproject.security.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank
    private final String secretKey;

    @NotNull
    private final Duration expiration;

    @NotBlank
    private final String issuer;

    
    @ConstructorBinding
    public JwtProperties(String secretKey,Duration expiration,String issuer){
        this.secretKey=secretKey;
        this.expiration=expiration;
        this.issuer=issuer;
    }

    
}
