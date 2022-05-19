package ru.bogdanov.diplom.config;

import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.BearerAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.CompositeGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;

@Configuration
public class AuthenticationReaderConfiguration {

    @Bean
    public GrpcAuthenticationReader compositeGrpcAuthenticationReader(List<GrpcAuthenticationReader> readers) {
        return new CompositeGrpcAuthenticationReader(readers);
    }

    @Bean
    public GrpcAuthenticationReader basicGrpcAuthenticationReader() {
        return new BasicGrpcAuthenticationReader();
    }

    @Bean
    public GrpcAuthenticationReader bearerAuthenticationReader() {
        return new BearerAuthenticationReader(token -> new PreAuthenticatedAuthenticationToken(token, token));
    }

    @Bean
    public GrpcAuthenticationReader jwtAuthenticationReader() {
        return new JWTAuthenticationReader(token -> new PreAuthenticatedAuthenticationToken(token, token));
    }

}
