create table if not exists available_slots
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6) default CURRENT_TIMESTAMP(6),
    end_time    datetime(6) not null,
    start_time  datetime(6) not null,
    is_reserved boolean     not null
);