package ru.bogdanov.diplom.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bogdanov.diplom.config.TokenConfigurationProperties;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.utils.CryptoUtils;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final TokenConfigurationProperties tokenConfigurationProperties;

    @Value(value = "${ru.bogdanov.diplom-key:}")
    private char[] keyStoreSecretKey;

    @PostConstruct
    public void init() throws Exception {
        if (keyStoreSecretKey == null || keyStoreSecretKey.length == 0) {
            this.keyStoreSecretKey = RandomStringUtils.randomAscii(35).toCharArray();
        }
        CryptoUtils.initKeyStore(keyStoreSecretKey);
    }

    @SneakyThrows
    public String generateToken(User user) {
        Validate.notNull(user);

        JwtClaims claims = new JwtClaims();

        claims.setGeneratedJwtId();
        claims.setSubject(user.getId().toString());
        claims.setClaim("employerId", user.getEmployerId());
        claims.setClaim("employeeId", user.getEmployeeId());
        claims.setClaim("role", user.getRole()
        );

        long ttl = (user.getTokenTtl() != null) ?
                user.getTokenTtl() :
                tokenConfigurationProperties.getTtl();

        claims.setExpirationTimeMinutesInTheFuture(tokenConfigurationProperties.getTtlInMinutes(ttl));

        JsonWebSignature signature = new JsonWebSignature();
        signature.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        signature.setKey(CryptoUtils.getSignPrivateKey(keyStoreSecretKey));
        signature.setPayload(claims.toJson());
        return signature.getCompactSerialization();
    }

    @SneakyThrows
    public JwtContext validateToken(String token) {
        JwtConsumer consumer = new JwtConsumerBuilder()
                .setVerificationKey(CryptoUtils.getSignCertificate().getPublicKey())
                .setRequireSubject()
                .setRequireJwtId()
                .setRequireExpirationTime()
                .build();

        return consumer.process(token);
    }

    @SneakyThrows
    public String generateRefreshToken(User user) {
        Validate.notNull(user);

        JwtClaims claims = new JwtClaims();

        claims.setGeneratedJwtId();
        claims.setSubject(user.getId().toString());
        claims.setClaim("employerId", user.getEmployerId());
        claims.setClaim("employeeId", user.getEmployeeId());
        claims.setClaim("role", user.getRole()
        );

        long refreshTtl = (user.getRefreshTokenTtl() != null) ?
                user.getRefreshTokenTtl() :
                tokenConfigurationProperties.getRefreshTokenTtl();

        claims.setExpirationTimeMinutesInTheFuture(tokenConfigurationProperties.getTtlInMinutes(refreshTtl));

        JsonWebSignature signature = new JsonWebSignature();
        signature.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        signature.setKey(CryptoUtils.getSignPrivateKey(keyStoreSecretKey));
        signature.setPayload(claims.toJson());
        return signature.getCompactSerialization();
    }
}
