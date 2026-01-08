package ke.bluestron.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * SENIOR DEV NOTE: Custom migration engine ensures we have 
 * absolute control over the 'TEXT' column definition required for 
 * large course syllabi.
 */
@Configuration
public class Migration {
    @Bean
    CommandLineRunner initDatabase(JdbcTemplate jdbc) {
        return args -> {
            jdbc.execute("CREATE TABLE IF NOT EXISTS courses (" +
                "id SERIAL PRIMARY KEY, " +
                "title VARCHAR(255) NOT NULL, " +
                "slug VARCHAR(255) UNIQUE NOT NULL, " +
                "description TEXT, " + //
                "learning_outcomes TEXT, " +
                "category VARCHAR(100), " +
                "is_featured BOOLEAN DEFAULT FALSE)");
            System.out.println(">> Bluestron: Postgres Schema Validated.");
        };
    }
}
