package console;

import EmplException.DoubleEmployeeException;
import model.EmployeeInfo;
import model.EmployeeToXML;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.properties.PropertiesConfigurationFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import parser.JaxbParser;
import util.Helper;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * Created by Admin on 04.06.2016.
 */
public class Console {
    private static final Logger log = (Logger) LogManager.getLogger();
    private File file;

    /**
     * start app
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        int choose = 0;
        try {
            while (choose != 1 && choose != 2) {
                System.out.println("Write \"1\" to download file or \"2\" to upload file");

                choose = scanner.nextInt();
                switch (choose) {
                    case (1):
                        download();
                        break;
                    case (2):
                        upload();
                        break;
                    default:
                        System.out.println("Incorrect number or string try again");
                        choose = 0;
                        break;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Try again");
            start();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param download флаг для загрузки или выгрузки
     */
    public void createFileIfNotExist(boolean download) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write name of file");
        //строим путь
        String path="load/";
        path += scanner.nextLine();
        path = path.replace(" ", "");
        if (!path.contains(".xml")) {
            path += ".xml";
        }
        file = new File(path);
        String choose = "";
        if (!file.exists()) {
            while (!choose.equals("y") && !choose.equals("n")) {
                System.out.println("file is not found. Create new file?(y/n)");
                choose = scanner.nextLine();
                if (choose.equals("y")) {
                    try {
                        file.createNewFile();
                        log.info("create new file: "+file.getName());
                    } catch (IOException e) {
                        log.error(e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    if (choose.equals("n")) {
                        if (download) {
                            download();
                        } else {
                            try {
                                upload();
                            } catch (JAXBException e) {
                                log.error(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public void download() {
        if (file == null || !file.exists()) {
            createFileIfNotExist(true);
        }
        log.info("start download");
        HashSet<EmployeeInfo> employeeInfos = new HashSet<>();
        try {
            Connection connection = Helper.getConnection();

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT depcode,depjob,discription FROM task");
            while (resultSet.next()) {
                employeeInfos.add(new EmployeeInfo(resultSet.getString("depcode"),
                        resultSet.getString("depjob"),
                        resultSet.getString("discription")));
            }
            log.info("SELECT depcode,depjob,discription FROM task");
            EmployeeToXML employeeToXML = new EmployeeToXML();
            employeeToXML.setEmployees(employeeInfos);

            JaxbParser parser = new JaxbParser();

            parser.saveObject(file, employeeToXML);
            log.info("success download");
        } catch (IOException e) {
            log.error(e.getMessage());

            e.printStackTrace();
        } catch (SQLException e) {
            log.error(e.getMessage());

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());

            e.printStackTrace();
        } catch (JAXBException e) {
            log.error(e.getMessage());

            e.printStackTrace();
        }
    }

    public HashSet<EmployeeInfo> getFromXML() {
        HashSet<EmployeeInfo> fromXML = new HashSet<>();
        try {
            log.info("start getting info from xml");
            DocumentBuilder xml = DocumentBuilderFactory.
                    newInstance().newDocumentBuilder();
            String depCode = "", depJob = "", desc = "";
            Document doc = xml.parse(file);
            Element rootel = doc.getDocumentElement();
            NodeList nodes = rootel.getElementsByTagName("employee");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node el = nodes.item(i);
                if (el.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) el;
                    depCode = eElement.getElementsByTagName("depCode").item(0).getTextContent();
                    depJob = eElement.getElementsByTagName("depJob").item(0).getTextContent();
                    desc = eElement.getElementsByTagName("description").item(0).getTextContent();
                    EmployeeInfo employee = new EmployeeInfo(depCode, depJob, desc);
                    if (fromXML.contains(employee)) {
                        fromXML.clear();
                        log.error("xml file contains double equal employees");
                        throw new DoubleEmployeeException();
                    } else {
                        fromXML.add(employee);
                        log.info("read "+employee.toString());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            System.exit(0);
        }
        log.info("success read xml");
        return fromXML;
    }

    public void upload() throws JAXBException {
        if (file == null || !file.exists()) {
            createFileIfNotExist(false);
        }
        log.info("start upload file");
        JaxbParser parser = new JaxbParser();
        if (parser.valid(file, new File("load/valid.xsd"))) {
            System.out.println(true);
        }else {
            System.exit(0);
        }
        HashSet<EmployeeInfo> fromXML = getFromXML();

        HashSet<EmployeeInfo> fromDB = new HashSet<>();
        Connection connection = null;
        try {
            connection = Helper.getConnection();
            log.info("success connection");
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT depcode,depjob,discription FROM task");
            while (resultSet.next()) {
                fromDB.add(new EmployeeInfo(resultSet.getString("depcode"),
                        resultSet.getString("depjob"),
                        resultSet.getString("discription")));
            }

            Set<EmployeeInfo> toRemove = new HashSet<EmployeeInfo>(fromDB) {{
                removeAll(fromXML);
            }};
            Set<EmployeeInfo> toAdd = new HashSet<EmployeeInfo>(fromXML) {{
                removeAll(fromDB);
            }};
            Set<EmployeeInfo> toUpd = new HashSet<EmployeeInfo>(fromXML) {{
                retainAll(fromDB);
            }};
            Set<EmployeeInfo> toUpd2 = new HashSet<EmployeeInfo>();

            for (Iterator<EmployeeInfo> i = toUpd.iterator(); i.hasNext(); ) {
                EmployeeInfo upd = i.next();
                for (Iterator<EmployeeInfo> j = fromDB.iterator(); j.hasNext(); ) {
                    EmployeeInfo db = j.next();
                    if (upd.equals(db) && (!(upd.getDescription().equals(db.getDescription())))) {
                        toUpd2.add(upd);
                    }
                }
            }

            if (!toAdd.isEmpty()) addToDB(toAdd, connection);
            if (!toRemove.isEmpty()) removeFromDB(toRemove, connection);
            if (!toUpd2.isEmpty()) updateInDB(toUpd2, connection);
            connection.commit(); // коммитим транзакцию; обе операции будут зафиксированы
            log.info("success commit");
        } catch (Exception e) {
            try {
                log.error(e.getMessage());
                connection.rollback();
            } catch (SQLException e1) {
                log.error(e.getMessage());
                e1.printStackTrace();
            }
            log.error(e.getMessage());
            e.printStackTrace();

        }
    }

    private void updateInDB(Set<EmployeeInfo> toUpd, Connection connection) throws SQLException {
        log.info("start update db");
        for (EmployeeInfo e : toUpd) {
            PreparedStatement statement = connection.prepareStatement("UPDATE task " +
                    "SET discription=? " +
                    "WHERE depcode=? AND depjob=?");
            statement.setString(1, e.getDescription());
            statement.setString(2, e.getDepCode());
            statement.setString(3, e.getDepJob());
            statement.executeUpdate();
        }
        log.info("success update");

    }

    private void removeFromDB(Set<EmployeeInfo> toRemove, Connection connection) throws SQLException {
        log.info("start remove");
        for (EmployeeInfo e : toRemove) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM task WHERE depcode=? AND depjob=? AND discription=?");
            statement.setString(1, e.getDepCode());
            statement.setString(2, e.getDepJob());
            statement.setString(3, e.getDescription());
            statement.executeUpdate();
        }
        log.info("success remove");
    }

    private void addToDB(Set<EmployeeInfo> toAdd, Connection connection) throws SQLException {
        log.info("start add");
        for (EmployeeInfo e : toAdd) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO task(depcode,depjob,discription) VALUES(?,?,?)");
            statement.setString(1, e.getDepCode());
            statement.setString(2, e.getDepJob());
            statement.setString(3, e.getDescription());
            statement.executeUpdate();
        }
        log.info("success add");
    }
}
