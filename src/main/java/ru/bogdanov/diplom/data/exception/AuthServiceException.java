package ru.bogdanov.diplom.data.exception;

import lombok.Getter;
import tech.inno.odp.grpc.generated.common.ErrorCode;

public class AuthServiceException extends RuntimeException {

    @Getter
    private final ErrorCode errorCode;
    @Getter
    private final int httpCode;

    public AuthServiceException(String message, ErrorCode errorCode, int httpCode) {
        super(message);
        this.errorCode = errorCode;
        this.httpCode = httpCode;
    }
}
