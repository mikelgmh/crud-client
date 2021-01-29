/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.exceptions.EmailAlreadyExistsException;
import crudclient.exceptions.EmailAndUsernameAlreadyExistException;
import crudclient.exceptions.UsernameAlreadyExistsException;
import crudclient.factories.CompanyFactory;
import crudclient.interfaces.CompanyInterface;
import crudclient.interfaces.UserInterface;
import crudclient.model.Company;
import crudclient.model.User;
import crudclient.model.UserPrivilege;
import crudclient.model.UserStatus;
import crudclient.util.security.AsymmetricEncryption;
import crudclient.util.validation.GenericValidations;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Mikel
 */
public class UserCreationController {

    private static final ResourceBundle rb = ResourceBundle.getBundle("config.config");
    // The needed class properties and variables.
    /**
     * The stage for this window.
     */
    private Stage stage;
    /**
     * the logger for this class
     */
    private static final Logger logger = Logger.getLogger("crudclient");
    /**
     * The user implementation
     */
    private UserInterface userImplementation;
    /**
     * the company implementation
     */
    private CompanyInterface companyImplementation;
    /**
     * The generic validations instance
     */
    private final GenericValidations genericValidations = new GenericValidations();
    /**
     * The list of companies
     */
    private ObservableList companyList;
    /**
     * An instance of the user management controller.
     */
    private UserManagementController userManagementController;

    // Text fields, password fields and choiceboxes.
    @FXML
    private TextField txt_firstname;
    @FXML
    private TextField txt_lastname;
    @FXML
    private TextField txt_username_create;
    @FXML
    private TextField txt_email_create;
    @FXML
    private TextField txt_company;
    @FXML
    private PasswordField txt_password;
    @FXML
    private PasswordField txt_repeatPassword;
    @FXML
    private ChoiceBox chb_status;
    @FXML
    private ChoiceBox chb_privilege;
    @FXML
    private ChoiceBox chb_company;

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
    private Button btn_create_create;
    @FXML
    private Button btn_cancel;

    /**
     * Empty constructor for this class.
     */
    public UserCreationController() {
    }

    /**
     * The method that is run when this window opens.
     *
     * @param parent
     */
    public void initStage(Parent parent) {
        logger.log(Level.INFO, "Loading user creation window...");
        // Creates a scena and a stage and opens the window.
        Scene scene = new Scene(parent); // Sets the scene
        Stage stage = new Stage(); // Creates a new stage

        // Set stage
        this.setStage(stage); // Sets the stage

        // Company implementation
        CompanyFactory companyFactory = new CompanyFactory(); // Creates a company factory
        this.companyImplementation = companyFactory.getImplementation(); // Gets a company implementation

        // Sets the default field values.
        setDefaultFieldValues();

        // Set some properties of the stage
        stage.setScene(scene);
        stage.setTitle("User Creation"); // Sets the title of the window
        stage.setResizable(false); // Prevents the user to resize the window.
        scene.getStylesheets().add(getClass().getResource("/crudclient/view/styles/inputStyle.css").toExternalForm()); // Imports the CSS file used for errors in some inputs.

        // Set validators
        txt_email_create.getProperties().put("emailValidator", false);
        txt_lastname.getProperties().put("minLengthValidator", false);
        txt_firstname.getProperties().put("minLengthValidator", false);
        txt_username_create.getProperties().put("minLengthValidator", false);
        txt_password.getProperties().put("passwordRequirements", false);
        txt_repeatPassword.getProperties().put("passwordRequirements", false);
        txt_repeatPassword.getProperties().put("passwordsMatch", false);
        hint_password.setText("The passwords shoud match the requirements:\n" + genericValidations.PASSWORD_CONDITIONS);

        // Set listeners
        this.setListeners();

        stage.show(); // Show the stage
    }

