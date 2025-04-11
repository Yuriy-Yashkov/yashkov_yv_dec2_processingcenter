-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_sales_point
create table if not exists sales_point
(
    id                bigserial primary key unique,
    pos_name          varchar(255),
    pos_address       varchar(255),
    pos_inn           varchar(12),
    acquiring_bank_id bigint,
    foreign key (acquiring_bank_id) references acquiring_bank (id) on delete cascade
);
-- rollback DROP TABLE sales_point
