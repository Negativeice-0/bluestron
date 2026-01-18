-- Users are the citizens of the Bluestron ecosystem.
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(200) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,  -- Bcrypt hashed
    role VARCHAR(20) DEFAULT 'USER',      -- USER, ADMIN
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);