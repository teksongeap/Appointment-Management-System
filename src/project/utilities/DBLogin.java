package project.utilities;

import project.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static project.utilities.JDBC.connection;

/**
 * The DBLogin class handles the database operations related to the login process.
 *
 * @author Teksong Eap
 */
public class DBLogin {
    /**
     * This method executes a SQL query to authenticate a user.
     * It returns an optional user object if the provided username and password are valid.
     *
     * @param userName The username provided by the user
     * @param password The password provided by the user
     * @return An optional user object if authentication is successful, empty optional otherwise
     */
    public static Optional<User> loginQuery(String userName, String password) {
        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userName);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(createUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * This method creates a user object from the provided result set.
     * It is used to create the user object returned by the loginQuery method.
     *
     * @param resultSet The result set from the executed SQL query
     * @return A user object with the data from the result set
     * @throws SQLException If there is an error reading the result set
     */
    private static User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("User_ID");
        String foundUserName = resultSet.getString("User_Name");
        String foundPassword = resultSet.getString("Password");
        return new User(userId, foundUserName, foundPassword);
    }

}
