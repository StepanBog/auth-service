package ru.bogdanov.diplom.grpc.service;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.bogdanov.diplom.data.exception.NotFoundServiceException;
import ru.bogdanov.diplom.grpc.generated.auth.model.User;
import ru.bogdanov.diplom.grpc.generated.auth.user.OneUserRequest;
import ru.bogdanov.diplom.grpc.generated.auth.user.UserServiceGrpc;
import ru.bogdanov.diplom.mapper.UserMapper;
import ru.bogdanov.diplom.service.IUserService;

import java.util.UUID;

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
