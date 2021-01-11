/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.client.UserRESTClient;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import crudclient.interfaces.User;
import crudclient.util.validation.GenericValidations;
import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Mikel
 */
public class UserManagementController {

    private Stage stage;
    private static final Logger logger = Logger.getLogger("signupsignin.controllers.SignUpController");
    private GenericValidations genericValidations = new GenericValidations();
    private User userImplementation;

    @FXML
    private TextField txt_name;
    @FXML
    private TextField txt_surname;
    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_company;
    @FXML
    private ChoiceBox chb_status;
    @FXML
    private ChoiceBox chb_privilege;

    public void initStage(Parent parent) {

        // Sets the hint texts to the inputs.
//        hint_Firstname.setText(MIN_THREE_CHARACTERS);
//        hint_Lastname.setText(MIN_THREE_CHARACTERS);
//        hint_Email.setText(ENTER_VALID_EMAIL);
//        hint_Username.setText(MIN_THREE_CHARACTERS);
//        hint_Password.setText(PASSWORD_CONDITIONS);
//        hint_RepeatPassword.setText("");
//
//        // Adds tooltips
//        btn_SignUp.setTooltip(new Tooltip("Click to sign up"));
//
//        // Sets custom properties to the inputs. These properties show the status of the validation of them.
//        txt_Email.getProperties().put("emailValidator", false);
//        txt_Firstname.getProperties().put("minLengthValidator", false);
//        txt_Lastname.getProperties().put("minLengthValidator", false);
//        txt_Password.getProperties().put("passwordRequirements", false);
//        txt_Username.getProperties().put("minLengthValidator", false);
//        txt_RepeatPassword.getProperties().put("passwordRequirements", false);
//        txt_RepeatPassword.getProperties().put("passwordsMatch", false);
        logger.log(Level.INFO, "Preparing window.");
        // Creates a scena and a stage and opens the window.
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        logger.log(Level.INFO, "Setting listeners for the components of the window.");
        this.setListeners();
        this.setStage(stage);
        stage.setScene(scene);
        //scene.getStylesheets().add(getClass().getResource("/crudclient/view/errorStyle.css").toExternalForm()); // Imports the CSS file used for errors in some inputs.
        stage.setTitle("User Management"); // Sets the title of the window
        stage.setResizable(false); // Prevents the user to resize the window.
        //stage.onCloseRequestProperty().set(this::handleCloseRequest);
        stage.show();
    }

    public void setListeners() {
        this.txt_name.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.minLength(this.txt_name, 3, newText, "minLengthValidator"); // Adds a min lenght validator
            this.genericValidations.textLimiter(this.txt_name, 20, newText); // Limits the input to 20 characters
            this.validate(); // Executes the validation.
        });
    }

    public void validate() {

    }

    public Stage getStage() {
        return this.stage;
    }

    public void setUserImplementation(User user) {
        this.userImplementation = user;
    }

    public User getUserImplementation() {
        return this.userImplementation;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
