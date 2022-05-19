package ru.bogdanov.diplom.data.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author SBogdanov
 * Сущность для хранения данных о пользователе
 */
@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class User extends AbstractEntity implements Serializable {

    /**
     * Идентификатор  работника
     */
    private UUID employeeId;

    /**
     * Идентификатор  работодателя
     */
    private UUID employerId;

    /**
     * Логин работника
     */
    private String login;

    /**
     * Пароль пользователя
     */
    private String password;

    /**
     * Токен для обновления
     */
    private String refreshToken;

    /**
     * Флаг доступности сервиса
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private boolean enable = true;

    /**
     * Время жизни токена в секундах (если не заданно то используется дефолтное значение заданное в настройках)
     */
    @Builder.Default
    private Long tokenTtl = 1800L;

    /**
     * Время жизни токена обновления в секундах (если не заданно то используется дефолтное значение заданное в настройках)
     */
    @Builder.Default
    private Long refreshTokenTtl = 172800L;

    /**
     * Роли пользователя
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
