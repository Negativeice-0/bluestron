-- Services are Bluestron's professional offerings beyond courses.
CREATE TABLE services (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT REFERENCES categories(id) ON DELETE SET NULL,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(200) NOT NULL UNIQUE,
    description TEXT,
    icon VARCHAR(100),                   -- e.g., "data-analysis", "software-dev"
    price_model VARCHAR(50),             -- "hourly", "project", "subscription"
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_services_category ON services(category_id);