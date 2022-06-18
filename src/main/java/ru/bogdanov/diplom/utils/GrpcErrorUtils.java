package ru.bogdanov.diplom.utils;

import com.google.protobuf.Any;
import com.google.rpc.Status;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import ru.bogdanov.diplom.grpc.generated.error.ErrorCode;
import ru.bogdanov.diplom.grpc.generated.error.ErrorInfoCustom;

import javax.validation.constraints.NotNull;

@UtilityClass
public class GrpcErrorUtils {

    public Status wrapErrorMessage(@NotNull final Exception e,
                                   @NotNull final ErrorCode errorCode,
                                   @NotNull final String domain,
                                   @NotNull final Integer httpCode,
                                   String errorMessage) {
        ErrorInfoCustom errorInfo = ErrorInfoCustom.newBuilder()
                .setReason(StringUtils.isEmpty(e.getMessage()) ? "" : e.getMessage())
                .setDomain(domain)
                .setHttpCode(httpCode)
                .setErrorCode(errorCode.getNumber())
                .build();

        Status.Builder builder = Status.newBuilder()
                .setCode(io.grpc.Status.INTERNAL.getCode().value())
                .addDetails(Any.pack(errorInfo));

        if (!StringUtils.isEmpty(errorMessage)) {
            builder.setMessage(errorMessage);
        }
        return builder.build();
    }

    public Status wrapErrorMessage(Exception e,
                                   ErrorCode errorCode,
                                   String domain) {
        return wrapErrorMessage(e, errorCode, domain, 400, null);
    }

    public Status wrapErrorMessage(@NotNull final Exception e,
                                   @NotNull final ErrorCode errorCode,
                                   @NotNull final String domain,
                                   @NotNull final int httpCode) {
        return wrapErrorMessage(e, errorCode, domain, httpCode, null);
    }

}
