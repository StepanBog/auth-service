package ru.bogdanov.diplom.service;

import org.jose4j.jwt.MalformedClaimException;
import ru.bogdanov.diplom.data.model.User;

/**
 * @author SBogdanov
 * Сервис для работы с токенами
 */
public interface ITokenService {

    /**
     * Валидация токена
     *
     * @param jwtToken переданный токен
     * @return пользователь которому соответствует токен
     */
    User validateToken(String jwtToken) throws MalformedClaimException;

    /**
     * Валидация токена для обновления токена
     *
     * @param refreshToken переданный токен
     * @return пользователь которому соответствует токен
     */
    User validateRefreshToken(String refreshToken) throws MalformedClaimException;

    /**
     * Генерация пользовательского токена
     *
     * @param user пользователь для генерации нового токена
     * @return сгенерированный токен пользователя
     */
    String generateUserToken(User user);

    /**
     * Генерация токена для получения нового accessToken
     *
     * @param user пользователь для генерации нового токена
     * @return сгенерированный токен для получения нового accessToken
     */
    String generateRefreshToken(User user);
}
