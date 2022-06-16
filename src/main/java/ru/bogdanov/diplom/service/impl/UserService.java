package ru.bogdanov.diplom.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bogdanov.diplom.config.TokenConfigurationProperties;
import ru.bogdanov.diplom.data.exception.NotFoundServiceException;
import ru.bogdanov.diplom.data.model.Role;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.grpc.generated.auth.user.UserSearchRequest;
import ru.bogdanov.diplom.grpc.generated.common.ErrorCode;
import ru.bogdanov.diplom.grpc.generated.common.UserRole;
import ru.bogdanov.diplom.repository.RoleRepository;
import ru.bogdanov.diplom.repository.UserRepository;
import ru.bogdanov.diplom.repository.specification.entity.UserSpecification;
import ru.bogdanov.diplom.service.IUserService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author SBogdanov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final TokenConfigurationProperties tokenConfigurationProperties;

    @Override
    public Optional<User> existUser(@NotNull final String principal) {
        return userRepository.findByLoginEqualsIgnoreCase(principal);
    }

    @Override
    public Optional<User> existUser(@NotNull final User user) {
        return userRepository.findByEmployeeId(user.getEmployeeId());
    }

    @Override
    @Transactional
    public User create(@NotNull final User user) {
        Optional<User> existUser = existUser(user);
        if (existUser.isPresent()) {
            return existUser.get();
        }

        Role userRole = roleRepository.findByRoleName(UserRole.ROLE_EMPLOYEE.name())
                .orElse(Role
                        .builder()
                        .roleName(UserRole.ROLE_EMPLOYEE)
                        .build());

        user.setTokenTtl(tokenConfigurationProperties.getTtl());
        user.setRole(userRole);
        return userRepository.save(user);
    }

    @Override
    public User findByEmployeeId(@NotNull UUID employeeId) {
        return userRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new NotFoundServiceException("User not exist", ErrorCode.AUTHORIZATION_ERROR, 401));
    }

    @Override
    public User findById(@NotNull UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundServiceException("User not exist", ErrorCode.AUTHORIZATION_ERROR, 401));
    }

    @Override
    public User findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundServiceException("User not exist", ErrorCode.AUTHORIZATION_ERROR, 401));
    }

    @Override
    @Transactional
    public User save(User user) {
        Role role = roleRepository.save(user.getRole());
        user.setRole(role);
        user.setEnable(true);
        return userRepository.save(user);
    }
}
