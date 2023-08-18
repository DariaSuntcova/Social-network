create table posts
(
    author_id bigint,
    data      timestamp(6),
    id        bigserial not null,
    text      varchar(255),
    title     varchar(255),
    image     bytea,
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
alter table if exists posts
    add constraint FK6xvn0811tkyo3nfjk2xvqx6ns foreign key (author_id) references users