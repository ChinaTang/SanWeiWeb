package databases;

import org.mariadb.jdbc.Driver;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

public class DataBaseTools {
    public static void loginDB() throws ClassNotFoundException, SQLException {
        Class.forName("org.mariadb.jdbc.Driver");
        DriverManager.registerDriver(new Driver());
    }

    public static Connection connectionDB() throws SQLException {
        Properties p = new Properties();
        p.put("user","root");
        p.put("password","1234");
        Connection connection = DriverManager.getConnection(DataConstant.DB_CONNECTION, p);
        return connection;
    }

    public static Connection connectionDataSource() throws NamingException, SQLException {
        Context context = new InitialContext();
        DataSource dataSource
                = (DataSource)context.lookup("java:comp/env/jdbc/SanWeiDB");
        return dataSource.getConnection();
    }

    public static void closeResultSet(ResultSet resultSet){
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closePrepStmt(PreparedStatement preparedStatement){
        if(preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection(Connection connection){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
