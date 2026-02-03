create table if not exists users
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6) default CURRENT_TIMESTAMP(6),
    email      varchar(255) not null,
    password   varchar(255) not null,
    username   varchar(255) not null,
    constraint UK__user__email
        unique (email),
    constraint UK__user__username
        unique (username)
);