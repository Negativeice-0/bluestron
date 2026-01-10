package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Creates course_category table with indexes and audit fields.
 */
public class V1__InitTrainingSchema implements Migration {
    @Override public String version() { return "V1"; }
    @Override public String description() { return "Init training schema (course_category)"; }

    @Override
    public void up(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS course_category (
                  id SERIAL PRIMARY KEY,
                  slug VARCHAR(100) UNIQUE NOT NULL,
                  name VARCHAR(150) NOT NULL,
                  description TEXT,
                  status VARCHAR(20) NOT NULL DEFAULT 'active',
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_category_slug ON course_category(slug);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_category_status ON course_category(status);");
        }
    }

    @Override
    public void down(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS course_category;");
        }
    }
}
