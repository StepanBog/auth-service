package ru.bogdanov.diplom.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ru.bogdanov.token")
public class TokenConfigurationProperties {

    /**
     * Token TTL in seconds
     */
    private Long ttl = 900L;

    public long getTtlInMinutes() {
        return ttl / 60L;
    }

    public long getTtlInMinutes(long ttl) {
        return ttl / 60L;
    }

    /**
     * Refresh token TTL in seconds
     */
    private Long refreshTokenTtl = 172800L;

}
