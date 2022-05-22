package ru.bogdanov.diplom.mapper;

import org.mapstruct.*;
import ru.bogdanov.diplom.data.model.User;
import ru.bogdanov.diplom.mapper.common.BoolValueMapper;
import ru.bogdanov.diplom.mapper.common.StringValueMapper;
import ru.bogdanov.diplom.mapper.common.TimestampMapper;
import ru.bogdanov.diplom.mapper.common.UUIDValueMapper;

@Mapper(uses = {
        StringValueMapper.class,
        UUIDValueMapper.class,
        BoolValueMapper.class,
        TimestampMapper.class,
        RoleMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {

    @Mapping(target = "username", source = "login")
    @Mapping(target = "rolesList", source = "roles")
    ru.bogdanov.diplom.grpc.generated.auth.model.User transform(User user);

    @Mapping(target = "login", source = "username")
    @Mapping(target = "roles", source = "rolesList")
    User transform(ru.bogdanov.diplom.grpc.generated.auth.model.User user);

}
