package project.controller;

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
import project.DAO.UserAndContactDAO;
import project.DAO.CustomerDAO;
import project.DAO.TerritoryDAO;
import project.model.*;
import project.utilities.AlertMessage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for reports of 3 types: first type shows appointments by contact, second type
 * shows customers by country, third type shows number of month and type of appointments.
 *
 * @author Teksong Eap
 */
public class ReportsController implements Initializable {

    /** Tab pane */
    @FXML
    private TabPane tabPane;

    /** Contact report tab */
    @FXML
    private Tab contactReportTab;

    /** Contact combo box */
    @FXML
    private ComboBox<Contact> contactComboBox;

    /** Contact table view */
    @FXML
    private TableView<Appointment> contactTableView;

    /** Appointment ID column */
    @FXML
    private TableColumn<Appointment, Integer> apptIDColumn;

    /** Title column */
    @FXML
    private TableColumn<Appointment, String> titleColumn;

    /** Description column */
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;

    /** Location column */
    @FXML
    private TableColumn<Appointment, String> locationColumn;

    /** Type column */
    @FXML
    private TableColumn<Appointment, String> typeColumn;

    /** Start date column */
    @FXML
    private TableColumn<Appointment, LocalDate> startDateColumn;

    /** End date column */
    @FXML
    private TableColumn<Appointment, LocalDate> endDateColumn;

    /** Start time column */
    @FXML
    private TableColumn<Appointment, LocalTime> startTimeColumn;

    /** End time column */
    @FXML
    private TableColumn<Appointment, LocalTime> endTimeColumn;

    /** Customer ID column */
    @FXML
    private TableColumn<Appointment, Integer> customerIDColumn;

    /** Total appointments label */
    @FXML
    private Label totalAppointmentsLabel;

    /** Country report tab */
    @FXML
    private Tab countryReportTab;

    /** Country combo box */
    @FXML
    private ComboBox<Country> countryComboBox;

    /** Customer table view */
    @FXML
    private TableView<Customer> customerTableView;

    /** Customer ID column */
    @FXML
    private TableColumn<Customer, Integer> customerIDCountryReport;

    /** Customer name column */
    @FXML
    private TableColumn<Customer, String> customerNameCountryReport;

    /** Address column */
    @FXML
    private TableColumn<Customer, String> addressCountryReport;

    /** Postal code column */
    @FXML
    private TableColumn<Customer, String> postalCodeCountryReport;

    /** Phone column */
    @FXML
    private TableColumn<Customer, String> phoneCountryReport;

    /** State column */
    @FXML
    private TableColumn<Customer, String> stateCountryReport;

    /** Total customers label */
    @FXML
    private Label totalCustomersLabel;

    /** Month report tab */
    @FXML
    private Tab monthReportTab;

    /** Month table view */
    @FXML
    private TableView<TypeAndMonthReport> monthTableView;

    /** Type month column */
    @FXML
    private TableColumn<TypeAndMonthReport, String> typeMonthColumn;

    /** Month column */
    @FXML
    private TableColumn<TypeAndMonthReport, String> monthColumn;

    /** Count column */
    @FXML
    private TableColumn<TypeAndMonthReport, Integer> countColumn;

    //first report
    /**
     * Handles the action of a contact being selected in the contactComboBox.
     * It updates the contactTableView to display only appointments associated with the selected contact.
     *
     * @param actionEvent the ActionEvent object
     */
    public void updateTableBasedOnContact(ActionEvent actionEvent) {
        int selectedContactId = contactComboBox.getSelectionModel().getSelectedItem().getContactId();
        if (selectedContactId != 0) {
            ObservableList<Appointment> appointmentsOfSelectedContact =
                    AppointmentDAO.getAppointmentsByContact(selectedContactId);
            contactTableView.setItems(appointmentsOfSelectedContact);
            int totalAppointments = contactTableView.getItems().size();
            totalAppointmentsLabel.setText("Total Appointments: " + totalAppointments);
        }
    }

    //second report
    /**
     * Handles the action of a country being selected in the countryComboBox.
     * It updates the customerTableView to display only customers associated with the selected country.
     */
    @FXML
    public void updateTableBasedOnCountry() {
        int selectedCountryId = countryComboBox.getSelectionModel().getSelectedItem().getCountryId();

        if (selectedCountryId != 0) {
            ObservableList<Customer> customersInSelectedCountry =
                    CustomerDAO.getCustomersByCountry(selectedCountryId);

            customerTableView.setItems(customersInSelectedCountry);
            int totalCustomers = customerTableView.getItems().size();
            totalCustomersLabel.setText("Total Customers: " + totalCustomers);
        }
    }

    /**
     * Handles the action of the 'logout' button being clicked.
     * It displays a confirmation dialog, and if the user confirms,
     * it logs out the user and exits the application.
     *
     * @param actionEvent the ActionEvent object
     */
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
     * Handles the action of the 'back' button being clicked.
     * It navigates back to the 'AppointmentsAndCustomers' view.
     *
     * @param actionEvent the ActionEvent object
     * @throws IOException if there is an error loading
     */
    public void goBack(ActionEvent actionEvent) throws IOException {
        // Define stage and scene objects
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/AppointmentsAndCustomers.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Initializes the controller after the FXML file has been loaded.
     * It requests focus on the 'title' TextField and populates the ComboBoxes with data.
     *
     * @param url url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Country> allCountries = TerritoryDAO.getAllCountries();
            countryComboBox.setItems(allCountries);
            ObservableList<Contact> allContacts = UserAndContactDAO.getAllContacts();
            contactComboBox.setItems(allContacts);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //first report
        apptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        //second report
        customerIDCountryReport.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("customerId"));
        customerNameCountryReport.setCellValueFactory(new PropertyValueFactory<Customer, String>("customerName"));
        addressCountryReport.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        postalCodeCountryReport.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        phoneCountryReport.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        stateCountryReport.setCellValueFactory(new PropertyValueFactory<Customer, String>("divisionName"));

        //third report
        monthColumn.setCellValueFactory(new PropertyValueFactory<TypeAndMonthReport, String>("month"));
        typeMonthColumn.setCellValueFactory(new PropertyValueFactory<TypeAndMonthReport, String>("type"));
        countColumn.setCellValueFactory(new PropertyValueFactory<TypeAndMonthReport, Integer>("count"));
        //populate table
        ObservableList<TypeAndMonthReport> typeAndMonthReports = AppointmentDAO.getAppointmentCountByTypeAndMonth();
        monthTableView.setItems(typeAndMonthReports);
    }

}
