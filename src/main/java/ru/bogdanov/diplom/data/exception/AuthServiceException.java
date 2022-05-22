package ru.bogdanov.diplom.data.exception;

import lombok.Getter;
import ru.bogdanov.diplom.grpc.generated.common.ErrorCode;

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
