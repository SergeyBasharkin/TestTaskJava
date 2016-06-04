package db;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Admin on 04.06.2016.
 */
public class EmployeeInfo {

    private String depCode;
    private String depJob;
    private String description;

    public EmployeeInfo() {
    }

    public EmployeeInfo(String depCode, String depJob, String description) {
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }


    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getDepJob() {
        return depJob;
    }

    public void setDepJob(String depJob) {
        this.depJob = depJob;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
