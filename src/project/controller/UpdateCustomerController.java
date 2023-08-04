package project.controller;

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
import java.util.stream.Stream;

/**
 * Controller for the UpdateCustomer view. Handles user interactions within the view, such as button clicks and ComboBox selections.
 * Responsible for populating form fields with the data of the customer to be updated,
 * validating user input, updating the customer's data in the database, and navigating back to the main view.
 *
 * @author Teksong Eap
 */
public class UpdateCustomerController implements Initializable {
    /** Stage object */
    private Stage stage;

    /** Scene object */
    private Parent scene;

    /** Customer to update */
    private Customer customerToUpdate;

    /** Customer ID field */
    @FXML
    private TextField customerId;

    /** Customer name field */
    @FXML
    private TextField customerName;

    /** Address field */
    @FXML
    private TextField address;

    /** Postal code field */
    @FXML
    private TextField postalCode;

    /** Phone number field */
    @FXML
    private TextField phoneNumber;

    /** Country combo box */
    @FXML
    private ComboBox<Country> country;

    /** Division combo box */
    @FXML
    private ComboBox<Division> division;

    /** Update button */
    @FXML
    private Button updateButton;

    /** Cancel button */
    @FXML
    private Button cancelButton;


    /**
     * Handles the action of the update button being clicked.
     * Validates the form input and, if valid, updates the customer's data in the database and navigates back to the main view.
     *
     * @param actionEvent the ActionEvent object
     * @throws IOException if there is an error loading the main view
     */
    @FXML
    public void updateCustomer(ActionEvent actionEvent) throws IOException {
        if (isAnyFieldEmpty()) {
            AlertMessage.showAlert(9);
            return;
        }
        try {
            updateCustomerFromFormInputs();
            if (updateCustomerInDBAndNavigateBack(customerToUpdate, actionEvent)) {
                return;
            }
            System.out.println("Failed to update customer!");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            AlertMessage.showAlert(13);
        }
    }

    /**
     * Sets the customer to be updated and populates the form fields with the customer's data.
     * Mostly the same as the one in AddCustomer.
     *
     * @param customer the customer to be updated
     */
    public void setCustomerToUpdate(Customer customer) {
        this.customerToUpdate = customer;
        customerId.setText(Integer.toString(customer.getCustomerId()));
        customerName.setText(customer.getCustomerName());
        address.setText(customer.getAddress());
        postalCode.setText(customer.getPostalCode());
        phoneNumber.setText(customer.getPhone());

        Country countryToSelect = country.getItems().stream()
                .filter(c -> c.getCountryId() == customer.getCountryId())
                .findFirst()
                .orElse(null);
        country.getSelectionModel().select(countryToSelect);

        // Populate the division ComboBox based on the selected country
        try {
            division.setItems(TerritoryDAO.getAllDivisionsByCountryId(country.getSelectionModel().getSelectedItem().getCountryId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Division divisionToSelect = division.getItems().stream()
                .filter(d -> d.getDivisionId() == customer.getDivisionId())
                .findFirst()
                .orElse(null);
        division.getSelectionModel().select(divisionToSelect);
    }

    /**
     * Updates the customerToUpdate object's data with the data currently in the form inputs.
     */
    private void updateCustomerFromFormInputs() {
        String customerNameText = customerName.getText();
        String addressText = address.getText();
        String postalCodeText = postalCode.getText();
        String phoneNumberText = phoneNumber.getText();
        int countryId = country.getSelectionModel().getSelectedItem().getCountryId();
        int divisionId = division.getSelectionModel().getSelectedItem().getDivisionId();

        customerToUpdate.setCustomerName(customerNameText);
        customerToUpdate.setAddress(addressText);
        customerToUpdate.setPostalCode(postalCodeText);
        customerToUpdate.setPhone(phoneNumberText);
        customerToUpdate.setCountryId(countryId);
        customerToUpdate.setDivisionId(divisionId);
    }

    /**
     * Attempts to update the customer's data in the database and, if successful, navigates back to the main view.
     *
     * @param customer the customer whose data should be updated in the database
     * @param event the ActionEvent object
     * @return true if the update was successful and the user was navigated back to the main view; false otherwise
     * @throws SQLException if there is a database access error
     * @throws IOException if there is an error loading the main view
     */
    private boolean updateCustomerInDBAndNavigateBack(Customer customer, ActionEvent event) throws SQLException, IOException {
        if (CustomerDAO.updateCustomerInDB(customer)) {
            AlertMessage.showAlert(19);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/AppointmentsAndCustomers.fxml")));
            stage.setScene(new Scene(scene));
            stage.show();
            return true;
        }
        return false;
    }

    /**
     * Checks if any of the form inputs are empty.
     *
     * @return true if any of the form inputs are empty; false otherwise
     */
    private boolean isAnyFieldEmpty() {
        return Stream.of(customerName.getText(), address.getText(), postalCode.getText(), phoneNumber.getText())
                .anyMatch(text -> text == null || text.isEmpty());
    }

    /**
     * Handles the action of the country ComboBox's selected item changing. Updates the items in the division ComboBox based on the selected country.
     *
     * @param event the ActionEvent object representing the ComboBox selection change event
     * @throws SQLException if there is a database access error
     */
    @FXML
    public void updateDivisionComboBox (ActionEvent event) throws SQLException {
        division.setItems(TerritoryDAO.getAllDivisionsByCountryId(country.getSelectionModel().getSelectedItem().getCountryId()));
        division.getSelectionModel().selectFirst();
    }

    /**
     * Handles the action of the cancel button being clicked. Displays a confirmation dialog, and if the user confirms, navigates back to the main view.
     *
     * @param actionEvent the ActionEvent object
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
     * Initializes the controller and requests focus on the customerName TextField
     * and populates the country ComboBox with all countries.
     *
     * @param url url
     * @param resourceBundle rb
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerName.requestFocus();
        try {
            country.setItems(TerritoryDAO.getAllCountries());
            country.getSelectionModel().selectFirst();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


}
