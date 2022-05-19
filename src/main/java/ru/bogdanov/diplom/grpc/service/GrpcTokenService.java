package ru.bogdanov.diplom.grpc.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import tech.inno.odp.data.exception.AuthServiceException;
import tech.inno.odp.grpc.generated.auth.token.GenerateTokenRequest;
import tech.inno.odp.grpc.generated.auth.token.RefreshTokenRequest;
import tech.inno.odp.grpc.generated.auth.token.TokenResponse;
import tech.inno.odp.grpc.generated.auth.token.TokenServiceGrpc;
import tech.inno.odp.grpc.generated.common.ErrorCode;
import tech.inno.odp.mapper.UserMapper;
import tech.inno.odp.service.ITokenService;
import tech.inno.odp.service.IUserService;
import tech.inno.odp.utils.TokenHolder;

import java.util.UUID;

@GrpcService(interceptorNames = "authorizationTokenInterceptor")
@RequiredArgsConstructor
@Slf4j
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
            tech.inno.odp.data.model.User user = tokenService.validateToken(token);
            TokenResponse response = TokenResponse.newBuilder()
                    .setUser(userMapper.transform(user))
                    .setAccessToken(token)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception ex) {
            log.error("Validate token exception", ex);
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

            log.info("Start generate token for employee with id - {}", request.getEmployeeId());

            tech.inno.odp.data.model.User user = userService.findByEmployeeId(
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
            log.error("Generate token exception", ex);
            throw new AuthServiceException("Validate token exception", ErrorCode.AUTHORIZATION_ERROR, 401);
        }
    }

    @Override
    public void refreshToken(RefreshTokenRequest request, StreamObserver<TokenResponse> responseObserver) {
        try {
            tech.inno.odp.data.model.User user = tokenService.validateRefreshToken(request.getRefreshToken());

            TokenResponse response = TokenResponse.newBuilder()
                    .setUser(userMapper.transform(user))
                    .setAccessToken(tokenService.generateUserToken(user))
                    .setRefreshToken(tokenService.generateRefreshToken(user))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Generate token exception", e);
            throw new AuthServiceException("Validate token exception", ErrorCode.AUTHORIZATION_ERROR, 401);
        }
    }
}
