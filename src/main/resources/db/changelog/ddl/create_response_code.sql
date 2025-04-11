-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_response_code
create table if not exists response_code
(
    id                bigserial primary key unique,
    error_code        varchar(2),
    error_description varchar(255),
    error_level       varchar(255)
);
-- rollback DROP TABLE response_code
