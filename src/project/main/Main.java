package project.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import project.utilities.JDBC;

import java.util.Objects;

/**
 * The Main class is the entry point of the application.
 * It extends the Application class from JavaFX.
 * @author Teksong Eap
 */
public class Main extends Application {

    /**
     * The start method, which is the main entry point for all JavaFX applications.
     *
     * @param primaryStage the primary stage
     * @throws Exception if anything goes wrong during loading the FXML file
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/project/view/Login.fxml")));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 400, 375));
        primaryStage.show();
    }

    /**
     * The main method is used to launch the JavaFX application.
     * It opens a connection to the database, launches the application and then
     * closes the database connection once the application has terminated.
     *
     * @param args command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        //Locale.setDefault(new Locale("fr"));
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
