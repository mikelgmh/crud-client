package crudclient.controllers;

// Library
/*import exceptions.ErrorClosingDatabaseResources;
import exceptions.ErrorConnectingDatabaseException;
import exceptions.ErrorConnectingServerException;
import exceptions.PasswordMissmatchException;
import exceptions.QueryException;
import exceptions.UserNotFoundException;
import interfaces.Signable;
import user.User;*/
// Java
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
//import signupsignin.util.ValidationUtils;

// JavaFX
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.application.Platform;
import javafx.scene.control.Hyperlink;
import javafx.stage.Modality;

/**
 * This class handles the interaction of the user with the graphic user
 * interface, on window sign in.
 *
 * @author Iker, Aketza
 */
public class SignInController {

    private static final Logger logger = Logger.getLogger("crudclient.controllers.SignInController");
    //private ValidationUtils validationUtils = new ValidationUtils();
    private Stage stage;
    //private Signable signableImplementation;
    @FXML
    private Button btn_SignIn;
    @FXML
    private TextField txt_User;
    @FXML
    private PasswordField txt_Password;
    @FXML
    private Hyperlink hlRecoverPassword;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public SignInController() {
    }

    /*public void setSignable(Signable signable) {
        this.signableImplementation = signable;
    }*/
    /**
     * Method to initialize the SignIn stage.
     *
     * @param root The base object for all nodes that have children in the scene
     * graph.
     */
    public void initStage(Parent root) {
        logger.log(Level.INFO, "Loading the SignIn stage.");
        Scene scene = new Scene(root);
        //this.setListeners();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        stage.show();
        logger.log(Level.INFO, "SignIn stage loaded.");
    }

    /**
     * Method to initialize the SignIn components.
     *
     * @param event
     */
    private void handleWindowShowing(WindowEvent event) {
        btn_SignIn.setDisable(false);
        txt_User.setPromptText("Insert username");
        txt_Password.setPromptText("Insert password");
        btn_SignIn.setDefaultButton(true);
        btn_SignIn.setTooltip(new Tooltip("Send identification values"));
    }

