DROP TABLE IF EXISTS user;

create table user
(
    id    bigint auto_increment
        primary key,
    email varchar(255)           null,
    name  varchar(255)           null,
    role  enum ('ADMIN', 'USER') null
);
