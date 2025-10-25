package org.example.resturent.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs; // 24 hours

    @Value("${jwt.issuer}")
    private String issuer;
}