    /**
     * Validate that the user and password fields have certain characters
     */
    /*public void setListeners() {
        this.txt_User.textProperty().addListener((obs, oldText, newText) -> {
            this.validationUtils.minLength(this.txt_User, 3, newText, "minLengthValidator");
            this.validationUtils.textLimiter(this.txt_User, 20, newText);
            this.validate();
        });
        this.txt_Password.textProperty().addListener((obs, oldText, newText) -> {
            this.validationUtils.textLimiter(this.txt_Password, 25, newText);
            this.validate();
        });
    }*/
    /**
     * Validates if the user and password field are writed
     */
    /*public void validate() {
        if (Boolean.parseBoolean(this.txt_User.getProperties().get("minLengthValidator").toString())
                && !txt_Password.getText().trim().equalsIgnoreCase("")) {
            this.btn_SignIn.setDisable(false);
        } else {
            this.btn_SignIn.setDisable(true);
        }
    }*/
    /**
     * Events related to the login button. This method is referenced in
     * SceneBuilder.
     *
     * @param event
     *
     * The following exceptions show an alert according to the message received
     *
     * @throws IOException input/output error
     * @throws exceptions.ErrorConnectingDatabaseException cannot connect to
     * database
     * @throws exceptions.PasswordMissmatchException password is not correct
     * @throws exceptions.ErrorClosingDatabaseResources cannot close the
     * database
     * @throws exceptions.QueryException the query is not correct
     * @throws exceptions.ErrorConnectingServerException cannot connect to the
     * server side
     */
    @FXML
    private void handleOnClickLogin(ActionEvent event) throws IOException {
        logger.log(Level.INFO, "Attempting to sign in.");
        // TODO: Login with encrypted method.

        // Guardamos la información de user y password dentro de la clase User
        /*User user = new User();
        user.setLogin(txt_User.getText());
        user.setPassword(txt_Password.getText());

        try {
            // Enviamos los datos al SignableImplementation para hacer la comprobación con la BD.
            user = this.signableImplementation.signIn(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signupsignin/view/Dashboard.fxml"));
            Parent root = (Parent) loader.load();
            DashboardController controllerDashboard = ((DashboardController) loader.getController());
            controllerDashboard.setSignableImplementation(this.signableImplementation);
            controllerDashboard.setUser(user);
            controllerDashboard.initStage(root);
            // Por último, cerramos la ventana de Login
            stage.close();
            logger.log(Level.INFO, "Signed In successfully");
        } catch (ErrorConnectingDatabaseException ex) {
            logger.log(Level.SEVERE, "Error connecting to the database.");
            Alert alertConnectingToDatabase = new Alert(Alert.AlertType.ERROR);
            alertConnectingToDatabase.setTitle("Error with the server.");
            alertConnectingToDatabase.setHeaderText("Error connecting to the database.");
            alertConnectingToDatabase.setContentText("An error ocurred trying to connect to the database. Contact the Administrator.");
            alertConnectingToDatabase.showAndWait();
        } catch (UserNotFoundException ex) {
            logger.log(Level.SEVERE, "User not found.");
            Alert alertUserNotFound = new Alert(Alert.AlertType.WARNING);
            alertUserNotFound.setTitle("User not found.");
            alertUserNotFound.setHeaderText("User not found.");
            alertUserNotFound.setContentText("The user entered does not exist in the database.");
            alertUserNotFound.showAndWait();
        } catch (PasswordMissmatchException ex) {
            logger.log(Level.SEVERE, "The password inserted does not match with the current user.");
            Alert alertPasswordNoMatch = new Alert(Alert.AlertType.WARNING);
            alertPasswordNoMatch.setTitle("The password does not match.");
            alertPasswordNoMatch.setHeaderText("Incorrect password.");
            alertPasswordNoMatch.setContentText("The password inserted does not match with the current user. Enter a valid password.");
            alertPasswordNoMatch.showAndWait();
        } catch (ErrorClosingDatabaseResources ex) {
            logger.log(Level.SEVERE, "Error closing the database resources.");
            Alert alertClosingDatabase = new Alert(Alert.AlertType.ERROR);
            alertClosingDatabase.setTitle("Unexpected error.");
            alertClosingDatabase.setHeaderText("Unexpected error ocurred.");
            alertClosingDatabase.setContentText("An unexpected error ocurred with the database. Contact the server Administrator.");
            alertClosingDatabase.showAndWait();
        } catch (QueryException ex) {
            logger.log(Level.SEVERE, "Error doing a query in the database.");
            Alert alertConnectingToDatabase = new Alert(Alert.AlertType.ERROR);
            alertConnectingToDatabase.setTitle("Unexpected error");
            alertConnectingToDatabase.setHeaderText("Unexpected error ocurred.");
            alertConnectingToDatabase.setContentText("An unexpected error ocurred with the database. Contact the server Administrator.");
            alertConnectingToDatabase.showAndWait();
        } catch (ErrorConnectingServerException ex) {
            logger.log(Level.SEVERE, "Error connecting to the server.");
            Alert alertConnectingToDatabase = new Alert(Alert.AlertType.ERROR);
            alertConnectingToDatabase.setTitle("Error with the server.");
            alertConnectingToDatabase.setHeaderText("Error connecting to the server.");
            alertConnectingToDatabase.setContentText("Can not connect to the server, try to restart the application or contact the server Administrator.");
            alertConnectingToDatabase.showAndWait();
        }*/
    }

    /**
     * Handler to the close button of the window.
     *
     * @param event
     */
    private void handleCloseRequest(WindowEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("Application will be closed");
        alert.setContentText("You will close the application");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            stage.close();
            Platform.exit();
        } else {
            event.consume();
            alert.close();
        }
    }

    /**
     * Handler to the recover password hyperlink.
     *
     * @param event
     */
    @FXML
    private void handleOnClickRecoverPassword(ActionEvent event) throws Exception {
        // Create the stage for RecoverPassword.
        Stage stageRecoverPassword = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/RecoverPassword.fxml"));
        Parent root = (Parent) loader.load();
        RecoverPasswordController controller = ((RecoverPasswordController) loader.getController());
        // Load the stage in Modal mode.
        stageRecoverPassword.initModality(Modality.APPLICATION_MODAL);
        controller.setStage(stageRecoverPassword);
        controller.initStage(root);
        stageRecoverPassword.showAndWait();
    }

}
