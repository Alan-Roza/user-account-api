-- create table users
CREATE TABLE users (
    id serial not null,
    blocking_datetime timestamp,
    email varchar(255),
    fail_tries_counter integer,
    name varchar(255),
    password varchar(255),
    primary key (id)
);
-- Insert users
INSERT INTO users (name, email, password, fail_tries_counter) VALUES
  ('John Doe', 'john@example.com', 'secure456', 0),
  ('test user', 'test@example.com', 'password123', 0);