    /**
     * Sets the default values for some of the JavaFX components. It also
     * retrieves some data from the server.
     */
    public void setDefaultFieldValues() {
        logger.log(Level.INFO, "Setting the default field values.");

        // Sets the items in the choiceboxes.
        this.chb_privilege.setItems(FXCollections.observableArrayList(UserPrivilege.values()));
        this.chb_status.setItems(FXCollections.observableArrayList(UserStatus.values()));

        // Gets the list of companies and sets the values into the Company choicebox.
        try {
            this.companyList = FXCollections.observableArrayList(companyImplementation.findAllCompanies_XML(new GenericType<List<Company>>() {
            }));
        } catch (Exception ex) { // If the server can't be reached
            Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
        }

        this.chb_company.getItems().setAll(companyList);
        // Sets the first values so we don't get an exception when loading the screen.
        this.chb_status.getSelectionModel().selectFirst();
        this.chb_privilege.getSelectionModel().selectFirst();
    }

    /**
     * Sets the listeners for the inputs in the form.
     */
    public void setListeners() {
        logger.log(Level.INFO, "Setting listeners for the components of the window.");

        // Sets a listener for the firstname field.
        this.txt_firstname.textProperty().addListener((obs, oldText, newText) -> {
            Boolean minlengthValidator = this.genericValidations.minLength(this.txt_firstname, 3, newText, "minLengthValidator"); // Adds a min lenght validator
            this.genericValidations.textLimiter(this.txt_firstname, 200, newText); // Limits the input to 200 characters
            setInputError(minlengthValidator, txt_firstname, hint_firstname);
            this.validate(); // Executes the validation.
        });

        // Sets a listener for the lastname field.
        this.txt_lastname.textProperty().addListener((obs, oldText, newText) -> {
            Boolean minlengthValidator = this.genericValidations.minLength(this.txt_lastname, 3, newText, "minLengthValidator"); // Adds a min lenght validator
            setInputError(minlengthValidator, txt_lastname, hint_lastname); // Sets the input to error mode if the validation is true
            this.genericValidations.textLimiter(this.txt_lastname, 200, newText); // Limits the input to 200 characters
            this.validate(); // Executes the validation.
        });

        // Sets a listener for the username field.
        this.txt_username_create.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_username_create, 200, newText); // Limits the input to 200 characters
            Boolean minlengthValidator = this.genericValidations.minLength(this.txt_username_create, 3, newText, "minLengthValidator"); // Adds a min lenght validator
            this.hint_username.setText("3 characters min");
            setInputError(minlengthValidator, txt_username_create, hint_username);
            this.validate(); // Executes the validation.
        });

        // Sets a listener for the company choicebox.
        this.chb_company.setOnAction(event -> {
            this.validate();
        });

        // Sets a listener for the email field.
        this.txt_email_create.textProperty().addListener((obs, oldText, newText) -> {
            //this.genericValidations.minLength(this.txt_email, 3, newText, "minLengthValidator");
            this.genericValidations.textLimiter(this.txt_email_create, 254, newText); // 254 char text limiter
            boolean emailValidator = this.genericValidations.regexValidator(this.genericValidations.EMAIL_REGEXP, this.txt_email_create, newText.toLowerCase(), "emailValidator"); // Adds a regex validation to check if the email is correct
            this.hint_email.setText(this.genericValidations.TXT_ENTER_VALID_EMAIL);
            setInputError(emailValidator, txt_email_create, hint_email); // Sets the input errors
            this.validate();
        });

        // Sets a listener for the password field.
        this.txt_password.textProperty().addListener((obs, oldText, newText) -> {
            Boolean passwordsMatch = this.genericValidations.comparePasswords(this.txt_password, this.txt_repeatPassword, "passwordsMatch");
            this.genericValidations.textLimiter(this.txt_password, 25, newText); // 25 char text limiter
            this.genericValidations.regexValidator(this.genericValidations.PASS_REGEXP, this.txt_password, newText, "passwordRequirements");
            this.setPasswordFieldsError(passwordsMatch); // Sets the password fields error
            this.validate();
        });

        // Sets a listener for the repeatPassword field.
        this.txt_repeatPassword.textProperty().addListener((obs, oldText, newText) -> {
            Boolean passwordsMatch = this.genericValidations.comparePasswords(this.txt_password, this.txt_repeatPassword, "passwordsMatch");
            this.genericValidations.textLimiter(this.txt_repeatPassword, 25, newText); // 25 char text limiter
            this.genericValidations.regexValidator(this.genericValidations.PASS_REGEXP, this.txt_repeatPassword, newText, "passwordRequirements");
            this.setPasswordFieldsError(passwordsMatch); // Sets the password fields error
            this.validate();
        });
    }

    /**
     * This method sets the password fields in red if they do not match each
     * other
     *
     * @param passwordsMatch boolean indicating if the password is matching or
     * not
     */
    public void setPasswordFieldsError(Boolean passwordsMatch) {
        if (!passwordsMatch) { // If passwords do noy match sets the input error styles
            this.hint_password.setTextFill(Color.RED); // Sets the color of the hint to red
            this.genericValidations.addClass(this.txt_password, "error", Boolean.TRUE); // adds the class
            this.hint_password.setText("Passwords don't match"); // Sets the text
            this.hint_repeatPassword.setTextFill(Color.RED); // Sets the color to red
            this.genericValidations.addClass(this.txt_repeatPassword, "error", Boolean.TRUE); // Sets the error class
        } else { // Sets the default styles to inputs
            if (!Boolean.parseBoolean(txt_password.getProperties().get("passwordRequirements").toString())) {
                this.hint_password.setTextFill(Color.RED); // Sets the color to red
                this.genericValidations.addClass(this.txt_password, "error", Boolean.TRUE); // Changes the css class
                this.hint_password.setText("The passwords do not fulfill the requirements:\n" + genericValidations.PASSWORD_CONDITIONS); // Sets the text
                this.hint_repeatPassword.setTextFill(Color.RED);// Prueba para ver si asier lee los comentarios
                this.genericValidations.addClass(this.txt_repeatPassword, "error", Boolean.TRUE); // Adds the css class
            } else { // If there's not error, set error class to false in the inputs.
                this.hint_password.setTextFill(genericValidations.greyColor); // Sets the color to grey
                this.genericValidations.addClass(this.txt_password, "error", Boolean.FALSE); // Adds the class
                this.hint_password.setText(genericValidations.PASSWORD_CONDITIONS); // Sets the hint text
                this.hint_repeatPassword.setTextFill(genericValidations.greyColor); // Sets the color to grey
                this.genericValidations.addClass(this.txt_repeatPassword, "error", Boolean.FALSE); // Adds the error class to false
            }

        }
    }

    /**
     * Sets the error style for the received TextField.
     *
     * @param validatorStatus
     * @param tf
     * @param hint
     */
    public void setInputError(boolean validatorStatus, TextField tf, Label hint) {
        if (!validatorStatus) { // if theres an error
            this.genericValidations.addClass(tf, "error", Boolean.TRUE); // Set the error class to true
            hint.setTextFill(Color.RED); // Set the color to red
        } else { // si no hay error
            hint.setTextFill(this.genericValidations.greyColor); // set the color to grey
            this.genericValidations.addClass(tf, "error", Boolean.FALSE);
        }
    }

    /**
     * Executes the validation task for each component in the form. If all true,
     * then create a new user. The create button is disabled if the validations
     * fail.
     */
    public void validate() {
        if (Boolean.parseBoolean(this.txt_email_create.getProperties().get("emailValidator").toString()) // If validations pass
                && Boolean.parseBoolean(this.txt_firstname.getProperties().get("minLengthValidator").toString())
                && Boolean.parseBoolean(this.txt_username_create.getProperties().get("minLengthValidator").toString())
                && Boolean.parseBoolean(this.txt_lastname.getProperties().get("minLengthValidator").toString())
                && Boolean.parseBoolean(this.txt_password.getProperties().get("passwordRequirements").toString())
                && Boolean.parseBoolean(this.txt_repeatPassword.getProperties().get("passwordRequirements").toString())
                && Boolean.parseBoolean(this.txt_repeatPassword.getProperties().get("passwordsMatch").toString())
                && this.chb_company.getSelectionModel().getSelectedItem() != null) {
            this.btn_create_create.setDisable(false); // disable create button
        } else { // Else enable create button
            this.btn_create_create.setDisable(true);
        }
    }

    /**
     * The creation button handler. Creates the user object and send a request.
     */
    public void onCreateButtonClickHandler() {
        // Create user.
        User user = new User();
        user.setName(txt_firstname.getText());
        user.setSurname(txt_lastname.getText());
        user.setEmail(txt_email_create.getText());
        user.setUsername(txt_username_create.getText());
        user.setCompany((Company) chb_company.getSelectionModel().getSelectedItem());
        user.setStatus((UserStatus) chb_status.getSelectionModel().getSelectedItem());
        user.setPrivilege((UserPrivilege) chb_privilege.getSelectionModel().getSelectedItem());

        try {
            // Encrypts the password before setting the password to the user.
                AsymmetricEncryption asymmetricEncryption;
           try{
                 asymmetricEncryption = new AsymmetricEncryption(getUserImplementation().getPublicKey());
           }catch(Exception e){
                asymmetricEncryption = new AsymmetricEncryption(rb.getString("PUBLIC_KEY"));
           }
           
// Get the public key and create a new instance of asymmetricEncryption
            String encryptedPassword = asymmetricEncryption.encryptString(txt_password.getText()); // encrypt the password
            user.setPassword(encryptedPassword);
            this.userImplementation.createUser(user); // Sends the request
            userManagementController.getUsers();
            // Show an alert if the user has been created
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // If the request is successful, ask the user to close the window or remain in the same one.
            alert.setTitle("Go back to the user management screen?");
            alert.setHeaderText("User successfully created");
            alert.setContentText("You will go back to the management window if you press OK");
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get().equals(ButtonType.OK)) {
                userManagementController.getUsers();
                userManagementController.createFilteredListAndTableListeners();
                stage.close();
            } else {
                alert.close();
            }
        } catch (ClientErrorException ex) { // If there's a client error
            Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
        } catch (UsernameAlreadyExistsException ex) { // Thrown when the username already exists in the database.
            Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
            hint_username.setText("The username already exists.");
            btn_create_create.setDisable(true);
            setInputError(false, txt_username_create, hint_username);
        } catch (EmailAlreadyExistsException ex) { // Thrown when the email already exists in the database.
            Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
            hint_email.setText("The email already exists.");
            btn_create_create.setDisable(true);
            setInputError(false, txt_email_create, hint_email);
        } catch (EmailAndUsernameAlreadyExistException e) {
            Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, e);
            hint_username.setText("The username already exists.");
            btn_create_create.setDisable(true);
            setInputError(false, txt_username_create, hint_username);

            Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, e);
            hint_email.setText("The email already exists.");
            btn_create_create.setDisable(true);
            setInputError(false, txt_email_create, hint_email);
        } catch (Exception ex) { // If the server disconnects this exception is thrown.
            Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
        }
    }

    /**
     * Closes the current window.
     */
    public void closeWindow() {
        // Asks the user if really want to close the window.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("Application will be closed");
        alert.setContentText("You will close the application");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            stage.close();
        } else {
            alert.close();
        }
    }

    /**
     * A method to show an alert so we don't repeat some code.
     *
     * @param type
     * @param title
     * @param header
     * @param content
     */
    public void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Gets the stage.
     *
     * @return
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage.
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Gets the user implementation.
     *
     * @return
     */
    public UserInterface getUserImplementation() {
        return userImplementation;
    }

    /**
     * Sets the user implementation.
     *
     * @param userImplementation
     */
    public void setUserImplementation(UserInterface userImplementation) {
        this.userImplementation = userImplementation;
    }

    /**
     * Gets the user management controller.
     *
     * @return
     */
    public UserManagementController getUserManagementController() {
        return userManagementController;
    }

    /**
     * Sets the user management controller.
     */
    public void setUserManagementController(UserManagementController userManagementController) {
        this.userManagementController = userManagementController;
    }

}
