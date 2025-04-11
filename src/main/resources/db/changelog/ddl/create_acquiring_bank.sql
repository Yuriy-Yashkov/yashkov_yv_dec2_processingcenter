-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_acquiring_bank
create table if not exists acquiring_bank
(
    id               bigserial primary key unique,
    bic              varchar(9),
    abbreviated_name varchar(255)
);
-- rollback DROP TABLE acquiring_bank
