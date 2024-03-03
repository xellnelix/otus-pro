-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence client_SEQ start with 1 increment by 1;

create table client
(
    id   bigint not null primary key,
    name varchar(50)
);

create table address
(
    id bigserial not null primary key,
    street varchar(100),
    client_id bigint
);

create table phone
(
    id bigserial not null primary key,
    number varchar(20),
    client_id bigint
);