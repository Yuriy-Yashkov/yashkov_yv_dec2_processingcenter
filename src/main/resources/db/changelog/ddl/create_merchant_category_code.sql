-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_merchant_category_code
create table if not exists merchant_category_code
(
    id       bigserial primary key unique,
    mcc      varchar(4),
    mcc_name varchar(255)
);
-- rollback DROP TABLE merchant_category_code
