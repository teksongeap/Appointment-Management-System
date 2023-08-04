package project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import project.DAO.AppointmentDAO;
import project.DAO.CustomerDAO;
import project.DAO.UserAndContactDAO;
import project.model.Appointment;
import project.model.Contact;
import project.model.Customer;
import project.model.User;
import project.utilities.AlertMessage;
import project.utilities.TimeUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * This class is the controller for the 'AddAppointment' view.
 * It provides functionalities for adding a new appointment to the system.
 *
 * @author Teksong Eap
 */
public class AddAppointmentController implements Initializable {

    /** Stage object */
    private Stage stage;

    /** Scene object */
    private Parent scene;

    /** Appointment ID field */
    @FXML
    private TextField appointmentId;

    /** Title field */
    @FXML
    private TextField title;

    /** Description field */
    @FXML
    private TextField description;

    /** Location field */
    @FXML
    private TextField location;

    /** Type field */
    @FXML
    private TextField type;

    /** Contact combo box */
    @FXML
    private ComboBox<Contact> contact;

    /** Customer combo box */
    @FXML
    private ComboBox<Customer> customer;

    /** User combo box */
    @FXML
    private ComboBox<User> user;

    /** Start date picker */
    @FXML
    private DatePicker startDate;

    /** End date picker */
    @FXML
    private DatePicker endDate;

    /** Start time field */
    @FXML
    private TextField startTime;

    /** End time field */
    @FXML
    private TextField endTime;

    /** Add button */
    @FXML
    private Button addButton;

    /** Cancel button */
    @FXML
    private Button cancelButton;

    /**
     * Handles the action of the 'add appointment' button being clicked.
     * It retrieves the inputs from the text fields and combo boxes, validates them, and adds a new appointment
     * to the database if the inputs are valid. After adding the appointment, it navigates back to the
     * 'AppointmentsAndCustomers' view.
     *
     * @param actionEvent ActionEvent object
     * @throws IOException if there is an error loading
     */
    public void addAppointment(ActionEvent actionEvent) throws IOException {
        if (isAnyFieldEmpty()) {
            AlertMessage.showAlert(9);
            return;
        }

        try {
            Appointment newAppointment = createAppointmentFromFormInputs();
            if (newAppointment == null) {
                return;
            }

            if (isAppointmentWithinBusinessHours(newAppointment)) {
                if (!AppointmentDAO.checkForOverlap(newAppointment)) {
                    if (addAppointmentToDBAndNavigateBack(newAppointment, actionEvent)) {
                        return;
                    }
                    AlertMessage.showAlert(11);
                } else {
                    AlertMessage.showAlert(14);
                }
            } else {
                AlertMessage.showAlert(12);
            }
        } catch (SQLException | DateTimeParseException e) {
            e.printStackTrace();
            AlertMessage.showAlert(13);
        }
    }

    /**
     * Checks if any of the required text fields is empty.
     * The method uses Java Streams to create a Stream of Strings that represent the text content
     * of various text fields like title, description, location, and type. It then applies the anyMatch
     * operation on the Stream with a predicate lambda function.
     *
     * LAMBDA EXPRESSION: Takes in a String as a parameter from the Stream and checks if it is null or empty.
     * The predicate "(text -> text == null || text.isEmpty())" returns true if the input String is null or empty,
     * and false otherwise.
     *
     * @return true if any of the required text fields is empty, false otherwise
     */
    private boolean isAnyFieldEmpty() {
        return Stream.of(title.getText(), description.getText(), location.getText(), type.getText())
                .anyMatch(text -> text == null || text.isEmpty());
    }

