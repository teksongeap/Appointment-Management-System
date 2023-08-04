package project.model;

/**
 * Class representing database user for logging in.
 *
 * @author Teksong Eap
 */
public class User {
    /**
     * database user ID
     * */
    private int userID;
    /**
     * database username
     * */
    private String username;
    /**
     * database user password
     */
    private String password;

    /**
     * This is the database user constructor
     *
     * @param userID database user ID
     * @param username database username
     * @param password database user password
     * */
    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    /**
     * @return the userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID database user ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the userName
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username database username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password database user password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Provides default syntax for database user information.
     * @return string concatenating userID and username
     * */
    @Override
    public String toString() {
        return ("[" + userID + "] " + username);
    }
}