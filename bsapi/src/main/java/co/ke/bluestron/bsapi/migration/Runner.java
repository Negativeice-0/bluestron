package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

/**
 * Simple Java-based migration runner. Run via Makefile:
 * make migrate
 */
public class Runner {
    public static void main(String[] args) throws Exception {
        String url = System.getenv().getOrDefault("BSAPI_DB_URL", "jdbc:postgresql://localhost:5432/bsdb");
        String user = System.getenv().getOrDefault("BSAPI_DB_USER", "bsapi_user");
        String pass = System.getenv().getOrDefault("BSAPI_DB_PASSWORD", "CHANGE_ME_LOCAL");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            List<Migration> migrations = List.of(
                new V1__InitTrainingSchema()
            );
            for (Migration m : migrations) {
                System.out.printf("Applying %s - %s%n", m.version(), m.description());
                m.up(conn);
            }
            System.out.println("Migrations applied successfully.");
        }
    }
}
