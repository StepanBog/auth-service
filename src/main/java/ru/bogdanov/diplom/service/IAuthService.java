package ru.bogdanov.diplom.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.grpc.generated.auth.model.UserRole;

import javax.validation.constraints.NotNull;

/**
 * @author SBogdanov
 * Сервис для работы с ауентификационными методами
 */
public interface IAuthService {

    /**
     * Ауентифицировать пользователя и сохранить если его нет
     *
     * @param basicToken токен с логином и паролем
     * @param role
     * @return ауентифицированный пользователь
     */
    User authenticate(@NotNull final UsernamePasswordAuthenticationToken basicToken, UserRole role);

}
