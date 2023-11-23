CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    fail_tries_counter INTEGER,
    blocking_datetime TIMESTAMP
);

INSERT INTO users(name, email, password, fail_tries_counter) VALUES('macarrao', 'macarrao@gmail.com', 'meumacarrao', 0);