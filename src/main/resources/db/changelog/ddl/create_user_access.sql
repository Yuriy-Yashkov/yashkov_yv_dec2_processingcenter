-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_user_access
create table if not exists user_access
(
    id            bigserial primary key,
    user_login    varchar(255),
    user_password varchar(255) default '{noop}123',
    full_name     varchar(255),
    user_role     varchar(255)
);
-- rollback DROP TABLE user_access
