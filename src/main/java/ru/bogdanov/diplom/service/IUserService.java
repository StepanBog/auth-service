package ru.bogdanov.diplom.service;

import org.springframework.data.domain.Page;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.grpc.generated.auth.user.UserSearchRequest;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

/**
 * @author SBogdanov
 * Сервис для работы с пользователем
 */
public interface IUserService {

    /**
     * Получить пользователя по переданному идентификатору
     *
     * @param principal - идентификатор
     * @return найденный пользователь или Optional.empty() - если пользователь не найден
     */
    Optional<User> existUser(@NotNull final String principal);

    /**
     * Получить пользователя по переданному идентификатору
     *
     * @param user - сущность пользователя
     * @return найденный пользователь или Optional.empty() - если пользователь не найден
     */
    Optional<User> existUser(@NotNull final User user);

    /**
     * Добавить нового пользователя
     *
     * @param user новый пользователя
     * @return добавленный пользователь
     */
    User create(@NotNull final User user);

    /**
     * Найти пользователя по идентификатору работника
     *
     * @param employeeId - идентификатор работника
     * @return найденный пользователь
     */
    User findByEmployeeId(@NotNull final UUID employeeId);

    /**
     * Найти пользователя по id
     *
     * @param userId - userId
     * @return пользователь
     */
    User findById(@NotNull UUID userId);

    /**
     * Найти пользователя по id
     *
     * @param refreshToken - refreshToken
     * @return пользователь
     */
    User findByRefreshToken(@NotNull String refreshToken);

    User save(User user);
}
