package db;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Admin on 04.06.2016.
 */
@XmlRootElement
public class EmployeeToXML {

    List<EmployeeInfo> employees;

    public List<EmployeeInfo> getEmployees() {
        return employees;
    }

    @XmlElement(name = "employee")
    @XmlElementWrapper
    public void setEmployees(List<EmployeeInfo> employees) {
        this.employees = employees;
    }
}
