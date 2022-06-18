package ru.bogdanov.diplom.grpc.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.bogdanov.diplom.data.exception.AuthServiceException;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.grpc.generated.auth.AuthRequest;
import ru.bogdanov.diplom.grpc.generated.auth.AuthResponse;
import ru.bogdanov.diplom.grpc.generated.auth.AuthServiceGrpc;
import ru.bogdanov.diplom.grpc.generated.error.ErrorCode;
import ru.bogdanov.diplom.mapper.UserMapper;
import ru.bogdanov.diplom.service.IAuthService;
import ru.bogdanov.diplom.service.ITokenService;
import ru.bogdanov.diplom.utils.TokenHolder;

@GrpcService(interceptorNames = "authorizationTokenInterceptor")
@RequiredArgsConstructor
@Slf4j
public class GrpcAuthService extends AuthServiceGrpc.AuthServiceImplBase {

    private final ITokenService tokenService;
    private final UserMapper userMapper;
    private final IAuthService authService;

    @Override
    public void auth(AuthRequest request, StreamObserver<AuthResponse> responseObserver) {
        UsernamePasswordAuthenticationToken basicToken = TokenHolder.getBasicAuthentication();
        if (basicToken == null || basicToken.getPrincipal() == null || basicToken.getCredentials() == null) {
            throw new AuthServiceException("Invalid authentication token", ErrorCode.AUTHORIZATION_ERROR, 401);
        }

        User user = authService.authenticate(basicToken,request.getRole());
        AuthResponse response = AuthResponse.newBuilder()
                .setUser(userMapper.transform(user))
                .setAccessToken(tokenService.generateUserToken(user))
                .setRefreshToken(tokenService.generateRefreshToken(user))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
