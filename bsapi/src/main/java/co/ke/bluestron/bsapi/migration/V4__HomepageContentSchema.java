package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.Statement;

public class V4__HomepageContentSchema implements Migration {
    @Override public String version() { return "V4"; }
    @Override public String description() { return "Homepage content: trainers, testimonials, featured content"; }

    @Override
    public void up(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS trainer (
                  id SERIAL PRIMARY KEY,
                  name VARCHAR(150) NOT NULL,
                  title VARCHAR(200),
                  bio TEXT,
                  image_url TEXT,
                  social_links JSONB, -- future-proof for links
                  status VARCHAR(20) NOT NULL DEFAULT 'active',
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS testimonial (
                  id SERIAL PRIMARY KEY,
                  author_name VARCHAR(150) NOT NULL,
                  author_role VARCHAR(150),
                  content TEXT NOT NULL,
                  image_url TEXT,
                  status VARCHAR(20) NOT NULL DEFAULT 'active',
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS featured_content (
                  id SERIAL PRIMARY KEY,
                  kind VARCHAR(30) NOT NULL CHECK (kind IN ('course','blog','service')),
                  ref_id INT NOT NULL,
                  position INT NOT NULL DEFAULT 0,
                  active BOOLEAN NOT NULL DEFAULT TRUE,
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("CREATE INDEX IF NOT EXISTS idx_trainer_status ON trainer(status);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_testimonial_status ON testimonial(status);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_featured_kind_position ON featured_content(kind, position);");
        }
    }

    @Override
    public void down(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS featured_content;");
            st.execute("DROP TABLE IF EXISTS testimonial;");
            st.execute("DROP TABLE IF EXISTS trainer;");
        }
    }
}
