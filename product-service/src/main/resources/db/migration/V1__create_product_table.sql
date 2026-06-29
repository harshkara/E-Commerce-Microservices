CREATE TABLE Product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description VARCHAR(1000),
                         price DECIMAL(10,2) NOT NULL,
                         stock INTEGER NOT NULL,
                         category VARCHAR(100),
                         active BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP,
                         updated_at TIMESTAMP
);