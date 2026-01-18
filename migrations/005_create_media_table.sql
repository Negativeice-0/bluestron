-- Media files are the digital artifacts – videos, images, documents.
CREATE TABLE media (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT REFERENCES courses(id) ON DELETE CASCADE,
    filename VARCHAR(255) NOT NULL,
    original_name VARCHAR(255),
    mime_type VARCHAR(100),
    file_path VARCHAR(500) NOT NULL,    -- Local path relative to uploads/
    file_size BIGINT,
    duration_seconds INTEGER,           -- For videos
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);