package project.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.model.Contact;
import project.model.Country;
import project.model.User;
import project.utilities.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User and Contact DAO handles the database interactions for contact and user.
 *
 * @author Teksong Eap
 */
public class UserAndContactDAO {

    /**
     * Retrieves all contacts from the database.
     *
     * @return an ObservableList containing all contacts in the database
     * @throws SQLException if a database access error occurs
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM CONTACTS";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String contactName = rs.getString("CONTACT_NAME");
                int contactId = rs.getInt("CONTACT_ID");
                Contact contact = new Contact(contactId, contactName);
                allContacts.add(contact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return allContacts;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return an ObservableList containing all users in the database
     * @throws SQLException if a database access error occurs
     */
    public static ObservableList<User> getAllUsers() throws SQLException {
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM USERS";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int userId = rs.getInt("USER_ID");
                String userName = rs.getString("USER_NAME");
                String password = rs.getString("PASSWORD");
                User user = new User(userId, userName, password);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return allUsers;
    }


}
