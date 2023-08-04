package project.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import project.DAO.AppointmentDAO;
import project.DAO.CustomerDAO;
import project.model.Appointment;
import project.model.Customer;
import project.model.User;
import project.utilities.AlertMessage;
import project.utilities.TimeUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the 'AppointmentsAndCustomers' view.
 * Provides functionalities for managing and viewing appointments and customers.
 *
 * @author Teksong Eap
 */
public class AppointmentsAndCustomersController implements Initializable {
    
    /** Stage object */
    private Stage stage;

    /** Scene object */
    private Parent scene;

    /** Appointment alert flag */
    private static boolean showAppointmentAlert = false;

    /** Table of customers */
    @FXML
    private TableView<Customer> customerTableView;

    /** Customer ID column */
    @FXML
    private TableColumn<Customer, Integer> customerId;

    /** Customer name column */
    @FXML
    private TableColumn<Customer, String> name;

    /** Customer address column */
    @FXML
    private TableColumn<Customer, String> address;

    /** Customer postal code column */
    @FXML
    private TableColumn<Customer, String> postalCode;

    /** Customer phone column */
    @FXML
    private TableColumn<Customer, String> phone;

    /** Customer country column */
    @FXML
    private TableColumn<Customer, String> country;

    /** Customer state column */
    @FXML
    private TableColumn<Customer, String> state;

    /** Table of appointments */
    @FXML
    private TableView<Appointment> appointmentTableView;

    /** Appointment ID column */
    @FXML
    private TableColumn<Appointment, Integer> appointmentId;

    /** Appointment title column */
    @FXML
    private TableColumn<Appointment, String> title;

    /** Appointment description column */
    @FXML
    private TableColumn<Appointment, String> description;

    /** Appointment location column */
    @FXML
    private TableColumn<Appointment, String> location;

    /** Appointment type column */
    @FXML
    private TableColumn<Appointment, String> type;

    /** Appointment contact column */
    @FXML
    private TableColumn<Appointment, Integer> contact;

    /** Appointment start time column */
    @FXML
    private TableColumn<Appointment, LocalTime> startTime;

    /** Appointment end time column */
    @FXML
    private TableColumn<Appointment, LocalTime> endTime;

    /** Appointment start date column */
    @FXML
    private TableColumn<Appointment, LocalDate> startDate;

    /** Appointment end date column */
    @FXML
    private TableColumn<Appointment, LocalDate> endDate;

    /** Appointment user ID column */
    @FXML
    private TableColumn<Appointment, Integer> userId;

    /** Appointment customer ID column */
    @FXML
    private TableColumn<Appointment, Integer> apptCustomerId;

    /** Weekly view radio button */
    @FXML
    private RadioButton viewByWeekRadioButton;

    /** Monthly view radio button */
    @FXML
    private RadioButton viewByMonthRadioButton;

    /** All view radio button */
    @FXML
    private RadioButton viewAllRadioButton;

    /** Appointment search date picker */
    @FXML
    private DatePicker appointmentSearchDatePicker;

    /** Customer search field */
    @FXML
    private TextField customerSearchField;

    //CUSTOMER SCREEN----------------------------------------------------------

