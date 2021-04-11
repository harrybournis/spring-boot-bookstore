CREATE TABLE publishers(
  id BIGSERIAL primary key,
  name VARCHAR(50) NOT NULL UNIQUE,
  address VARCHAR(100),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);
