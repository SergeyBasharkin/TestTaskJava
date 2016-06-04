package view;

import db.EmployeeInfo;
import db.EmployeeToXML;
import parser.JaxbParser;
import util.Helper;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Admin on 04.06.2016.
 */
public class Console {
    private File file;
    public void start(){
        System.out.println("Write \"1\" to download file or \"2\" to upload file");
        Scanner scanner=new Scanner(System.in);
        int choose=0;
        while (choose!=1&&choose!=2) {
            choose=scanner.nextInt();
            switch (choose) {
                case (0):
                    System.out.println("Incorrect number or string try again");
                case (1):download();
                    break;
                case (2)://upload
                    break;
                default:
                    choose = 0;
                    break;
            }
        }
    }
    public void createFileIfNotExist(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Write path of file");
        String path=scanner.nextLine();
        file=new File(path);
        String choose = "";
        if (!file.exists()){
            while (!choose.equals("y")) {
                System.out.println("file is not found. Create new file?(y/n)");
                choose = scanner.nextLine();
                if (choose.equals("y")) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (choose.equals("n")){
                        createFileIfNotExist();
                    }
                }
            }
        }
        download();
    }
    public void download(){
        if (file==null||!file.exists()) {
            createFileIfNotExist();
        }
        ArrayList<EmployeeInfo> employeeInfos=new ArrayList<>();
        try {
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
