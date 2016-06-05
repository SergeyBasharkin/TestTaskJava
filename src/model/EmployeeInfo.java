package model;

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

    @Override
    public String toString() {
        return "EmployeeInfo{" +
                "depCode='" + depCode + '\'' +
                ", depJob='" + depJob + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeInfo that = (EmployeeInfo) o;

        if (!getDepCode().equals(that.getDepCode())) return false;
        return getDepJob().equals(that.getDepJob());

    }

    @Override
    public int hashCode() {
        int result = getDepCode().hashCode();
        result = 31 * result + getDepJob().hashCode();
        return result;
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
