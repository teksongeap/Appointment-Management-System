package project.utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Final class representing different varieties of an alert message.
 *
 * @author Teksong Eap
 */
public final class AlertMessage {

    /**
     * private constructor for AlertMessage.
     */
    private AlertMessage() {
        // private constructor to prevent instantiation
    }

    /**
     * Simple alert message method governed by switch statement.
     *
     * @param caseNumber type of alert
     * @return result of button press
     */
    public static Optional<ButtonType> showAlert(int caseNumber) {
        Alert alert;
        switch(caseNumber) {
            case 1:
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setHeaderText("Logout?");
                alert.setContentText("Do you really want to logout?");
                break;
            case 2:
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Delete appointment?");
                alert.setContentText("The selected appointment will be deleted. Continue?");
                break;
            case 3:
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Delete customer?");
                alert.setContentText("The selected customer and their corresponding appointments will be deleted. Continue?");
                break;
            case 4:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("No appointment selected!");
                alert.setContentText("Please select an appointment first to affect any change!");
                break;
            case 5:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("No customer selected!");
                alert.setContentText("Please select a customer first to affect any change!");
                break;
            case 6:
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("CONFIRMATION");
                alert.setHeaderText("Cancel?");
                alert.setContentText("Are you sure you want to cancel?");
                break;
            case 7:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Foreign key constraint!");
                alert.setContentText("This customer has associated appointments. Please delete these appointments before deleting the customer.");
                break;
            case 8:
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText("No Upcoming Appointments");
                alert.setContentText("You have no appointments starting within the next 15 minutes.");
                alert.showAndWait();
                break;
            case 9:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("All fields must be filled!");
                alert.setContentText("Please check to see if there is any missing information.");
                break;
            case 10:
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText("Success!");
                alert.setContentText("Customer successfully added!");
                break;
            case 11:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Failed to add appointment!");
                alert.setContentText("Check your parameters or debug.");
                break;
            case 12:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Not within business hours!");
                alert.setContentText("Please change the time.");
                break;
            case 13:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Incorrect time format!");
                alert.setContentText("Please change the time.");
                break;
            case 14:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Overlapping appointments for selected customer!");
                alert.setContentText("Please change the time.");
                break;
            case 15:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Start date and end date are not chronologically plausible!");
                alert.setContentText("Please change the date.");
                break;
            case 16:
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Start time and end time are not chronologically plausible!");
                alert.setContentText("Please change the time.");
                break;
            case 17:
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText("Success!");
                alert.setContentText("Appointment successfully added!");
                break;
            case 18:
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText("Success!");
                alert.setContentText("Appointment successfully updated!");
                break;
            case 19:
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("INFORMATION");
                alert.setHeaderText("Success!");
                alert.setContentText("Customer successfully updated!");
                break;
            default:
                return Optional.empty();  // In case of an invalid caseNumber
        }

        return alert.showAndWait();
    }
}
