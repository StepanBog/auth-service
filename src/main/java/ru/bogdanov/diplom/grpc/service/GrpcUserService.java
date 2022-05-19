package ru.bogdanov.diplom.grpc.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import tech.inno.odp.data.exception.NotFoundServiceException;
import tech.inno.odp.grpc.generated.auth.model.User;
import tech.inno.odp.grpc.generated.auth.user.OneUserRequest;
import tech.inno.odp.grpc.generated.auth.user.UserSearchRequest;
import tech.inno.odp.grpc.generated.auth.user.UserServiceGrpc;
import tech.inno.odp.grpc.generated.auth.user.UsersResponse;
import tech.inno.odp.mapper.UserMapper;
import tech.inno.odp.service.IUserService;

import java.util.UUID;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class GrpcUserService extends UserServiceGrpc.UserServiceImplBase {

    private final IUserService userService;
    private final UserMapper userMapper;

    @Override
    public void createIfNotExist(User request, StreamObserver<User> responseObserver) {
        User user;
        try {
            user = userMapper.transform(
                    userService.findByEmployeeId(UUID.fromString(request.getEmployeeId()))
            );
        } catch (NotFoundServiceException e) {
            log.warn("Not find user by employee id {}", request.getEmployeeId());

            user = userMapper.transform(
                    userService.create(
                            userMapper.transform(request)
                    ));
        }
        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

    @Override
    public void findOne(OneUserRequest request, StreamObserver<User> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        responseObserver.onNext(
                userMapper.transform(
                        userService.findById(userId))
        );
        responseObserver.onCompleted();
    }

    @Override
    public void find(UserSearchRequest request, StreamObserver<UsersResponse> responseObserver) {
        Page<tech.inno.odp.data.model.User> findUsers = userService.find(request);
        responseObserver.onNext(
                UsersResponse
                        .newBuilder()
                        .addAllUsers(findUsers.getContent().stream()
                                .map(userMapper::transform)
                                .collect(Collectors.toList()))
                        .setTotalPages(findUsers.getTotalPages())
                        .setTotalSize(findUsers.getTotalElements())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void save(User request, StreamObserver<User> responseObserver) {
        responseObserver.onNext(
                userMapper.transform(
                        userService.save(
                                userMapper.transform(request)
                        )
                )
        );
        responseObserver.onCompleted();
    }
}
