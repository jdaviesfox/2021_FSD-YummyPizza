// Import statements
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static java.lang.System.out;

public class databaseConnect {

    // Main method.
    public static void main(String[] args) {
    }

    // Declare static variables to be in the connect() method.
    public static final String URL = "jdbc:postgresql://localhost/jamesdaviesfox";
    public static final String USER = "jamesdaviesfox";
    public static final String PASSWORD = "password";
    public static final String DRIVER_CLASS = "org.postgresql.Driver";
    public static Connection connection;
    public static PreparedStatement pst;

    // Method to connect to the database.
    public static void connect()
    {
        // Step 1:  Load the JDCB Driver. Wrapped in try-catch statement
        //          in case driver does not load successfully.
        try {
            Class.forName(DRIVER_CLASS); }
            catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Step 2:  Make connection to the database using the static
        //          variables declared above.
        try {
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
            out.println("Connection success!"); }
            catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
