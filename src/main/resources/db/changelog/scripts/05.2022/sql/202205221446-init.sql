create table role
(
    id          uuid                    not null
        constraint role_pk
            primary key,
    role_name   varchar                 not null,
    description varchar,
    created_at  timestamp default now() not null,
    updated_at  timestamp default now() not null
);

comment on table role is 'Таблица хранящая данные о ролях';
comment on column role.role_name is 'Имя роли';
comment on column role.description is 'Описание роли';
comment on column role.created_at is 'Дата создания ';
comment on column role.updated_at is 'Дата обновления ';

create table "user"
(
    id            uuid                    not null
        constraint user_pk
            primary key,
    employee_id   uuid,
    employer_id   uuid,
    login         varchar(100),
    token_ttl     bigint,
    created_at    timestamp default now() not null,
    updated_at    timestamp default now() not null,
    enable        boolean   default true,
    password      varchar   default ''::character varying,
    refresh_token varchar,
    role_id       uuid                    not null
        constraint role_user_id_fk
            references role

);


comment on table "user" is 'Таблица хранящая данные о пользователях';

comment on column "user".employee_id is 'Идентификатор  работника';
comment on column "user".employer_id is 'Идентификатор  работодателя';
comment on column "user".login is 'логин работника';
comment on column "user".created_at is 'Дата создания ';
comment on column "user".updated_at is 'Дата обновления ';
comment on column "user".enable is 'Поле доступности сервиса пользователю';
comment on column "user".password is 'Поле хранящее пароль пользователя';
comment on column "user".refresh_token is 'Токен для обновления';
comment on column "user".token_ttl is 'Время жизни создаваемого токена в секундах';
comment on column "user".role_id is 'Идентификатор роли';

