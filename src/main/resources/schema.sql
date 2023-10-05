DROP TABLE IF EXISTS point;
DROP TABLE IF EXISTS user;

create table point (
                       id bigint auto_increment,
                       sequence integer,
                       reg_dt datetime(6),
                       upd_dt datetime(6),
                       user_id bigint,
                       value bigint,
                       primary key (id)
);

create table user
(
    id    bigint auto_increment
        primary key,
    email varchar(255)           null,
    name  varchar(255)           null,
    role  enum ('ADMIN', 'USER') null
);
