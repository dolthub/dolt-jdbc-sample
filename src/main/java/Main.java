import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// This class shows how to connect to a Dolt SQL server using JDBC, prepare and execute a SQL query, and
// use some of Dolt's version control features to describe how data changed.
public class Main {

    // JDBC URL, username, and password of Dolt server
    // Note that this URL assumes you are running a Dolt SQL server on port 11229 in case
    // you already have a MySQL server running on port 3306. Change the port in the URL
    // string here if you want to run the Dolt SQL server on a different port.
    private static final String URL = "jdbc:mysql://localhost:11229/doltdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // JDBC variables for opening and managing connection
    private static Connection connection;


    public static void main(String[] args) throws Exception {
        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Connection to Dolt database successful!");

            // Perform a sample query (SELECT)
            String query = "SELECT id, name, email FROM users";
            PreparedStatement statement = connection.prepareStatement(query);

            // Execute query and get the result
            ResultSet resultSet = statement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.printf("User [ID=%d, Name=%s, Email=%s]%n", id, name, email);
            }

            // Close the statement and result set
            resultSet.close();
            statement.close();

            // Query the dolt_diff() table function to see what changed in the most recent commit
            // Dolt stored procedures, functions, and system tables can all be accessed through JDBC.
            //  - https://docs.dolthub.com/sql-reference/version-control/dolt-sql-procedures
            //  - https://docs.dolthub.com/sql-reference/version-control/dolt-sql-functions
            //  - https://docs.dolthub.com/sql-reference/version-control/dolt-system-tables
            statement = connection.prepareStatement("select * from dolt_diff('HEAD~', 'HEAD', 'users');");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int toId = resultSet.getInt("to_id");
                String toName = resultSet.getString("to_name");
                String toEmail = resultSet.getString("to_email");
                int fromId = resultSet.getInt("from_id");
                String fromName = resultSet.getString("from_name");
                String fromEmail = resultSet.getString("from_email");
                String diffType = resultSet.getString("diff_type");

                System.out.printf("%s: From (%d, %s, %s), To (%d, %s, %s)%n", diffType, fromId, fromName, fromEmail, toId, toName, toEmail);
            }

            // Close the statement and result set
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            // Handle SQL exceptions
            System.err.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Always close the connection to avoid database connection leakage
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}