/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import com.esotericsoftware.kryo.Kryo;
import com.rits.cloning.Cloner;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.scene.control.behavior.TextBinding;
import crudclient.factories.UserFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import crudclient.model.User;
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
import crudclient.interfaces.UserInterface;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Mikel
 */
public class UserManagementController {

    private Stage stage;
    private static final Logger logger = Logger.getLogger("signupsignin.controllers.SignUpController");
    private final GenericValidations genericValidations;
    private UserInterface userImplementation;
    private ObservableList<User> masterData = FXCollections.observableArrayList();

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

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_create;

    // Table related stuff
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> tc_name;
    @FXML
    private TableColumn<User, String> tc_surname;
    @FXML
    private TableColumn<User, String> tc_username;
    @FXML
    private TableColumn<User, String> tc_email;
    @FXML
    private TableColumn<User, String> tc_company;
    @FXML
    private TableColumn<User, String> tc_status;
    @FXML
    private TableColumn<User, String> tc_privilege;

    public UserManagementController() {
        this.genericValidations = new GenericValidations();
    }

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
        this.setCellValueFactories();

        // Set stage
        this.setStage(stage);

        // Set some properties of the stage
        stage.setScene(scene);
        stage.setTitle("User Management"); // Sets the title of the window
        stage.setResizable(false); // Prevents the user to resize the window.
        scene.getStylesheets().add(getClass().getResource("/crudclient/view/styles/inputStyle.css").toExternalForm()); // Imports the CSS file used for errors in some inputs.

        // Set the default values for some cells
        this.setDefaultFieldValues();
        this.btn_delete.setDisable(true);
        stage.show(); // Show the stage

        // PRUEBAS DE ENCRIPTAR Y RECIBIR LA CLAVE PÚBLICA DEL SERVIDOR
//        String prueba = this.userImplementation.getPublicKey();
//        AsymmetricEncryption enc = new AsymmetricEncryption(prueba);
//        String encryptedString = enc.encryptString("EEEE");
//        System.out.println(encryptedString);
//        System.out.println(prueba);
    }

    public void setCellValueFactories() {
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

        this.table.getSelectionModel().selectedItemProperty().addListener(this::handleUsersTableSelectionChanged);

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

    /**
     * Manages the selection of a row in the table.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handleUsersTableSelectionChanged(ObservableValue observable, User oldValue, User newValue) {
        // System.out.println(newValue.getEmail());
        this.btn_delete.setDisable(false);
    }

    public void setDefaultFieldValues() {
        this.chb_privilege.setItems(FXCollections.observableArrayList(UserPrivilege.values()));
        this.chb_status.setItems(FXCollections.observableArrayList(UserStatus.values()));

        // Sets the first values so we don't get an exception when loading the screen.
        this.chb_status.getSelectionModel().selectFirst();
        this.chb_privilege.getSelectionModel().selectFirst();
        // Se obtiene la lista de usuarios utilizando la implementación que hay en la propiedad de la clase. Se necesita pasar desde la ventana anterior o desde el método main.
        getUsers();

        // Crea las listas de filtrado y llama al método que crea los listeners.
        FilteredList<User> filteredData = new FilteredList<>(masterData, p -> true);
        setSearchFilterListeners(filteredData);
        SortedList<User> sortedData = new SortedList<>(filteredData);

        // Bindea de 
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        this.table.setItems(sortedData);
    }

    public void getUsers() {
        this.masterData = FXCollections.observableArrayList(getUserImplementation().getUsers(new GenericType<List<User>>() {
        }));
    }

    public void handleOnClickCreateButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/SignUp.fxml"));
        Parent root = (Parent) loader.load();
        UserCreationController controller = ((UserCreationController) loader.getController());
        controller.setUserImplementation(getUserImplementation());
        controller.setStage(getStage());
        controller.initStage(root);
    }

    public void setSearchFilterListeners(FilteredList<User> filteredData) {
        if (chb_status.getSelectionModel() == null) {
            System.out.println("El modelo del checkboz es null");
        }
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(()
                -> user -> user.getName().contains(txt_name.getText().toLowerCase().trim())
                && user.getSurname().contains(txt_surname.getText().toLowerCase().trim())
                && user.getEmail().contains(txt_email.getText().toLowerCase().trim())
                && user.getUsername().contains(txt_username.getText())
                && user.getCompany().getName().contains(txt_company.getText().toLowerCase().trim())
                && user.getStatus().toString().equalsIgnoreCase(chb_status.getSelectionModel().getSelectedItem().toString())
                && user.getPrivilege().toString().equalsIgnoreCase(chb_privilege.getSelectionModel().getSelectedItem().toString()),
                txt_name.textProperty(),
                txt_surname.textProperty(),
                txt_email.textProperty(),
                txt_username.textProperty(),
                txt_company.textProperty(),
                chb_status.getSelectionModel().selectedItemProperty(),
                chb_privilege.getSelectionModel().selectedItemProperty()
        ));

//        txt_name.textProperty().addListener((obsVal, oldValue, newValue) -> {
//            filteredData.setPredicate(user -> user.getName().contains(txt_name.getText().toLowerCase().trim()));
//        });
//
//        txt_surname.textProperty().addListener((obsVal, oldValue, newValue) -> {
//            filteredData.setPredicate(user -> user.getSurname().contains(txt_surname.getText().toLowerCase().trim()));
//        });
//        txt_email.textProperty().addListener((obsVal, oldValue, newValue) -> {
//            filteredData.setPredicate(user -> user.getEmail().contains(txt_email.getText().toLowerCase().trim()));
//        });
//        txt_name.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(user -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                // Returns true if matches, else returns false
//                return user.getName().toLowerCase().contains(newValue.toLowerCase());
//            });
//        });
//        txt_surname.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(user -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                // Returns true if matches, else returns false
//                return user.getSurname().toLowerCase().contains(newValue.toLowerCase());
//            });
//        });
//        txt_company.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(user -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                // Returns true if matches, else returns false
//                return user.getCompany().getName().toLowerCase().contains(newValue.toLowerCase());
//            });
//        });
//
//        txt_username.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(user -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                // Returns true if matches, else returns false
//                return user.getUsername().toLowerCase().contains(newValue.toLowerCase());
//            });
//        });
//        txt_email.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(user -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                // Returns true if matches, else returns false
//                return user.getEmail().toLowerCase().contains(newValue.toLowerCase());
//            });
//        });
    }

    public void searchByProperty() {

    }

    public void validate() {

    }

    public Stage getStage() {
        return this.stage;
    }

    public void setUserImplementation(UserInterface user) {
        this.userImplementation = user;
    }

    public UserInterface getUserImplementation() {
        return this.userImplementation;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
