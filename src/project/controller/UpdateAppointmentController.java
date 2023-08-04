package project.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * Controller for the 'UpdateAppointment' view.
 * It handles user interactions within the view, and performs necessary operations such as
 * checking input validity, updating an appointment's data, and navigating back to the main view.
 *
 * @author Teksong Eap
 */
public class UpdateAppointmentController implements Initializable {

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

    /** Update button */
    @FXML
    private Button updateButton;

    /** Cancel button */
    @FXML
    private Button cancelButton;

    /** Appointment to update */
    private Appointment appointmentToUpdate;


    /**
     * Handles the action of the 'update' button being clicked.
     * It validates the form input and, if valid, updates the appointment's data.
     *
     * @param actionEvent ActionEvent object
     */
    @FXML
    public void updateAppointment(ActionEvent actionEvent) {
        if (isAnyFieldEmpty()) {
            AlertMessage.showAlert(9);
            return;
        }
        try {
            updateAppointmentFromFormInputs();
            if (isAppointmentWithinBusinessHours(appointmentToUpdate)) {
                if (!AppointmentDAO.checkForOverlap(appointmentToUpdate)) {
                    if (updateAppointmentInDBAndNavigateBack(appointmentToUpdate, actionEvent)) {
                        return;
                    }
                    AlertMessage.showAlert(11);
                } else {
                    AlertMessage.showAlert(14);
                }
            } else {
                AlertMessage.showAlert(12);
            }
        } catch (SQLException | DateTimeParseException | IOException e) {
            e.printStackTrace();
            AlertMessage.showAlert(13);
        }
    }

    /**
     * Checks if any of the input fields are empty.
     *
     * @return true if any of the input fields are empty, false otherwise
     */
    private boolean isAnyFieldEmpty() {
        return Stream.of(title.getText(), description.getText(), location.getText(), type.getText())
                .anyMatch(text -> text == null || text.isEmpty());
    }

    /**
     * Checks if an appointment is within business hours.
     *
     * @param appointment the appointment to check
     * @return true if the appointment is within business hours, false otherwise
     */
    private boolean isAppointmentWithinBusinessHours(Appointment appointment) {
        return TimeUtil.isWithinBusinessHours(appointment.getStartDateTime())
                && TimeUtil.isWithinBusinessHours(appointment.getEndDateTime());
    }

    /**
     * Updates the appointmentToUpdate object's data with the data currently in the form inputs.
     *
     * @throws DateTimeParseException if the start or end time text cannot be parsed to a LocalTime
     */
    private void updateAppointmentFromFormInputs() throws DateTimeParseException {
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
            return;
        }
        // Check if the start time is after the end time when the start and end dates are the same
        if (startDateValue.equals(endDateValue) && startTimeValue.isAfter(endTimeValue)) {
            AlertMessage.showAlert(16);
            return;
        }

        LocalDateTime startDateTimeLocal = LocalDateTime.of(startDateValue, startTimeValue);
        LocalDateTime endDateTimeLocal = LocalDateTime.of(endDateValue, endTimeValue);

        // Update the fields of the appointment
        appointmentToUpdate.setCustomerId(customerSelected);
        appointmentToUpdate.setUserId(userSelected);
        appointmentToUpdate.setContactId(contactSelected);
        appointmentToUpdate.setTitle(title.getText());
        appointmentToUpdate.setDescription(description.getText());
        appointmentToUpdate.setLocation(location.getText());
        appointmentToUpdate.setType(type.getText());
        appointmentToUpdate.setStartDateTime(startDateTimeLocal);
        appointmentToUpdate.setEndDateTime(endDateTimeLocal);
        appointmentToUpdate.setStartDate(startDateValue);
        appointmentToUpdate.setEndDate(endDateValue);
        appointmentToUpdate.setStartTime(startTimeValue);
        appointmentToUpdate.setEndTime(endTimeValue);
    }

    /**
     * Attempts to update the appointment's data in the database and, if successful, navigates back to the main view.
     *
     * @param appointment the appointment whose data should be updated in the database
     * @param actionEvent the ActionEvent object representing the button click event
     * @return true if the update was successful and the user was navigated back to the main view; false otherwise
     * @throws SQLException if there is a database access error
     * @throws IOException if there is an error loading the main view
     */
    private boolean updateAppointmentInDBAndNavigateBack(Appointment appointment, ActionEvent actionEvent) throws SQLException, IOException {
        if (AppointmentDAO.updateAppointmentInDB(appointment)) {
            AlertMessage.showAlert(18);
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/AppointmentsAndCustomers.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
            return true;
        }
        return false;
    }

    /**
     * This method is used to set the appointment details in the UpdateAppointment view.
     * It takes an Appointment object as an argument, and then uses its fields to set the text and selected values of the various form inputs.
     * LAMBDA EXPRESSION: Represented by "c -> c.getContactId() == appointment.getContactId()"
     * Used in this method to conveniently and concisely filter the stream of items in the ComboBoxes.
     * Allows us to directly express what we are looking for without having to write explicit loops.
     *
     * @param appointment The Appointment object to be updated.
     */
    public void setAppointmentToUpdate(Appointment appointment) {
        this.appointmentToUpdate = appointment;
        appointmentId.setText(Integer.toString(appointment.getAppointmentId()));
        title.setText(appointment.getTitle());
        description.setText(appointment.getDescription());
        location.setText(appointment.getLocation());
        type.setText(appointment.getType());

        Contact contactToSelect = contact.getItems().stream()
                .filter(c -> c.getContactId() == appointment.getContactId())
                .findFirst()
                .orElse(null);
        contact.getSelectionModel().select(contactToSelect);

        Customer customerToSelect = customer.getItems().stream()
                .filter(c -> c.getCustomerId() == appointment.getCustomerId())
                .findFirst()
                .orElse(null);
        customer.getSelectionModel().select(customerToSelect);

        User userToSelect = user.getItems().stream()
                .filter(u -> u.getUserID() == appointment.getUserId())
                .findFirst()
                .orElse(null);
        user.getSelectionModel().select(userToSelect);

        startDate.setValue(appointment.getStartDate());
        endDate.setValue(appointment.getEndDate());
        startTime.setText(appointment.getStartTime().toString());
        endTime.setText(appointment.getEndTime().toString());
    }

    /**
     * Handles the action of the 'cancel' button being clicked.
     * It displays a confirmation dialog, and if the user confirms, it navigates back to the main view.
     *
     * @param actionEvent the ActionEvent object representing the button click event
     * @throws IOException if there is an error loading the main view
     */
    @FXML
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
     * It requests focus on the 'title' TextField and populates the ComboBoxes with data.
     *
     * @param url url
     * @param resourceBundle rb
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
