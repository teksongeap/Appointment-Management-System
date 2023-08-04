package project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import project.DAO.CustomerDAO;
import project.DAO.TerritoryDAO;
import project.model.Country;
import project.model.Customer;
import project.model.Division;
import project.utilities.AlertMessage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for the 'AddCustomer' view.
 * It provides functionalities for adding a new customer to the system.
 *
 * @author Teksong Eap
 */
public class AddCustomerController implements Initializable {

    /** The stage object */
    private Stage stage;

    /** The scene object */
    private Parent scene;

    /** The customer's ID */
    @FXML
    private TextField customerId;

    /** The customer's name */
    @FXML
    private TextField customerName;

    /** The customer's address */
    @FXML
    private TextField address;

    /** The customer's postal code */
    @FXML
    private TextField postalCode;

    /** The customer's phone number */
    @FXML
    private TextField phoneNumber;

    /** The customer's country */
    @FXML
    private ComboBox<Country> country;

    /** The customer's division */
    @FXML
    private ComboBox<Division> division;

    /** The add button */
    @FXML
    private Button addButton;

    /** The cancel button */
    @FXML
    private Button cancelButton;

    /**
     * Handles the action of the 'add customer' button being clicked.
     * It retrieves the inputs from the text fields and combo boxes, validates them, and adds a new customer
     * to the database if the inputs are valid. After adding the customer, it navigates back to the
     * 'AppointmentsAndCustomers' view.
     *
     * @param actionEvent ActionEvent object
     * @throws IOException if there is an error loading
     */
    @FXML
    public void addCustomer(ActionEvent actionEvent) throws IOException {
        boolean customerAdded = false;

        // Get the text from all the TextFields
        String customerNameText = customerName.getText();
        String addressText = address.getText();
        String postalCodeText = postalCode.getText();
        String phoneNumberText = phoneNumber.getText();

        // Check if any of the TextFields are empty
        if (customerNameText == null || customerNameText.isEmpty() ||
                addressText == null || addressText.isEmpty() ||
                postalCodeText == null || postalCodeText.isEmpty() ||
                phoneNumberText == null || phoneNumberText.isEmpty()) {
            // Show an error message to the user
            AlertMessage.showAlert(9);
        } else {
            String countryName = country.getSelectionModel().getSelectedItem().getCountryName();
            String divisionName = division.getSelectionModel().getSelectedItem().getDivisionName();
            int countryId = country.getSelectionModel().getSelectedItem().getCountryId();
            int divisionId = division.getSelectionModel().getSelectedItem().getDivisionId();
            try {
                // Retrieve an unused Customer ID
                int customerId = CustomerDAO.getUnusedCustomerId();

                // Create a new Customer object
                Customer newCustomer = new Customer(customerId, divisionId, countryId, customerNameText,
                        addressText, postalCodeText, phoneNumberText, countryName, divisionName);

                // Add the new customer to the database

                customerAdded = CustomerDAO.addCustomerToDB(newCustomer);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (customerAdded) {
            AlertMessage.showAlert(10);
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/AppointmentsAndCustomers.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    /**
     * Handles the action of a new country being selected from the 'country' combo box.
     * It updates the items in the 'division' combo box based on the selected country.
     *
     * @param actionEvent ActionEvent object
     * @throws SQLException if there is an error accessing the database
     */
    @FXML
    public void updateDivisionComboBox (ActionEvent actionEvent) throws SQLException {
        division.setItems(TerritoryDAO.getAllDivisionsByCountryId(country.getSelectionModel().getSelectedItem().getCountryId()));
        division.getSelectionModel().selectFirst();
    }

    /**
     * Handles the action of the 'cancel' button being clicked.
     * It displays a confirmation dialog, and if the user confirms, it navigates back to the
     * 'AppointmentsAndCustomers' view.
     *
     * @param actionEvent ActionEvent object
     * @throws IOException if there is an error loading the 'AppointmentsAndCustomers' view
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
     * It sets up the items in the 'country' and 'division' combo boxes, and sets the initial focus on the 'customerName' text field.
     *
     * @param url url
     * @param resourceBundle resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerName.requestFocus();
        try {
            country.setItems(TerritoryDAO.getAllCountries());
            country.getSelectionModel().selectFirst();
            division.setItems(TerritoryDAO.getAllDivisionsByCountryId(country.getSelectionModel().getSelectedItem().getCountryId()));
            division.getSelectionModel().selectFirst();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
