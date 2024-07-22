package Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public  Connection connection() {
        Connection conn = null;
        try {
            
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3307/library_ms", "root", "");
        } catch (SQLException e) {
            System.out.println("sql exception: " + e.getMessage());
        }
        return conn;
    }
}
