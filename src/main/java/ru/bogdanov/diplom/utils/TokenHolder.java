package ru.bogdanov.diplom.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import static net.devh.boot.grpc.server.security.interceptors.AuthenticatingServerInterceptor.SECURITY_CONTEXT_KEY;

@UtilityClass
public class TokenHolder {

    public static UsernamePasswordAuthenticationToken getBasicAuthentication() {
        Authentication authentication = SECURITY_CONTEXT_KEY.get().getAuthentication();

        return authentication instanceof UsernamePasswordAuthenticationToken ?
                (UsernamePasswordAuthenticationToken) authentication :
                null;
    }

    public static PreAuthenticatedAuthenticationToken getBearerAuthentication() {
        Authentication authentication = SECURITY_CONTEXT_KEY.get().getAuthentication();

        return authentication instanceof PreAuthenticatedAuthenticationToken ?
                (PreAuthenticatedAuthenticationToken) authentication :
                null;
    }

    public static AbstractAuthenticationToken getToken() {
        Authentication authentication = SECURITY_CONTEXT_KEY.get().getAuthentication();

        return authentication instanceof AbstractAuthenticationToken ?
                (AbstractAuthenticationToken) authentication :
                null;
    }
}