    /**
     * Creates a new appointment from the inputs in the form fields.
     * Error checks if start date is after end date.
     *
     * @return a new Appointment object, or null if the inputs are invalid
     * @throws DateTimeParseException if the start time or end time is not a valid time
     * @throws SQLException if there is a problem accessing the database
     */
    private Appointment createAppointmentFromFormInputs() throws DateTimeParseException, SQLException {
        int contactSelected = contact.getSelectionModel().getSelectedItem().getContactId();
        int customerSelected = customer.getSelectionModel().getSelectedItem().getCustomerId();
        int userSelected = user.getSelectionModel().getSelectedItem().getUserID();
        LocalDate startDateValue = startDate.getValue();
        LocalDate endDateValue = endDate.getValue();
        LocalTime startTimeValue = LocalTime.parse(startTime.getText());
        LocalTime endTimeValue = LocalTime.parse(endTime.getText());

        // Check if the start date is after the end date
        if (startDateValue.isAfter(endDateValue)) {
            AlertMessage.showAlert(15);
            return null;
        }
        // Check if the start time is after the end time when the start and end dates are the same
        if (startDateValue.equals(endDateValue) && startTimeValue.isAfter(endTimeValue)) {
            AlertMessage.showAlert(16);
            return null;
        }

        LocalDateTime startDateTimeLocal = LocalDateTime.of(startDateValue, startTimeValue);
        LocalDateTime endDateTimeLocal = LocalDateTime.of(endDateValue, endTimeValue);

        int unusedAppointmentId = AppointmentDAO.getUnusedAppointmentId();

        return new Appointment(unusedAppointmentId, customerSelected, userSelected,
                contactSelected, title.getText(), description.getText(), location.getText(), type.getText(),
                startDateTimeLocal, endDateTimeLocal,
                startDateValue, endDateValue, startTimeValue, endTimeValue);
    }


    /**
     * Checks if the start and end times of an appointment are within business hours.
     *
     * @param appointment the appointment to check
     * @return true if the start and end times of the appointment are within business hours, false otherwise
     */
    private boolean isAppointmentWithinBusinessHours(Appointment appointment) {
        return TimeUtil.isWithinBusinessHours(appointment.getStartDateTime())
                && TimeUtil.isWithinBusinessHours(appointment.getEndDateTime());
    }

    /**
     * Adds an appointment to the database and navigates back to the 'AppointmentsAndCustomers' view.
     *
     * @param appointment the appointment to add
     * @param actionEvent the event that triggered this method
     * @return true if the appointment was added successfully and the view was navigated back successfully,
     *         false otherwise
     * @throws SQLException if there is a problem accessing the database
     * @throws IOException if there is a problem navigating back
     */
    private boolean addAppointmentToDBAndNavigateBack(Appointment appointment, ActionEvent actionEvent) throws SQLException, IOException {
        if (AppointmentDAO.addAppointmentToDB(appointment)) {
            AlertMessage.showAlert(17);
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/AppointmentsAndCustomers.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
            return true;
        }
        return false;
    }


    /**
     * Handles the action of the 'cancel' button being clicked.
     * It displays a confirmation dialog, and if the user confirms, it navigates back to the
     * 'AppointmentsAndCustomers' view.
     *
     * @param actionEvent ActionEvent object
     * @throws IOException if there is an error loading
     */
    public void cancel(ActionEvent actionEvent) throws IOException {
        Optional<ButtonType> result = AlertMessage.showAlert(6);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/AppointmentsAndCustomers.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            System.out.println("Back in the pan.");
        }
    }

    /**
     * Initializes the controller after the FXML file has been loaded.
     * It sets up the items in the 'contact', 'user', and 'customer' combo boxes, and sets the initial focus on the 'title' text field.
     * It also sets the default values of 'startDate' and 'endDate' to the current date.
     *
     * @param url url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title.requestFocus();
        try {
            contact.setItems(UserAndContactDAO.getAllContacts());
            contact.getSelectionModel().selectFirst();
            user.setItems(UserAndContactDAO.getAllUsers());
            user.getSelectionModel().selectFirst();
            customer.setItems(CustomerDAO.getAllCustomers());
            customer.getSelectionModel().selectFirst();
            startDate.setValue(LocalDate.now());
            endDate.setValue(LocalDate.now());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
