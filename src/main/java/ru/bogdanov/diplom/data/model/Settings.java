package ru.bogdanov.diplom.data.model;

import lombok.*;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * @author SBogdanov
 * Сущность для хранения данных о глобальных настройках
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class Settings extends AbstractEntity implements Serializable {

    /**
     * Флаг добавления новых пользователей, если false то новые пользователи не сохраняются в системе при авторизации/ауентификации
     */
    @Builder.Default
    private boolean addNewUsers = true;

}
