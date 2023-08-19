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
    author_id         bigint,
    date              timestamp(6),
    id                bigserial not null,
    target_id         bigint,
    notification_type varchar(255) check (notification_type in ('POST', 'FRIEND_REQUEST', 'MESSAGE')),
    read_status       varchar(255),
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

alter table if exists notifications
    add constraint FK12cqci56d05cpmlhk708u964o foreign key (author_id) references users;

alter table if exists notifications
    add constraint FKbt4366w5ppe8o9qo4w6cl3bbk foreign key (target_id) references users;

alter table if exists post_image_url_list
    add constraint FKbc95or52x5khbi11v7s01ybxh foreign key (post_id) references posts;

alter table if exists posts
    add constraint FK6xvn0811tkyo3nfjk2xvqx6ns foreign key (author_id) references users