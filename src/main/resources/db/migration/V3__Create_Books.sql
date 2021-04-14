CREATE TABLE books(
  id BIGSERIAL primary key,
  author_id BIGINT NOT NULL REFERENCES authors ON DELETE CASCADE,
  publisher_id BIGINT REFERENCES authors ON DELETE CASCADE,
  isbn VARCHAR(20) NOT NULL UNIQUE,
  title VARCHAR(100) NOT NULL,
  description TEXT,
  visible BOOLEAN NOT NULL DEFAULT true,
  position INT,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);

CREATE INDEX index_author_id_on_books ON books(author_id);
CREATE INDEX index_publisher_id_on_books ON books(publisher_id);