    /**
     * Handles the action of the 'add customer' button being clicked.
     * It navigates to the 'AddCustomer' view.
     *
     * @param actionEvent the ActionEvent object
     * @throws IOException if there is an error loading
     */
    @FXML
    public void addCustomer(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/AddCustomer.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Handles the action of the 'update customer' button being clicked.
     * It navigates to the 'UpdateCustomer' view for the selected customer.
     *
     * @param actionEvent the ActionEvent object
     * @throws IOException if there is an error loading
     */
    @FXML
    public void updateCustomer(ActionEvent actionEvent) throws IOException {
        if(customerTableView.getSelectionModel().isEmpty()) {
            AlertMessage.showAlert(5);
        } else {
            Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                System.out.println("Selected customer is null.");
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/project/view/UpdateCustomer.fxml"));
            loader.load();

            UpdateCustomerController UCC = loader.getController();
            UCC.setCustomerToUpdate(selectedCustomer);

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Handles the action of the 'delete customer' button being clicked.
     * It deletes the selected customer from the database after confirmation.
     *
     * The method first checks if there is a selected customer in the customerTableView.
     * If no customer is selected, an alert message is shown.
     * If a customer is selected, the method retrieves all appointments from the database
     * and filters the appointments associated with the selected customer.
     *
     * After this, a confirmation alert is displayed asking the user whether they really want to delete
     * the selected customer and all associated appointments. If the user confirms the deletion,
     * the method attempts to delete all appointments associated with the customer
     * and then the customer itself from the database. If the deletion is successful,
     * an information alert is displayed to inform the user of the successful deletion.
     *
     * LAMBDA EXPRESSION: The lambda expression used in this method is
     * (appointment -> appointment.getCustomerId() == selectedCustomer.getCustomerId()).
     * This is a predicate function that takes an Appointment object as input and returns true if the customerId
     * of the Appointment object matches the customerId of the selected customer. It is used to filter
     * the stream of all appointments to get the appointments associated with the selected customer.
     *
     * @param actionEvent the ActionEvent object
     */
    @FXML
    public void deleteCustomer(ActionEvent actionEvent) {
        if(customerTableView.getSelectionModel().isEmpty()) {
            AlertMessage.showAlert(5);
        } else {
            Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
            // Check if the selected customer has any associated appointments
            ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();
            ObservableList<Appointment> associatedAppointments = FXCollections.observableArrayList(
                    allAppointments.stream()
                            .filter(appointment -> appointment.getCustomerId() == selectedCustomer.getCustomerId())
                            .collect(Collectors.toList())
            );

            int customerId = selectedCustomer.getCustomerId();
            String customerName = selectedCustomer.getCustomerName();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete customer?");
            alert.setContentText("Do you really want to delete the customer with ID: " + customerId + " and NAME: " + customerName + " and all associated appointments from the database?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Delete all appointments for the customer
                    for (Appointment appointment : associatedAppointments) {
                        AppointmentDAO.deleteAppointmentFromDB(appointment.getAppointmentId());
                    }
                    // Now delete the customer
                    if (CustomerDAO.deleteCustomerFromDB(customerId)) {
                        System.out.println("Deletion successful!");
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Customer and associated appointments deleted!");
                        alert.setContentText("The customer with ID: " + customerId + " and NAME: " + customerName + " and all associated appointments have been deleted.");
                        alert.showAndWait();
                        appointmentTableView.setItems(AppointmentDAO.getAllAppointments());
                    } else {
                        System.out.println("Something mysterious has happened and the customer wasn't deleted!");
                    }
                    customerTableView.setItems(CustomerDAO.getAllCustomers());
                } catch (SQLException e) {
                    System.out.println("There was an error deleting the customer and/or the associated appointments.");
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * Searches for a customer by their name.
     *
     * This method retrieves all customers from the database and filters them based on the provided search text.
     * The filtering is case-insensitive and checks if the search text is contained within the customer's name.
     * If the search text is empty, all customers are displayed.
     * Otherwise, only those customers whose names contain the search text are displayed.
     *
     * LAMBDA EXPRESSION: The expression used in this method is
     * "(customer -> customer.getCustomerName().toLowerCase().contains(searchText.toLowerCase()))".
     * This is a predicate function that takes a Customer object as input and checks if the customer's name,
     * converted to lower case, contains the search text, also converted to lower case.
     * It returns true if the customer's name contains the search text, and false otherwise.
     */
    @FXML
    public void searchCustomer() {
        String searchText = customerSearchField.getText();

        // If search text is empty, show all customers
        if (searchText == null || searchText.isEmpty()) {
            customerTableView.setItems(CustomerDAO.getAllCustomers());
            return;
        }

        // Get all customers
        ObservableList<Customer> allCustomers = CustomerDAO.getAllCustomers();

        // Filter customers based on search text
        ObservableList<Customer> filteredCustomers = allCustomers.stream()
                .filter(customer -> customer.getCustomerName().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // Update the TableView to show only the filtered customers
        customerTableView.setItems(filteredCustomers);
    }

    //APPOINTMENT SCREEN-----------------------------------------------------

    /**
     * Handles the action of the 'add appointment' button being clicked.
     * It navigates to the 'AddAppointment' view.
     *
     * @param actionEvent the ActionEvent object
     * @throws IOException if there is an error loading
     */
    @FXML
    public void addAppointment(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/AddAppointment.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Handles the action of the 'update appointment' button being clicked.
     * It navigates to the 'UpdateAppointment' view for the selected appointment.
     * Uses setAppointmentToUpdate() in UpdateAppointmentController to set the appointment to be edited.
     *
     * @param actionEvent the ActionEvent object
     * @throws IOException if there is an error loading
     */
    @FXML
    public void updateAppointment(ActionEvent actionEvent) throws IOException {
        if(appointmentTableView.getSelectionModel().isEmpty()) {
            AlertMessage.showAlert(4);
        }
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            System.out.println("Selected appointment null.");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/project/view/UpdateAppointment.fxml"));
        loader.load();
        UpdateAppointmentController UAC = loader.getController();
        UAC.setAppointmentToUpdate(selectedAppointment);
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Handles the action of the 'delete appointment' button being clicked.
     * It deletes the selected appointment from the database after confirmation.
     * Shows custom message before and after deletion.
     *
     * @param actionEvent the ActionEvent object
     * @throws SQLException if there is an error accessing the database
     */
    @FXML
    public void deleteAppointment(ActionEvent actionEvent) throws SQLException {
        if(appointmentTableView.getSelectionModel().isEmpty()) {
            AlertMessage.showAlert(4);
        }
        else {
            int appointmentID = appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId();
            String appointmentType = appointmentTableView.getSelectionModel().getSelectedItem().getType();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete appointment?");
            alert.setContentText("Do you really want to delete the appointment with ID: " + appointmentID +  " and TYPE: " + appointmentType + " from the database?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                int selectedAppointmentId = appointmentTableView.getSelectionModel().getSelectedItem().getAppointmentId();
                try {
                if (AppointmentDAO.deleteAppointmentFromDB(selectedAppointmentId)) {
                    System.out.println("Deletion successful!");
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment deleted!");
                    alert.setContentText("The appointment with ID: " + appointmentID +  " and TYPE: " + appointmentType + " has been deleted.");
                    alert.showAndWait();
                } else {
                    System.out.println("Something mysterious has happened and the appointment wasn't deleted!");
                }
                appointmentTableView.setItems(AppointmentDAO.getAllAppointments());
                } catch (SQLException e) {
                    System.out.println("There was an error deleting the appointment.");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Handles the action of the 'view all' button being clicked.
     * It updates the appointments TableView to show all appointments.
     *
     * @param actionEvent the ActionEvent object
     */
    @FXML
    public void viewAll(ActionEvent actionEvent) {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        appointmentTableView.setItems(appointments);
    }

    /**
     * Handles the action of the 'view this month' button being clicked.
     * It updates the appointments TableView to show only the appointments for the current month.
     *
     * @param actionEvent the ActionEvent object
     */
    @FXML
    public void viewThisMonth(ActionEvent actionEvent) {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointmentsThisMonth();
        appointmentTableView.setItems(appointments);
    }

    /**
     * Handles the action of the 'view this week' button being clicked.
     * It updates the appointments TableView to show only the appointments for the current week.
     *
     * @param actionEvent the ActionEvent object
     */
    @FXML
    public void viewThisWeek(ActionEvent actionEvent) {
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointmentsThisWeek();
        appointmentTableView.setItems(appointments);
    }

    /**
     * Searches for appointments by a certain date,
     * First, it gets the date picked from the DatePicker, retrieves a list of all the appointments,
     * and then goes through this list and picks out only the appointments that are on the date selected.
     * LAMBDA EXPRESSION: The expression is "appointment -> selectedDate.equals(appointment.getStartDateTime().toLocalDate())"
     *  and it takes an appointment and checks if its date is the same as
     * the date picked. The filter method uses this function to go through each appointment
     * in the list, and it only keeps the ones where the function is true.
     *
     * Finally, the method updates the TableView to show only the appointments that passed the filter.
     *
     * @param actionEvent the event that triggered this method
     */
    @FXML
    public void searchByDate(ActionEvent actionEvent) {
        // Get the selected date from the DatePicker
        LocalDate selectedDate = appointmentSearchDatePicker.getValue();
        if (selectedDate == null) {
            System.out.println("No date selected!");
            return;
        }

        // Get all appointments
        ObservableList<Appointment> allAppointments = AppointmentDAO.getAllAppointments();

        // Filter appointments by the selected date
        ObservableList<Appointment> filteredAppointments = allAppointments.stream()
                .filter(appointment -> selectedDate.equals(appointment.getStartDateTime().toLocalDate()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        // Update the TableView to show only the filtered appointments
        appointmentTableView.setItems(filteredAppointments);
    }

    //FLOATING TWO BUTTONS--------------------------------------------------
    /**
     * Handles the action of the 'view reports' button being clicked.
     * It navigates to the 'Reports' view.
     *
     * @param actionEvent the ActionEvent object
     * @throws IOException if there is an error loading
     */
    @FXML
    public void viewReports(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/Reports.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Handles the action of the 'logout' button being clicked.
     * It displays a confirmation dialog, and if the user confirms, it logs out the user and exits the application.
     *
     * @param actionEvent the ActionEvent object representing the button click event
     */
    @FXML
    public void logout(ActionEvent actionEvent) {
        Optional<ButtonType> result = AlertMessage.showAlert(1);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Logging out.");
            System.exit(0);
        } else {
            System.out.println("Back in the pan.");
        }
    }

    /**
     * Sets the value of the 'showAppointmentAlert' flag.
     * If set to true, an alert for the upcoming appointment will be displayed when the view is initialized.
     *
     * @param value the new value of the 'showAppointmentAlert' flag
     */
    public static void setShowAppointmentAlert(boolean value) {
        showAppointmentAlert = value;
    }

    /**
     * Initializes the controller after the FXML file has been loaded.
     * It sets up the cell value factories for the appointments and customers tables,
     * and populates the tables with data.
     * It also checks for an upcoming appointment within the next 15 minutes,
     * and if found, displays an alert.
     *
     * @param url url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initializing AppointmentsAndCustomersController...");
        // Set the radio buttons to a toggle group
        ToggleGroup viewToggleGroup = new ToggleGroup();
        this.viewByWeekRadioButton.setToggleGroup(viewToggleGroup);
        this.viewByMonthRadioButton.setToggleGroup(viewToggleGroup);
        this.viewAllRadioButton.setToggleGroup(viewToggleGroup);
        this.viewAllRadioButton.setSelected(true); // Default view is 'View All'

        // Set up the cell value factories for the appointments table
        this.appointmentId.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        this.title.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.description.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.location.setCellValueFactory(new PropertyValueFactory<>("location"));
        this.type.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.contact.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        this.startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        this.endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        this.startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        this.endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        this.userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        this.apptCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        // Set up the columns in the customer table
        customerId.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
        name.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        postalCode.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        phone.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        country.setCellValueFactory(new PropertyValueFactory<Customer, String>("countryName"));
        state.setCellValueFactory(new PropertyValueFactory<Customer, String>("divisionName"));

        // Populate the appointment table with data from the database
        ObservableList<Appointment> appointments = AppointmentDAO.getAllAppointments();
        appointmentTableView.setItems(appointments);
        ObservableList<Customer> customers = CustomerDAO.getAllCustomers();
        customerTableView.setItems(customers);

        // Check for upcoming appointment
        Appointment upcomingAppointment = null;
        try {
            upcomingAppointment = AppointmentDAO.getUpcomingAppointment(LoginController.getCurrentUser().getUserID());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        // Check if the appointment falls within the next 15 minutes and business hours
        if (upcomingAppointment != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime in15Minutes = now.plusMinutes(15);

            if (now.isBefore(upcomingAppointment.getStartDateTime()) && in15Minutes.isAfter(upcomingAppointment.getStartDateTime())
                    && TimeUtil.isWithinBusinessHours(upcomingAppointment.getStartDateTime())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upcoming Appointment");
                alert.setHeaderText("You have an appointment starting soon within 15 minutes!");
                alert.setContentText("Appointment ID: " + upcomingAppointment.getAppointmentId()
                        + "\nDate: " + upcomingAppointment.getStartDate()
                        + "\nTime: " + upcomingAppointment.getStartTime());
                alert.showAndWait();
            }
        } else {
            if (showAppointmentAlert) {
                AlertMessage.showAlert(8);
                showAppointmentAlert = false;
            }
        }



    }

}