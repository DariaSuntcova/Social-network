create table dialog
(
    id             bigserial not null,
    main_user_id   bigint unique,
    target_user_id bigint unique,
    primary key (id)
);

create table friendship
(
    id             bigserial not null,
    main_user_id   bigint unique,
    target_user_id bigint unique,
    status         varchar(255) check (status in ('FRIEND', 'SUBSCRIBED')),
    primary key (id)
);

create table images
(
    id         bigserial not null,
    size       bigint,
    image_name varchar(255),
    type       varchar(255),
    content    bytea,
    primary key (id)
);

create table notifications
(
    its_read          boolean   not null,
    author_id         bigint,
    data              timestamp(6),
    id                bigserial not null,
    target_id         bigint,
    notification_type varchar(255) check (notification_type in ('FRIEND_REQUEST', 'DIALOG')),
    primary key (id)
);

create table post_image_url_list
(
    post_id        bigint not null,
    image_url_list varchar(255)
);

create table posts
(
    author_id bigint,
    data      timestamp(6),
    id        bigserial not null,
    text      varchar(255),
    title     varchar(255),
    primary key (id)
);

create table users
(
    id       bigserial not null,
    email    varchar(255),
    login    varchar(255) unique,
    password varchar(255),
    roles    varchar(255) check (roles in ('USER', 'ADMIN')),
    primary key (id)
);

alter table if exists dialog
    add constraint main_user_id foreign key (main_user_id) references users;

alter table if exists dialog
    add constraint target_user_id foreign key (target_user_id) references users;

alter table if exists friendship
    add constraint main_user_id foreign key (main_user_id) references users;

alter table if exists friendship
    add constraint target_user_id foreign key (target_user_id) references users;

alter table if exists notifications
    add constraint author_id foreign key (author_id) references users;

alter table if exists notifications
    add constraint target_id foreign key (target_id) references users;

alter table if exists post_image_url_list
    add constraint post_id foreign key (post_id) references posts;

alter table if exists posts
    add constraint author_id foreign key (author_id) references users