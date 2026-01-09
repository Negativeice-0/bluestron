package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.Statement;

public class V3__InitBlogSchema implements Migration {
    @Override public String version() { return "V3"; }
    @Override public String description() { return "Init blog schema"; }

    @Override
    public void up(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS blog_post (
                  id SERIAL PRIMARY KEY,
                  slug VARCHAR(150) UNIQUE NOT NULL,
                  title VARCHAR(200) NOT NULL,
                  summary TEXT,
                  markdown TEXT NOT NULL,
                  thumbnail_url TEXT,
                  status VARCHAR(20) NOT NULL DEFAULT 'draft' CHECK (status IN ('draft','published','archived')),
                  published_at TIMESTAMP,
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("CREATE INDEX IF NOT EXISTS idx_blog_post_slug ON blog_post(slug);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_blog_post_status ON blog_post(status);");
        }
    }

    @Override
    public void down(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS blog_post;");
        }
    }
}
