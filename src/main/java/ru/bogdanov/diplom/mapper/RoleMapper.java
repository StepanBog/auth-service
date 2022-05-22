package ru.bogdanov.diplom.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.bogdanov.diplom.data.model.Role;
import ru.bogdanov.diplom.mapper.common.BoolValueMapper;
import ru.bogdanov.diplom.mapper.common.StringValueMapper;
import ru.bogdanov.diplom.mapper.common.TimestampMapper;
import ru.bogdanov.diplom.mapper.common.UUIDValueMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {

    default String transform(Role role) {
        return role.getAuthority();
    }

    default Set<Role> transform(List<ru.bogdanov.diplom.grpc.generated.auth.model.Role> roles) {
        return roles.stream()
                .map(r -> Role.builder()
                        .roleName(r.getRoleName())
                        .description(r.getDescription())
                        .build())
                .collect(Collectors.toSet());
    }

    default List<ru.bogdanov.diplom.grpc.generated.auth.model.Role> transform(Set<Role> roles) {
        return roles.stream()
                .map(r -> ru.bogdanov.diplom.grpc.generated.auth.model.Role.newBuilder()
                        .setRoleName(r.getRoleName())
                        .setDescription(r.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
