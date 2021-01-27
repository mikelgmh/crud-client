package crudclient.controllers;

import crudclient.client.UserRESTClient;
import crudclient.interfaces.EmailServiceInterface;
import crudclient.model.User;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The controller for the RecoverPassword view.
 *
 * @author Iker de la Cruz
 */
public class EmailServiceController {

    private static final Logger logger = Logger.getLogger("crudclient.controllers.RecoverPasswordController");
    private Stage stage;
    private EmailServiceInterface emailServiceImplementation;
    @FXML
    private TextField tfRecoverPassword;
    @FXML
    private Button btnRecoverPassword;

    public EmailServiceController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Method to initialize the RecoverPassword stage.
     *
     * @param root The loaded view.
     */
    public void initStage(Parent root) {
        logger.log(Level.INFO, "Loading the RecoverPassword stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("RecoverPassword");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        logger.log(Level.INFO, "RecoverPassword stage loaded.");
    }

    /**
     * Method to initialize the RecoverPassword components.
     *
     * @param event
     */
    private void handleWindowShowing(WindowEvent event) {
        tfRecoverPassword.clear();
        btnRecoverPassword.setDisable(false);
        btnRecoverPassword.setDefaultButton(true);
    }

    /**
     * Method to recover the user password sending by email.
     *
     * @param event
     */
    @FXML
    private void sendRecoverPasswordMail(ActionEvent event) {
        // Create an User to send via XML the email to the server
        User user = new User();
        user.setEmail(tfRecoverPassword.getText());
        // Create new Thread to send the email
        EmailServiceThread emailThread = new EmailServiceThread(user);
        emailThread.start();
        stage.close();
    }

    /**
     *
     * @return
     */
    public EmailServiceInterface getEmailServiceImplementation() {
        return emailServiceImplementation;
    }

    /**
     *
     * @param emailServiceInterface
     */
    public void setImplementation(EmailServiceInterface emailServiceInterface) {
        this.emailServiceImplementation = emailServiceInterface;
    }

}
