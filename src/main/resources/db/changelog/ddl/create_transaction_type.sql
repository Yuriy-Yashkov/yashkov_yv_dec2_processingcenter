-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_transaction_type
create table if not exists transaction_type
(
    id                    bigserial primary key unique,
    transaction_type_name varchar(255),
    operator              varchar(1)
);
-- rollback DROP TABLE transaction_type
