package crudclient.controllers;

// Java
import crudclient.factories.EmailServiceFactory;
import crudclient.interfaces.EmailServiceInterface;
import crudclient.interfaces.SignInInterface;
import crudclient.model.User;
import crudclient.util.security.AsymmetricEncryption;
import crudclient.util.session.UserSession;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private Stage stage;
    private SignInInterface signInImplementation;
    private AsymmetricEncryption ae;
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
        ae = new AsymmetricEncryption(getSignInImplementation().getPublicKey());
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
     * Events related to the login button. This method is referenced in
     * SceneBuilder.
     *
     * @param event
     * @throws Exception input/output error.
     */
    @FXML
    private void handleOnClickLogin(ActionEvent event) throws Exception {
        logger.log(Level.INFO, "Attempting to sign in.");
        try {
            User user = new User();
            user.setUsername(txt_User.getText());
            user.setPassword(ae.encryptString(txt_Password.getText()));
            User loggedUser = getSignInImplementation().loginUser_XML(user, User.class);
            
            // Create the stage for Dashboard
            Stage stageDashboard = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/Dashboard.fxml"));
            Parent root = (Parent) loader.load();
            DashboardController controller = ((DashboardController) loader.getController());
            // Load the stage
            controller.setUser(loggedUser);
            controller.setStage(stageDashboard);
            controller.initStage(root);
            // Close the current stage
            stage.close();
        } catch (Exception ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid login.");
            alert.setHeaderText("Wrong username or passwords.");
            alert.showAndWait();
        }

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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/EmailService.fxml"));
        Parent root = (Parent) loader.load();
        EmailServiceController controller = ((EmailServiceController) loader.getController());
        // Load the stage in Modal mode.
        EmailServiceFactory emailServiceFactory = new EmailServiceFactory();
        EmailServiceInterface emailServiceImplementation = emailServiceFactory.getImplementation();
        stageRecoverPassword.initModality(Modality.APPLICATION_MODAL);
        controller.setImplementation(emailServiceImplementation);
        controller.setStage(stageRecoverPassword);
        controller.initStage(root);
        stageRecoverPassword.showAndWait();
    }

    /**
     * Method to return the Company implementation.
     *
     * @return The Company implementation.
     */
    public SignInInterface getSignInImplementation() {
        return signInImplementation;
    }

    /**
     * Method to set the Company implementation getting the Company interface.
     *
     * @param companyInterface
     */
    public void setImplementation(SignInInterface signInInterface) {
        this.signInImplementation = signInInterface;
    }

}
