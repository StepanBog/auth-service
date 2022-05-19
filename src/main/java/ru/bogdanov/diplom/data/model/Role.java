package ru.bogdanov.diplom.data.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import tech.inno.odp.grpc.generated.common.UserRole;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author SBogdanov
 * Сущность для хранения данных о роли пользователя
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class Role extends AbstractEntity implements GrantedAuthority, Serializable {

    /**
     * Имя роли
     */
    @Enumerated(EnumType.STRING)
    private UserRole roleName;

    /**
     * Описание роли
     */
    private String description;

    @Override
    @Transient
    public String getAuthority() {
        return roleName.name();
    }
}
