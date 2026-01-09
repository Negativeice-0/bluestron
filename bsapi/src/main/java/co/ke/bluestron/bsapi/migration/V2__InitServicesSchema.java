package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.Statement;

public class V2__InitServicesSchema implements Migration {
    @Override public String version() { return "V2"; }
    @Override public String description() { return "Init services schema"; }

    @Override
    public void up(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS service_offering (
                  id SERIAL PRIMARY KEY,
                  slug VARCHAR(100) UNIQUE NOT NULL,
                  title VARCHAR(200) NOT NULL,
                  description TEXT,
                  category VARCHAR(30) NOT NULL CHECK (category IN ('research','data_analysis','software')),
                  status VARCHAR(20) NOT NULL DEFAULT 'active',
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("""
                CREATE TABLE IF NOT EXISTS service_enquiry (
                  id SERIAL PRIMARY KEY,
                  service_offering_id INT NOT NULL REFERENCES service_offering(id) ON DELETE CASCADE,
                  full_name VARCHAR(150) NOT NULL,
                  email VARCHAR(150) NOT NULL,
                  phone VARCHAR(50),
                  organization VARCHAR(150),
                  message TEXT,
                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                  created_by VARCHAR(100),
                  updated_by VARCHAR(100)
                );
            """);

            st.execute("CREATE INDEX IF NOT EXISTS idx_service_offering_slug ON service_offering(slug);");
            st.execute("CREATE INDEX IF NOT EXISTS idx_service_enquiry_offering_id ON service_enquiry(service_offering_id);");
        }
    }

    @Override
    public void down(Connection conn) throws Exception {
        try (Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS service_enquiry;");
            st.execute("DROP TABLE IF EXISTS service_offering;");
        }
    }
}
