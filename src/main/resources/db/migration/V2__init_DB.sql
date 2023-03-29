INSERT INTO users (id, login, mail, password, role)
VALUES (1, 'admin', 'mail@example.com', 'pass', 'ADMIN');

alter sequence user_seq restart with 2;