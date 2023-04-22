-- CREATE DATABASE bookstore_dev WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'bookstore_dev');
CREATE DATABASE bookstore_dev;
GRANT ALL PRIVILEGES ON DATABASE bookstore_dev TO dev;
