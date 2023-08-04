package project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import project.model.User;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import static project.utilities.DBLogin.loginQuery;

/**
 * The LoginController class handles the logic for the login form.
 * This includes the login process, language localization and the login log.
 *
 * @author Teksong Eap
 */
public class LoginController implements Initializable {

    /** Current user */
    private static User currentUser;

    /** Locale toggle group */
    @FXML
    public ToggleGroup localeToggleGroup;

    /** Username label */
    public Label usernameLabel;

    /** Password label */
    public Label passwordLabel;

    /** Login button */
    public Button loginButton;

    /** Reset button */
    public Button resetButton;

    /** Title label */
    public Label titleLabel;

    /** Username text field */
    @FXML
    private TextField usernameTextField;

    /** Password field */
    @FXML
    private PasswordField passwordTextField;

    /** Time zone label */
    @FXML
    private Label timeZoneLabel;

    /** English radio button */
    @FXML
    private RadioButton englishRadioButton;

    /** French radio button */
    @FXML
    private RadioButton frenchRadioButton;

    /**
     * This method is called when the login button is clicked.
     * It attempts to authenticate the user and log the login attempt.
     * If authentication is successful, it loads the Appointments view and sets showAppointmentAlert to true.
     * If not, it shows an error message and asks the user to try again.
     *
     * @param actionEvent Event created by button click
     * @throws IOException If there is an issue loading the Appointments view
     */
    @FXML
    private void loginButtonAction(ActionEvent actionEvent) throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        LocalDateTime now = LocalDateTime.now();
        String fileName = "login_activity.txt";

        FileWriter fileWriter = new FileWriter(fileName, true);
        PrintWriter loginLog = new PrintWriter(fileWriter);

        if (authenticate(username, password)) {
            AppointmentsAndCustomersController.setShowAppointmentAlert(true);
            System.out.println(currentUser + "'s login successful!");
            // Define stage and scene objects
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/AppointmentsAndCustomers.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();

            //log the successful login
            loginLog.println(username + "'s login was successful at " + now + " (" + ZoneId.systemDefault() + ")");
        }  else {
            System.out.println(currentUser + "'s login unsuccessful!");
            // Load the appropriate resource bundle depending on the default locale
            ResourceBundle rb = ResourceBundle.getBundle(
                    Locale.getDefault().getLanguage().equals("fr") ? "project/utilities/Messages_fr" : "project/utilities/Messages_en",
                    Locale.getDefault());

            // Construct the error message
            String errorMessage;
            if (Locale.getDefault().getLanguage().equals("fr")) {
                errorMessage = String.format("%s %s %s/%s %s. %s!", rb.getString("Invalid"), rb.getString("Username"),
                        rb.getString("and"), rb.getString("or"), rb.getString("Password"), rb.getString("Pleasetryagain"));
            } else {
                errorMessage = "Invalid Username and/or Password. Please try again!";
            }

            // Display the error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(errorMessage);
            alert.showAndWait();

            // Log the unsuccessful login
            loginLog.println(username + "'s login was unsuccessful at " + now + " (" + ZoneId.systemDefault() + ")");
        }
        loginLog.close();

    }

    /**
     * This method is called when the reset button is clicked.
     * It clears the username and password text fields.
     */
    @FXML
    private void resetButtonAction() {
        // Clear the text fields
        usernameTextField.clear();
        passwordTextField.clear();
    }

    /**
     * This method authenticates the provided username and password.
     *
     * @param username The provided username
     * @param password The provided password
     * @return True if authentication is successful, false otherwise
     * @throws IOException If there is an error during authentication
     */
    private boolean authenticate(String username, String password) throws IOException {
        Optional<User> userResult = loginQuery(username, password);
        if (userResult.isPresent()) {
            currentUser = userResult.get();
            return true;
        } else {
            return false;
        }
    }

    /**
     * gets current user
     * @return user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * This method calls englishify().
     *
     * @param actionEvent Event created by button click
     */
    public void setLocaleEnglish(ActionEvent actionEvent) {
        englishify();
    }

    /**
     * This method calls frenchify().
     *
     * @param actionEvent Event created by button click
     */
    public void setLocaleFrench(ActionEvent actionEvent) {
        frenchify();
    }

    /**
     * This method sets the locale to French and updates the labels.
     */
    public void frenchify() {
        Locale.setDefault(new Locale("fr"));
        ResourceBundle frb = ResourceBundle.getBundle("project/utilities/Messages_fr", Locale.getDefault());
        usernameLabel.setText(frb.getString("Username"));
        passwordLabel.setText(frb.getString("Password"));
        resetButton.setText(frb.getString("Reset"));
        loginButton.setText(frb.getString("Login"));
        titleLabel.setText(frb.getString("Title"));
    }

    /**
     * This method sets the locale to English and updates the labels.
     */
    public void englishify() {
        Locale.setDefault(new Locale("en"));
        ResourceBundle erb = ResourceBundle.getBundle("project/utilities/Messages_en", Locale.getDefault());
        usernameLabel.setText(erb.getString("Username"));
        passwordLabel.setText(erb.getString("Password"));
        resetButton.setText(erb.getString("Reset"));
        loginButton.setText(erb.getString("Login"));
        titleLabel.setText(erb.getString("Title"));
    }

    /**
     * This method initializes the login form.
     * It is called automatically when the form is loaded.
     * It sets the timeZoneLabel to the current time zone and sets the default locale.
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login form initialized.");
        timeZoneLabel.setText(ZoneId.systemDefault().toString());

        if (Locale.getDefault().getLanguage().equals("fr")) {
            frenchRadioButton.fire();
            frenchify();
        }
    }

}
