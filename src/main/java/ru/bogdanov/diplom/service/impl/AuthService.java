package ru.bogdanov.diplom.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bogdanov.diplom.data.exception.AuthServiceException;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.grpc.generated.common.ErrorCode;
import ru.bogdanov.diplom.service.IAuthService;
import ru.bogdanov.diplom.service.IUserService;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author SBogdanov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User authenticate(@NotNull final UsernamePasswordAuthenticationToken basicToken) {
        String principal = basicToken.getPrincipal().toString();
        String credentials = basicToken.getCredentials().toString();

        Optional<User> existUserOptional = userService.existUser(principal);

        if (existUserOptional.isEmpty()) {
            throw new AuthServiceException("User not exist", ErrorCode.AUTHORIZATION_ERROR, 401);
        }
        User existUser = existUserOptional.get();
        if (!existUser.isEnable()) {
            throw new AuthServiceException("User not enable", ErrorCode.PERMISSION_DENIED, 401);
        }

        if (StringUtils.isEmpty(existUser.getPassword())) {
            throw new AuthServiceException("User dont have user role", ErrorCode.AUTHORIZATION_ERROR, 401);
        }

        if (!passwordEncoder.matches(credentials, existUser.getPassword())) {
            throw new AuthServiceException("Incorrect password", ErrorCode.AUTHORIZATION_ERROR, 401);
        }
        return existUser;
    }
}