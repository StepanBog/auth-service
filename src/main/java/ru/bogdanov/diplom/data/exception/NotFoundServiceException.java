package ru.bogdanov.diplom.data.exception;

import tech.inno.odp.grpc.generated.common.ErrorCode;

public class NotFoundServiceException extends AuthServiceException {

    public NotFoundServiceException(String message, ErrorCode errorCode, int httpCode) {
        super(message, errorCode, httpCode);
    }
}
