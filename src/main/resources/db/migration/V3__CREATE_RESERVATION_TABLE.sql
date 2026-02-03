create table if not exists reservations
(
    id                bigint auto_increment
        primary key,
    created_at        datetime(6) default CURRENT_TIMESTAMP(6),
    available_slot_id bigint not null,
    user_id           bigint not null,
    constraint UK__reservation__available_slot
        unique (available_slot_id),
    constraint FK__reservation__user
        foreign key (user_id) references users (id),
    constraint FK__reservation__available_slot
        foreign key (available_slot_id) references available_slots (id)
);