package databases;

import org.mariadb.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseTools {
    public static Connection connectionJDBC() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        DriverManager.registerDriver(new Driver());
        Properties p = new Properties();
        p.put("user","root");
        p.put("password","123456");
        Connection connection = DriverManager.getConnection(DataConstant.DB_CONNECTION, p);
        return connection;
    }
}
