package ru.bogdanov.diplom.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtContext;
import org.springframework.stereotype.Service;
import ru.bogdanov.diplom.data.exception.AuthServiceException;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.grpc.generated.error.ErrorCode;
import ru.bogdanov.diplom.service.ITokenService;
import ru.bogdanov.diplom.service.IUserService;
import ru.bogdanov.diplom.service.TokenManager;

import java.util.UUID;

/**
 * @author SBogdanov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService {

    private final TokenManager tokenManager;
    private final IUserService userService;

    @Override
    public User validateToken(String jwtToken) throws MalformedClaimException {

        JwtContext validTokenContext = tokenManager.validateToken(jwtToken);
        UUID userId = UUID.fromString(validTokenContext.getJwtClaims().getSubject());

        User user = userService.findById(userId);
        if (!user.isEnable()) {
            throw new AuthServiceException("User not enable", ErrorCode.PERMISSION_DENIED, 401);
        }
        return user;
    }

    @Override
    public User validateRefreshToken(String refreshToken) throws MalformedClaimException {
        User user = userService.findByRefreshToken(refreshToken);
        if (!user.isEnable()) {
            throw new AuthServiceException("User not enable", ErrorCode.PERMISSION_DENIED, 401);
        }
        return user;
    }

    @Override
    public String generateUserToken(User user) {
        return tokenManager.generateToken(user);
    }

    @Override
    public String generateRefreshToken(User user) {
        String token = tokenManager.generateRefreshToken(user);
        user.setRefreshToken(token);
        userService.save(user);
        return token;
    }
}
