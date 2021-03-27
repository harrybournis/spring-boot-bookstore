CREATE TABLE authors(
  id BIGSERIAL primary key,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(50) NOT NULL UNIQUE
);
