package ru.bogdanov.diplom.mapper;

import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.bogdanov.diplom.data.model.Role;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.data.model.User.UserBuilder;
import ru.bogdanov.diplom.grpc.generated.auth.model.User.Builder;
import ru.bogdanov.diplom.mapper.common.TimestampMapper;
import ru.bogdanov.diplom.mapper.common.UUIDValueMapper;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private UUIDValueMapper uUIDValueMapper;
    @Autowired
    private TimestampMapper timestampMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ru.bogdanov.diplom.grpc.generated.auth.model.User transform(User user) {
        if ( user == null ) {
            return null;
        }

        Builder user1 = ru.bogdanov.diplom.grpc.generated.auth.model.User.newBuilder();

        if ( user.getLogin() != null ) {
            user1.setUsername( user.getLogin() );
        }
        if ( user.getRoles() != null ) {
            for ( Role role : user.getRoles() ) {
                user1.addRoles( roleToRole( role ) );
            }
        }
        if ( user.getId() != null ) {
            user1.setId( uUIDValueMapper.mapToString( user.getId() ) );
        }
        if ( user.getEmployerId() != null ) {
            user1.setEmployerId( uUIDValueMapper.mapToString( user.getEmployerId() ) );
        }
        if ( user.getEmployeeId() != null ) {
            user1.setEmployeeId( uUIDValueMapper.mapToString( user.getEmployeeId() ) );
        }
        user1.setEnable( user.isEnable() );
        if ( user.getPassword() != null ) {
            user1.setPassword( user.getPassword() );
        }
        if ( user.getRefreshTokenTtl() != null ) {
            user1.setRefreshTokenTtl( String.valueOf( user.getRefreshTokenTtl() ) );
        }
        if ( user.getUpdatedAt() != null ) {
            user1.setUpdatedAt( timestampMapper.mapToProto( user.getUpdatedAt() ) );
        }
        if ( user.getCreatedAt() != null ) {
            user1.setCreatedAt( timestampMapper.mapToProto( user.getCreatedAt() ) );
        }

        return user1.build();
    }

    @Override
    public User transform(ru.bogdanov.diplom.grpc.generated.auth.model.User user) {
        if ( user == null ) {
            return null;
        }

        UserBuilder<?, ?> user1 = User.builder();

        if ( user.hasUsername() ) {
            user1.login( user.getUsername() );
        }
        Set<Role> set = roleMapper.transform( user.getRolesList() );
        if ( set != null ) {
            user1.roles( set );
        }
        if ( user.hasId() ) {
            user1.id( uUIDValueMapper.mapToUUID( user.getId() ) );
        }
        if ( user.hasUpdatedAt() ) {
            user1.updatedAt( timestampMapper.mapToLocalDateTime( user.getUpdatedAt() ) );
        }
        if ( user.hasCreatedAt() ) {
            user1.createdAt( timestampMapper.mapToLocalDateTime( user.getCreatedAt() ) );
        }
        if ( user.hasEmployeeId() ) {
            user1.employeeId( uUIDValueMapper.mapToUUID( user.getEmployeeId() ) );
        }
        if ( user.hasEmployerId() ) {
            user1.employerId( uUIDValueMapper.mapToUUID( user.getEmployerId() ) );
        }
        if ( user.hasPassword() ) {
            user1.password( user.getPassword() );
        }
        if ( user.hasEnable() ) {
            user1.enable( user.getEnable() );
        }
        if ( user.hasRefreshTokenTtl() ) {
            user1.refreshTokenTtl( Long.parseLong( user.getRefreshTokenTtl() ) );
        }

        return user1.build();
    }

    protected ru.bogdanov.diplom.grpc.generated.auth.model.Role roleToRole(Role role) {
        if ( role == null ) {
            return null;
        }

        ru.bogdanov.diplom.grpc.generated.auth.model.Role.Builder role1 = ru.bogdanov.diplom.grpc.generated.auth.model.Role.newBuilder();

        if ( role.getId() != null ) {
            role1.setId( uUIDValueMapper.mapToString( role.getId() ) );
        }
        if ( role.getRoleName() != null ) {
            role1.setRoleName( role.getRoleName() );
        }
        if ( role.getDescription() != null ) {
            role1.setDescription( role.getDescription() );
        }
        if ( role.getUpdatedAt() != null ) {
            role1.setUpdatedAt( timestampMapper.mapToProto( role.getUpdatedAt() ) );
        }
        if ( role.getCreatedAt() != null ) {
            role1.setCreatedAt( timestampMapper.mapToProto( role.getCreatedAt() ) );
        }

        return role1.build();
    }
}
