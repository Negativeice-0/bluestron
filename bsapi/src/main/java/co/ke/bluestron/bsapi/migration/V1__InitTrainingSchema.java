package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.Statement;

public class V1__InitTrainingSchema implements Migration {
    @Override public String version() { return "V1"; }
    @Override public String description() { return "Init training schema"; }

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

            st.execute("""
                CREATE TABLE IF NOT EXISTS course (
                  id SERIAL PRIMARY KEY,
                  slug VARCHAR(150) UNIQUE NOT NULL,
                  title VARCHAR(200) NOT NULL,
                  short_description TEXT,
                  full_description TEXT,
                  learning_outcomes TEXT[],
                  thumbnail_url TEXT,
                  category_id INT NOT NULL REFERENCES course_category(id) ON DELETE RESTRICT,
                  status VARCHAR(20) NOT NULL DEFAULT 'draft',
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS venue (
                  id SERIAL PRIMARY KEY,
                  name VARCHAR(150),
                  city VARCHAR(100),
                  address TEXT,
                  map_url TEXT,
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS course_instance (
                  id SERIAL PRIMARY KEY,
                  course_id INT NOT NULL REFERENCES course(id) ON DELETE CASCADE,
                  mode VARCHAR(20) NOT NULL CHECK (mode IN ('online','in_person')),
                  start_date DATE NOT NULL,
                  end_date DATE,
                  capacity INT,
                  status VARCHAR(20) NOT NULL DEFAULT 'open' CHECK (status IN ('open','waitlist','closed')),
                  venue_id INT REFERENCES venue(id) ON DELETE SET NULL,
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS registration (
                  id SERIAL PRIMARY KEY,
                  course_id INT NOT NULL REFERENCES course(id) ON DELETE RESTRICT,
                  course_instance_id INT REFERENCES course_instance(id) ON DELETE SET NULL,
                  full_name VARCHAR(150) NOT NULL,
                  email VARCHAR(150) NOT NULL,
                  phone VARCHAR(50),
                  organization VARCHAR(150),
                  role VARCHAR(100),
                  preferred_date DATE,
                  payment_option VARCHAR(20) NOT NULL CHECK (payment_option IN ('online','invoice')),
                  payment_status VARCHAR(20) NOT NULL DEFAULT 'pending' CHECK (payment_status IN ('pending','success','failed')),
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("CREATE INDEX IF NOT EXISTS idx_course_category_slug ON course_category(slug);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_slug ON course(slug);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_category_id ON course(category_id);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_course_instance_course_id ON course_instance(course_id);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_registration_course_id ON registration(course_id);");
        }
    }

    @Override
    public void down(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS registration;");
            st.execute("DROP TABLE IF EXISTS course_instance;");
            st.execute("DROP TABLE IF EXISTS venue;");
            st.execute("DROP TABLE IF EXISTS course;");
            st.execute("DROP TABLE IF EXISTS course_category;");
        }
    }
}
