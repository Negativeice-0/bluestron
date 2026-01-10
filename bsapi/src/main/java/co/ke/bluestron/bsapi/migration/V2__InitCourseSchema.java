package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.Statement;

public class V2__InitCourseSchema implements Migration {
    @Override public String version() { return "V2"; }
    @Override public String description() { return "Init course schema"; }

    @Override
    public void up(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS course (
                  id SERIAL PRIMARY KEY,
                  slug VARCHAR(150) UNIQUE NOT NULL,
                  title VARCHAR(200) NOT NULL,
                  short_description TEXT,
                  full_description TEXT,
                  thumbnail_url TEXT,
                  category_id INT NOT NULL REFERENCES course_category(id) ON DELETE RESTRICT,
                  status VARCHAR(20) NOT NULL DEFAULT 'draft',
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_slug ON course(slug);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_category_id ON course(category_id);");
        }
    }

    @Override
    public void down(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS course;");
        }
    }
}
