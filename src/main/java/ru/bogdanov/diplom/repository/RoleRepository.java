package ru.bogdanov.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bogdanov.diplom.data.model.Role;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью роль
 *
 * @see Role
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM role WHERE role_name = :userRole")
    Optional<Role> findByRoleName(@Param("userRole") String userRole);
}
