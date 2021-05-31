CREATE TABLE users (
   id                    BIGSERIAL PRIMARY KEY,
   "name"                character varying(255) not null,
   username              character varying(255) not null,
   password              character varying(255) not null,
   authorities           character varying(255) not null
);

insert into users("name", username, password, authorities)
-- password is devcave
values ('The user',          'user', '{bcrypt}$2a$10$k/seahkMOTguhzQUN/PlbOHSzXW346Avqa7xtgS0mMt7fQMbBcZA6', 'ROLE_USER'),
       ('The administrator', 'admin', '{bcrypt}$2a$10$k/seahkMOTguhzQUN/PlbOHSzXW346Avqa7xtgS0mMt7fQMbBcZA6', 'ROLE_USER,ROLE_ADMIN');
