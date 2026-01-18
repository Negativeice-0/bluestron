-- Blog posts are the knowledge articles – stories, tutorials, insights.
CREATE TABLE blog_posts (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(300) NOT NULL,
    slug VARCHAR(300) NOT NULL UNIQUE,
    excerpt TEXT,
    content TEXT NOT NULL,
    author_name VARCHAR(100),
    cover_image_path VARCHAR(500),       -- Reference to media table
    published BOOLEAN DEFAULT FALSE,
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_blog_published ON blog_posts(published, published_at DESC);