-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_card
create table if not exists card
(
    id                bigserial primary key unique,
    card_number       varchar(50),
    expiration_date   date,
    holder_name       varchar(50),
    payment_system_id bigint,
    foreign key (payment_system_id) references payment_system (id) on delete cascade
);
-- rollback DROP TABLE card
