create table Fortune (
    id bigint generated always as identity,
    message text not null,
    category varchar(100) not null,
    author text
);