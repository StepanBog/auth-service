package ru.bogdanov.diplom.data.exception;


import ru.bogdanov.diplom.grpc.generated.error.ErrorCode;

public class NotFoundServiceException extends AuthServiceException {

    public NotFoundServiceException(String message, ErrorCode errorCode, int httpCode) {
        super(message, errorCode, httpCode);
    }
}
