package EmplException;

/**
 * Created by Admin on 05.06.2016.
 */
public class DoubleEmployeeException extends Exception {
    @Override
    public String getMessage() {
        return "xml file contains double equal employees";
    }
}
