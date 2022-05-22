package ru.bogdanov.diplom.grpc.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.bogdanov.diplom.data.exception.AuthServiceException;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.grpc.generated.auth.token.GenerateTokenRequest;
import ru.bogdanov.diplom.grpc.generated.auth.token.RefreshTokenRequest;
import ru.bogdanov.diplom.grpc.generated.auth.token.TokenResponse;
import ru.bogdanov.diplom.grpc.generated.auth.token.TokenServiceGrpc;
import ru.bogdanov.diplom.grpc.generated.common.ErrorCode;
import ru.bogdanov.diplom.mapper.UserMapper;
import ru.bogdanov.diplom.service.ITokenService;
import ru.bogdanov.diplom.service.IUserService;
import ru.bogdanov.diplom.utils.TokenHolder;

import java.util.UUID;

@GrpcService(interceptorNames = "authorizationTokenInterceptor")
@RequiredArgsConstructor
public class GrpcTokenService extends TokenServiceGrpc.TokenServiceImplBase {

    private final ITokenService tokenService;
    private final IUserService userService;
    private final UserMapper userMapper;

    @Override
    public void validateToken(Empty request, StreamObserver<TokenResponse> responseObserver) {
        PreAuthenticatedAuthenticationToken jwtToken = TokenHolder.getBearerAuthentication();
        if (jwtToken == null || jwtToken.getCredentials() == null) {
            throw new AuthServiceException("Invalid authentication token", ErrorCode.AUTHORIZATION_ERROR, 401);
        }
        try {
            String token = jwtToken.getCredentials().toString();
            User user = tokenService.validateToken(token);
            TokenResponse response = TokenResponse.newBuilder()
                    .setUser(userMapper.transform(user))
                    .setAccessToken(token)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            throw new AuthServiceException("Validate token exception", ErrorCode.AUTHORIZATION_ERROR, 401);
        }
    }

    @Override
    public void generateToken(GenerateTokenRequest request, StreamObserver<TokenResponse> responseObserver) {
        PreAuthenticatedAuthenticationToken jwtToken = TokenHolder.getBearerAuthentication();
        if (jwtToken == null || jwtToken.getCredentials() == null) {
            throw new AuthServiceException("Invalid authentication token", ErrorCode.AUTHORIZATION_ERROR, 401);
        }
        try {
            String token = jwtToken.getCredentials().toString();
            tokenService.validateToken(token);

            User user = userService.findByEmployeeId(
                    UUID.fromString(request.getEmployeeId())
            );

            TokenResponse response = TokenResponse.newBuilder()
                    .setUser(userMapper.transform(user))
                    .setAccessToken(tokenService.generateUserToken(user))
                    .setRefreshToken(tokenService.generateRefreshToken(user))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            throw new AuthServiceException("Validate token exception", ErrorCode.AUTHORIZATION_ERROR, 401);
        }
    }

    @Override
    public void refreshToken(RefreshTokenRequest request, StreamObserver<TokenResponse> responseObserver) {
        try {
            User user = tokenService.validateRefreshToken(request.getRefreshToken());

            TokenResponse response = TokenResponse.newBuilder()
                    .setUser(userMapper.transform(user))
                    .setAccessToken(tokenService.generateUserToken(user))
                    .setRefreshToken(tokenService.generateRefreshToken(user))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            throw new AuthServiceException("Validate token exception", ErrorCode.AUTHORIZATION_ERROR, 401);
        }
    }
}
