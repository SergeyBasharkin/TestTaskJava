package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Admin on 04.06.2016.
 */
public class Helper {
    public static Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
        Properties properties=new Properties();
        FileInputStream in=new FileInputStream("config/jdbc.properties");
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
