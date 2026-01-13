-- Categories are the bedrock – the taxonomy that organizes all learning content.
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,        -- e.g., "Data Science", "Bioinformatics"
    slug VARCHAR(100) NOT NULL UNIQUE,        -- URL‑friendly identifier
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);