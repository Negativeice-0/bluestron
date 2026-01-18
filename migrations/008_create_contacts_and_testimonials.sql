-- Contact submissions are incoming messages from visitors.
CREATE TABLE contact_submissions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    subject VARCHAR(200),
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Testimonials are voices of satisfied learners and clients.
CREATE TABLE testimonials (
    id BIGSERIAL PRIMARY KEY,
    author_name VARCHAR(200) NOT NULL,
    author_role VARCHAR(200),            -- e.g., "Data Scientist", "Researcher"
    author_company VARCHAR(200),
    content TEXT NOT NULL,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);