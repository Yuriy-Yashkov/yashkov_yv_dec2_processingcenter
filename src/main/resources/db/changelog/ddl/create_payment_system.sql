-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_payment_system
CREATE TABLE IF NOT EXISTS payment_system
(
    id                  BIGSERIAL PRIMARY KEY UNIQUE,
    payment_system_name VARCHAR(50)
);
-- rollback DROP TABLE payment_system;
