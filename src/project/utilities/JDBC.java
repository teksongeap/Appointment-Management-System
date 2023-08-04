package project.utilities;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * This is the Java DataBase Connectivity class containing methods that open/close a connection to the database.
 *
 * @author Teksong Eap
 */
public abstract class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;

    /**
     * This method opens a connection between IntelliJ and the mySQL database.
     */
    public static void openConnection(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection Successful!");
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method closes the connection between IntelliJ and the mySQL database.
     */
    public static void closeConnection(){
        try {
            connection.close();
            System.out.println("Connection Closed!");
        }catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method returns the Connection object between IntelliJ and the mySQL database.
     * @return current connection
     */
    public static Connection getConnection() {return connection;}

}
