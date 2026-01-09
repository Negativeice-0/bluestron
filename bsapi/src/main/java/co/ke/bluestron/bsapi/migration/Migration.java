package co.ke.bluestron.bsapi.migration;

import java.sql.Connection;

//Migration interface = Blueprint 
// (defines the contract - what all migrations MUST have)
public interface Migration {
    void up(Connection conn) throws Exception;
    void down(Connection conn) throws Exception;
    String version();
    String description();
}
