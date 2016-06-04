package app;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Admin on 03.06.2016.
 */
public class Connecter {
    public static void main(String[] args) {
        try {
            testSelect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void testSelect() throws IOException, SQLException, ClassNotFoundException {
        Connection conn=getConnection();

        Statement statement=conn.createStatement();
        ResultSet resultSet=statement.executeQuery("SELECT * FROM task");
        if (resultSet.next()){
            System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString(3));
            System.out.println(resultSet.getString(4));
        }
        resultSet.close();
    }

    public static Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
        Properties properties=new Properties();
        FileInputStream in=new FileInputStream("jdbc.properties");
        properties.load(in);
        in.close();

        String driver=properties.getProperty("jdbc.driverClassName");
        Class.forName(driver);
        String url = properties.getProperty("jdbc.url");
        String username=properties.getProperty("jdbc.username");
        String password=properties.getProperty("jdbc.password");

        return DriverManager.getConnection(url,username,password);
    }
}
