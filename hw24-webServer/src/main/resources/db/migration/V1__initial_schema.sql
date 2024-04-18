create table address (
    id bigserial not null primary key,
    street varchar(250)
);

create table client (
    id bigserial not null primary key,
    name varchar(250),
    address_id bigint references address(id)
);

create table phone (
    id bigserial not null primary key,
    number varchar(250),
    client_id bigint references client(id)
);
