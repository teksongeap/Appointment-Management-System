package project.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.controller.LoginController;
import project.model.Customer;
import project.utilities.AlertMessage;
import project.utilities.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Handles the database interactions for Customer objects.
 *
 * @author Teksong Eap
 */
public class CustomerDAO {

    /**
     * Retrieves an unused customer ID from the database. This is the highest current ID plus one.
     *
     * @return an unused customer ID
     * @throws SQLException if a database access error occurs
     */
    public static int getUnusedCustomerId() throws SQLException {
        String SQL = "SELECT MAX(Customer_ID) as MaxId FROM CUSTOMERS";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("MaxId") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        // Default ID if no entries in the table
        return 1;
    }

    /**
     * Adds a new customer to the database.
     *
     * The method sets up a SQL PreparedStatement to execute the INSERT operation,
     * and sets the parameters of the PreparedStatement using the provided Customer object's data.
     * The method then executes the SQL statement and checks the returned row count to determine whether the operation was successful.
     *
     * @param customer the customer to add to the database
     * @return true if the operation was successful; false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean addCustomerToDB(Customer customer) throws SQLException {
        String SQL = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
        int rowsAffected = 0;
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, customer.getCustomerId());
            ps.setString(2, customer.getCustomerName());
            ps.setString(3, customer.getAddress());
            ps.setString(4, customer.getPostalCode());
            ps.setString(5, customer.getPhone());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, LoginController.getCurrentUser().getUsername());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, LoginController.getCurrentUser().getUsername());
            ps.setInt(10, customer.getDivisionId());

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return rowsAffected > 0;
    }

    /**
     * Updates an existing customer in the database.
     *
     * The method sets up a SQL PreparedStatement to execute the UPDATE operation,
     * and sets the parameters of the PreparedStatement using the provided Customer object's data.
     * The method then executes the SQL statement and checks the returned row count to determine whether the operation was successful.
     *
     * @param customer the customer with updated information
     * @return true if the operation was successful; false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean updateCustomerInDB(Customer customer) throws SQLException {
        String SQL = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        int rowsAffected = 0;
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, customer.getCustomerName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(6, LoginController.getCurrentUser().getUsername());
            ps.setInt(7, customer.getDivisionId());
            ps.setInt(8, customer.getCustomerId());
            rowsAffected = ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return rowsAffected > 0;
    }

    /**
     * Retrieves all customers from the database.
     *
     * @return an ObservableList containing all customers in the database
     */
    public static ObservableList<Customer> getAllCustomers() {
        String SQL = "SELECT customers.CUSTOMER_ID, customers.CUSTOMER_NAME, customers.ADDRESS, customers.POSTAL_CODE, customers.PHONE, " +
                "first_level_divisions.DIVISION_ID, first_level_divisions.DIVISION, " +
                "countries.COUNTRY_ID, countries.COUNTRY " +
                "FROM customers " +
                "JOIN first_level_divisions ON customers.DIVISION_ID = first_level_divisions.DIVISION_ID " +
                "JOIN countries ON first_level_divisions.COUNTRY_ID = countries.COUNTRY_ID";
        return getCustomersWithQuery(SQL);
    }

    /**
     * Retrieves all customers associated with a given country ID from the database.
     *
     * @param selectedCountryId the ID of the country whose customers are to be retrieved
     * @return an ObservableList containing all customers for the selected country in the database
     */
    public static ObservableList<Customer> getCustomersByCountry(int selectedCountryId) {
        String SQL = "SELECT customers.CUSTOMER_ID, customers.CUSTOMER_NAME, customers.ADDRESS, customers.POSTAL_CODE, customers.PHONE, " +
                "first_level_divisions.DIVISION_ID, first_level_divisions.DIVISION, " +
                "countries.COUNTRY_ID, countries.COUNTRY " +
                "FROM customers " +
                "JOIN first_level_divisions ON customers.DIVISION_ID = first_level_divisions.DIVISION_ID " +
                "JOIN countries ON first_level_divisions.COUNTRY_ID = countries.COUNTRY_ID " +
                "WHERE countries.COUNTRY_ID = ?";
        return getCustomersWithQuery(SQL, selectedCountryId);
    }

    /**
     * Retrieves customers from the database using a specified query.
     *
     * The method sets up a SQL PreparedStatement using the provided query and parameters,
     * then executes the query and uses the returned ResultSet to create Customer objects and add them to an ObservableList.
     *
     * @param query the SQL query to execute
     * @param parameters the parameters to use in the query
     * @return an ObservableList containing the customers resulting from the query
     */
    private static ObservableList<Customer> getCustomersWithQuery(String query, Object... parameters) {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customer newCustomer = createCustomerFromResultSet(rs);
                    allCustomers.add(newCustomer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }


    /**
     * Creates a Customer object from a ResultSet.
     *
     * @param rs the ResultSet to convert
     * @return a Customer object with the data from the ResultSet
     * @throws SQLException if a database access error occurs
     */
    private static Customer createCustomerFromResultSet(ResultSet rs) throws SQLException {
        int customerId = rs.getInt("CUSTOMER_ID");
        String customerName = rs.getString("CUSTOMER_NAME");
        String address = rs.getString("ADDRESS");
        String postalCode = rs.getString("POSTAL_CODE");
        String phone = rs.getString("PHONE");
        int divisionId = rs.getInt("DIVISION_ID");
        String divisionName = rs.getString("DIVISION");
        int countryId = rs.getInt("COUNTRY_ID");
        String countryName = rs.getString("COUNTRY");
        return new Customer(customerId, divisionId, countryId, customerName, address, postalCode, phone, countryName, divisionName);
    }

    /**
     * Deletes a customer from the database.
     * Returns boolean of rowsAffected > 0, 0 being false and anything above true.
     *
     * @param selectedCustomerId the ID of the customer to delete
     * @return true if the operation was successful; false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean deleteCustomerFromDB(int selectedCustomerId) throws SQLException {
        String SQL = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
        ps.setInt(1, selectedCustomerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }
}
