DROP TABLE IF EXISTS user;

create table user
(
    id    bigint auto_increment
        primary key,
    email varchar(255)           null,
    name  varchar(255)           null,
    role  enum ('ADMIN', 'USER') null
);

INSERT INTO user (name, email, role)
values ('이지아', 'jia@naver.com', 'USER');

INSERT INTO user (name, email, role)
values ('최지웅', 'jiwoong@naver.com', 'ADMIN');