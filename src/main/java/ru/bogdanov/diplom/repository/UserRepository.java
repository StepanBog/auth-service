package ru.bogdanov.diplom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.bogdanov.diplom.data.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью пользователь
 *
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    @EntityGraph(attributePaths = { "role"})
    Optional<User> findByLoginEqualsIgnoreCase(String login);

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findByEmployeeId(UUID employeeId);

    @EntityGraph(attributePaths = { "role"})
    Optional<User> findByRefreshToken(String refreshToken);

    @Override
    @EntityGraph(attributePaths = { "role"})
    List<User> findAll(Specification<User> specification, Sort sort);

    @Override
    @EntityGraph(attributePaths = { "role"})
    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
