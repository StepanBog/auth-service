package ru.bogdanov.diplom.grpc.interceptor;

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.interceptors.AbstractAuthenticatingServerCallListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

import static net.devh.boot.grpc.server.security.interceptors.AuthenticatingServerInterceptor.SECURITY_CONTEXT_KEY;

@RequiredArgsConstructor
@Component
public class AuthorizationTokenInterceptor implements ServerInterceptor {

    private final GrpcAuthenticationReader compositeGrpcAuthenticationReader;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next
    ) {
        SecurityContext authentication = new SecurityContextImpl(compositeGrpcAuthenticationReader.readAuthentication(call, headers));
        Context context = Context.current().withValue(SECURITY_CONTEXT_KEY, authentication);
        Context previousContext = context.attach();
        try {
            return new AuthenticatingServerCallListener<>(next.startCall(call, headers), context);
        } finally {
            context.detach(previousContext);
        }
    }

    private static class AuthenticatingServerCallListener<ReqT> extends AbstractAuthenticatingServerCallListener<ReqT> {

        protected AuthenticatingServerCallListener(ServerCall.Listener<ReqT> delegate, Context context) {
            super(delegate, context);
        }

        @Override
        protected void attachAuthenticationContext() {
            //noop
        }

        @Override
        protected void detachAuthenticationContext() {
            //noop
        }
    }
}
