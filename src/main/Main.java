package main;

import db.EmployeeInfo;
import db.EmployeeToXML;
import parser.JaxbParser;
import util.Helper;
import view.Console;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Admin on 04.06.2016.
 */
public class Main {
    public static void main(String[] args) {
        Console console=new Console();
        console.start();
    }

    public static void test(){
        ArrayList<EmployeeInfo> employeeInfos=new ArrayList<>();
        try {
            File file=new File("employees.xml");
            if (!file.exists()){
                file.createNewFile();
            }
            Connection connection= Helper.getConnection();

            Statement statement=connection.createStatement();

            ResultSet resultSet=statement.executeQuery("SELECT depcode,depjob,discription FROM task");
            while (resultSet.next()){
                employeeInfos.add(new EmployeeInfo(resultSet.getString("depcode"),
                        resultSet.getString("depjob"),
                        resultSet.getString("discription")));
            }
            EmployeeToXML employeeToXML=new EmployeeToXML();
            employeeToXML.setEmployees(employeeInfos);

            JaxbParser parser=new JaxbParser();

            parser.saveObject(file,employeeToXML);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
