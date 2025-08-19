create type gender_type as enum ('MALE','FEMALE','NON_BINARY','UNSPECIFIED');

create table users (
    id bigserial primary key,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null unique,
    gender gender_type not null default 'UNSPECIFIED',
    date_of_birth date,
    created_at timestamptz not null default now(),
    active boolean not null default true
);
