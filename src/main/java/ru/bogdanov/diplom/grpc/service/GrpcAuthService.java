package ru.bogdanov.diplom.grpc.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.bogdanov.diplom.data.model.User;
import tech.inno.odp.data.exception.AuthServiceException;
import tech.inno.odp.grpc.generated.auth.AuthResponse;
import tech.inno.odp.grpc.generated.auth.AuthServiceGrpc;
import tech.inno.odp.grpc.generated.common.ErrorCode;
import tech.inno.odp.mapper.UserMapper;
import tech.inno.odp.service.IAuthService;
import tech.inno.odp.service.ITokenService;
import tech.inno.odp.utils.TokenHolder;

@GrpcService(interceptorNames = "authorizationTokenInterceptor")
@RequiredArgsConstructor
@Slf4j
public class GrpcAuthService extends AuthServiceGrpc.AuthServiceImplBase {

    private final ITokenService tokenService;
    private final UserMapper userMapper;
    private final IAuthService authService;

    @Override
    public void auth(Empty request, StreamObserver<AuthResponse> responseObserver) {
        UsernamePasswordAuthenticationToken basicToken = TokenHolder.getBasicAuthentication();
        if (basicToken == null || basicToken.getPrincipal() == null || basicToken.getCredentials() == null) {
            throw new AuthServiceException("Invalid authentication token", ErrorCode.AUTHORIZATION_ERROR, 401);
        }

        User user = authService.authenticate(basicToken);
        AuthResponse response = AuthResponse.newBuilder()
                .setUser(userMapper.transform(user))
                .setAccessToken(tokenService.generateUserToken(user))
                .setRefreshToken(tokenService.generateRefreshToken(user))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
