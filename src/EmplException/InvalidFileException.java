package EmplException;

/**
 * Created by Admin on 05.06.2016.
 */
public class InvalidFileException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid content. Check your xml. example xml:\n" +
                "            <?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\" standalone=\\\"yes\\\"?>\\n\" +\n" +
                "                    \"<employeeToXML>\\n\" +\n" +
                "                    \"    <employees>\\n\" +\n" +
                "                    \"        <employee>\\n\" +\n" +
                "                    \"            <depCode>43</depCode>\\n\" +\n" +
                "                    \"            <depJob>554</depJob>\\n\" +\n" +
                "                    \"            <description>3321</description>\\n\" +\n" +
                "                    \"        </employee>\\n\" +\n" +
                "                    \"    </employees>\\n\" +\n" +
                "                    \"</employeeToXML>";
    }
}
