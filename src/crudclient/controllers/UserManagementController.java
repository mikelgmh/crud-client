/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.factories.UserFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import crudclient.interfaces.User;
import crudclient.model.UserModel;
import crudclient.model.UserPrivilege;
import crudclient.model.UserStatus;
import crudclient.util.security.AsymmetricEncryption;
import crudclient.util.validation.GenericValidations;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML
    private Label hint_email;

    // Table related stuff
    @FXML
    private TableView<UserModel> table;
    @FXML
    private TableColumn<UserModel, String> tc_name;
    @FXML
    private TableColumn<UserModel, String> tc_surname;
    @FXML
    private TableColumn<UserModel, String> tc_username;
    @FXML
    private TableColumn<UserModel, String> tc_email;
    @FXML
    private TableColumn<UserModel, String> tc_company;
    @FXML
    private TableColumn<UserModel, String> tc_status;
    @FXML
    private TableColumn<UserModel, String> tc_privilege;

    public void initStage(Parent parent) {

        // Sets the default hint behavior
        this.hint_email.setVisible(false);

        logger.log(Level.INFO, "Loading window...");
        // Creates a scena and a stage and opens the window.
        Scene scene = new Scene(parent);
        Stage stage = new Stage();

        // Set listeners
        this.setListeners();

        // Set factories
        this.setFactoriesForColumnCells();

        // Set stage
        this.setStage(stage);

        // Set some properties of the stage
        stage.setScene(scene);
        stage.setTitle("User Management"); // Sets the title of the window
        stage.setResizable(false); // Prevents the user to resize the window.
        scene.getStylesheets().add(getClass().getResource("/crudclient/view/styles/inputStyle.css").toExternalForm()); // Imports the CSS file used for errors in some inputs.

        // Set the default values for some cells
        this.setDefaultFieldValues();
        stage.show(); // Show the stage

        // PRUEBAS DE ENCRIPTAR Y RECIBIR LA CLAVE PÚBLICA DEL SERVIDOR
        String prueba = this.userImplementation.getPublicKey();
        AsymmetricEncryption enc = new AsymmetricEncryption(prueba);
        String encryptedString = enc.encryptString("EEEE");
        System.out.println(encryptedString);
        System.out.println(prueba);
    }

    public void setFactoriesForColumnCells() {
        tc_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tc_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tc_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        tc_company.setCellValueFactory(new PropertyValueFactory<>("username"));
        tc_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tc_privilege.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * Sets the listeners for all the inputs.
     */
    public void setListeners() {
        logger.log(Level.INFO, "Setting listeners for the components of the window.");
        this.txt_name.textProperty().addListener((obs, oldText, newText) -> {
            //this.genericValidations.minLength(this.txt_name, 3, newText, "minLengthValidator"); // Adds a min lenght validator
            this.genericValidations.textLimiter(this.txt_name, 200, newText); // Limits the input to 200 characters
            this.validate(); // Executes the validation.
        });

        this.txt_surname.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_surname, 200, newText); // Limits the input to 200 characters
            this.validate(); // Executes the validation.
        });

        this.txt_username.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_username, 200, newText); // Limits the input to 200 characters
            this.validate(); // Executes the validation.
        });
        this.txt_company.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_company, 200, newText); // Limits the input to 200 characters
            this.validate(); // Executes the validation.
        });
        
        this.table.getSelectionModel().selectedIndexProperty().addListener(this::handleUsersTableSelectionChanged);

        // Validation for the Email field
        this.txt_email.textProperty().addListener((obs, oldText, newText) -> {
            //this.genericValidations.minLength(this.txt_email, 3, newText, "minLengthValidator");
            this.genericValidations.textLimiter(this.txt_email, 254, newText);
//            boolean emailValidator = this.genericValidations.regexValidator(this.genericValidations.EMAIL_REGEXP, this.txt_email, newText.toLowerCase(), "emailValidator"); // Adds a regex validation to check if the email is correct
//            this.hint_email.setText(this.genericValidations.TXT_ENTER_VALID_EMAIL);
//            if (!emailValidator) {
//
//                this.genericValidations.addClass(this.txt_email, "error", Boolean.TRUE);
//            } else {
//                this.hint_email.setTextFill(this.genericValidations.greyColor);
//                this.genericValidations.addClass(this.txt_email, "error", Boolean.FALSE);
//            }
            // Changes the color of the inputs when the user types.

            this.validate();
        });
    }
    
    private void handleUsersTableSelectionChanged(ObservableValue observable, Object oldValue, Object newValue){
        System.out.println(newValue);
    }

    public void setDefaultFieldValues() {
        this.chb_privilege.setItems(FXCollections.observableArrayList(UserPrivilege.values()));
        this.chb_status.setItems(FXCollections.observableArrayList(UserStatus.values()));

        User user = new UserFactory().getUserImplementation(UserFactory.ImplementationType.TEST_IMPLEMENTATION);
        ObservableList i = FXCollections.observableArrayList(user.getUsers());
        table.setItems(i);
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
