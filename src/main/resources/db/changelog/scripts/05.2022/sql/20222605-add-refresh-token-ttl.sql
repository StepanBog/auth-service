alter table "user"
    ADD COLUMN refresh_token_ttl bigint default 172800;