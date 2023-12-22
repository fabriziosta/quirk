package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DB_Connection {
    private static Connection conn;
    private static Statement statement;

    public static Statement prepareConnection(){
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/qwirk?user=root&password=root");
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    public static void close_conn(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
