package ru.bogdanov.diplom.grpc;

import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import tech.inno.odp.data.exception.AuthServiceException;
import tech.inno.odp.grpc.generated.common.ErrorCode;
import tech.inno.odp.utils.GrpcErrorUtils;

@Slf4j
@GrpcAdvice
public class GrpcExceptionAdvice {

    @Value(value = "${spring.application.name:odp-auth}")
    private String applicationName;

    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException handle(Exception e) {
        log.error("Unknown exception", e);
        return StatusProto.toStatusRuntimeException(
                GrpcErrorUtils.wrapErrorMessage(e, ErrorCode.UNKNOWN_ERROR, applicationName)
        );
    }

    @GrpcExceptionHandler(AuthServiceException.class)
    public StatusRuntimeException handle(AuthServiceException e) {
        log.error("Unknown exception", e);
        return StatusProto.toStatusRuntimeException(
                GrpcErrorUtils.wrapErrorMessage(e, e.getErrorCode(), applicationName, e.getHttpCode())
        );
    }
}
