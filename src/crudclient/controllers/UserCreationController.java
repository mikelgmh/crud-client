/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.interfaces.UserInterface;
import crudclient.util.security.AsymmetricEncryption;
import crudclient.util.validation.GenericValidations;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Mikel
 */
public class UserCreationController {

    private Stage stage;
    private static final Logger logger = Logger.getLogger("signupsignin.controllers.SignUpController");
    private UserInterface userImplementation;
    private final GenericValidations genericValidations = new GenericValidations();
    private AsymmetricEncryption enc;

    @FXML
    private TextField txt_firstname;
    @FXML
    private TextField txt_lastname;
    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_company;
    @FXML
    private TextField txt_password;
    @FXML
    private TextField txt_repeatPassword;
    @FXML
    private ChoiceBox chb_status;
    @FXML
    private ChoiceBox chb_privilege;

    // Hint labels
    @FXML
    private Label hint_email;
    @FXML
    private Label hint_firstname;
    @FXML
    private Label hint_lastname;
    @FXML
    private Label hint_username;
    @FXML
    private Label hint_password;
    @FXML
    private Label hint_repeatPassword;

    // Buttons
    @FXML
    private Button btn_create;

    public UserCreationController() {
    }

    public void initStage(Parent parent) {

        // Sets the default hint behavior
        logger.log(Level.INFO, "Loading user creation window...");
        // Creates a scena and a stage and opens the window.
        Scene scene = new Scene(parent);
        Stage stage = new Stage();

        // Set stage
        this.setStage(stage);

        // Set some properties of the stage
        stage.setScene(scene);
        stage.setTitle("User Creation"); // Sets the title of the window
        stage.setResizable(false); // Prevents the user to resize the window.
        scene.getStylesheets().add(getClass().getResource("/crudclient/view/styles/inputStyle.css").toExternalForm()); // Imports the CSS file used for errors in some inputs.

        // Set validators
        txt_email.getProperties().put("emailValidator", false);
        txt_lastname.getProperties().put("minLengthValidator", false);
        txt_firstname.getProperties().put("minLengthValidator", false);
        txt_username.getProperties().put("minLengthValidator", false);
        // Set listeners
        this.setListeners();

        stage.show(); // Show the stage

        // PRUEBAS DE ENCRIPTAR Y RECIBIR LA CLAVE PÚBLICA DEL SERVIDOR
        String publicKeyAsHex = this.userImplementation.getPublicKey();
        enc = new AsymmetricEncryption(publicKeyAsHex);

    }

    public void setListeners() {
        logger.log(Level.INFO, "Setting listeners for the components of the window.");
        this.txt_firstname.textProperty().addListener((obs, oldText, newText) -> {
            Boolean minlengthValidator = this.genericValidations.minLength(this.txt_firstname, 3, newText, "minLengthValidator"); // Adds a min lenght validator
            this.genericValidations.textLimiter(this.txt_firstname, 200, newText); // Limits the input to 200 characters
            setInputError(minlengthValidator, txt_firstname, hint_firstname);
            this.validate(); // Executes the validation.
        });

        this.txt_lastname.textProperty().addListener((obs, oldText, newText) -> {
            Boolean minlengthValidator = this.genericValidations.minLength(this.txt_lastname, 3, newText, "minLengthValidator"); // Adds a min lenght validator
            setInputError(minlengthValidator, txt_lastname, hint_lastname);
            this.genericValidations.textLimiter(this.txt_lastname, 200, newText); // Limits the input to 200 characters
            this.validate(); // Executes the validation.
        });

        this.txt_username.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_username, 200, newText); // Limits the input to 200 characters
            Boolean minlengthValidator = this.genericValidations.minLength(this.txt_username, 3, newText, "minLengthValidator"); // Adds a min lenght validator

            setInputError(minlengthValidator, txt_username, hint_username);
            this.validate(); // Executes the validation.
        });

        // Validation for the Email field
        this.txt_email.textProperty().addListener((obs, oldText, newText) -> {
            //this.genericValidations.minLength(this.txt_email, 3, newText, "minLengthValidator");
            this.genericValidations.textLimiter(this.txt_email, 254, newText);
            boolean emailValidator = this.genericValidations.regexValidator(this.genericValidations.EMAIL_REGEXP, this.txt_email, newText.toLowerCase(), "emailValidator"); // Adds a regex validation to check if the email is correct
            this.hint_email.setText(this.genericValidations.TXT_ENTER_VALID_EMAIL);
            if (!emailValidator) {

                this.genericValidations.addClass(this.txt_email, "error", Boolean.TRUE);
            } else {
                this.hint_email.setTextFill(this.genericValidations.greyColor);
                this.genericValidations.addClass(this.txt_email, "error", Boolean.FALSE);
            }
            this.validate();
        });
    }

    public void setInputError(boolean validatorStatus, TextField tf, Label hint) {
        if (!validatorStatus) {

            this.genericValidations.addClass(tf, "error", Boolean.TRUE);
        } else {
            hint.setTextFill(this.genericValidations.greyColor);
            this.genericValidations.addClass(tf, "error", Boolean.FALSE);
        }
    }

    public void validate() {

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public UserInterface getUserImplementation() {
        return userImplementation;
    }

    public void setUserImplementation(UserInterface userImplementation) {
        this.userImplementation = userImplementation;
    }

}
