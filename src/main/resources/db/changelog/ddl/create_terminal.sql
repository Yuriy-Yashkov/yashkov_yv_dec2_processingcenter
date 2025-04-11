-- liquibase formatted sql
-- changeset Yury Yashkov:create_table_terminal
create table if not exists terminal
(
    id          bigserial primary key unique,
    terminal_id varchar(9),
    mcc_id      integer,
    pos_id      bigint,
    foreign key (mcc_id) references merchant_category_code (id) on delete cascade,
    foreign key (pos_id) references sales_point (id) on delete cascade
);
-- rollback DROP TABLE terminal
