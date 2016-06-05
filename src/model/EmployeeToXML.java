package model;

import javax.xml.bind.annotation.*;
import java.util.HashSet;

/**
 * Created by Admin on 04.06.2016.
 */
@XmlRootElement
public class EmployeeToXML {

    HashSet<EmployeeInfo> employees;

    public HashSet<EmployeeInfo> getEmployees() {
        return employees;
    }

    @XmlElement(name = "employee")
    @XmlElementWrapper
    public void setEmployees(HashSet<EmployeeInfo> employees) {
        this.employees = employees;
    }
}
