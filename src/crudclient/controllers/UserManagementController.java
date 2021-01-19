/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import crudclient.model.User;
import crudclient.model.UserPrivilege;
import crudclient.model.UserStatus;
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
import crudclient.model.Product;
import javafx.scene.control.cell.ChoiceBoxListCell;
import java.io.IOException;
import java.util.Arrays;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableArray;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.control.cell.ComboBoxTableCell;
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
    private TableColumn<User, UserPrivilege> tc_privilege;

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

        this.configTableView();

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

    public void configTableView() {
        table.setEditable(true);

        tc_name.setCellFactory(TextFieldTableCell.forTableColumn());
        tc_name.setOnEditCommit((TableColumn.CellEditEvent<User, String> data) -> {
            // System.out.println(table.getSelectionModel().getSelectedItem().getName());
            table.getSelectionModel().getSelectedItem().setName(data.getNewValue());
            //   System.out.println(table.getSelectionModel().getSelectedItem().getName());
            userImplementation.editUser(table.getSelectionModel().getSelectedItem());
        });

        ObservableList userStatuses = FXCollections.observableArrayList(UserStatus.values());
        tc_status.setCellFactory(ComboBoxTableCell.forTableColumn(userStatuses));

        ObservableList userPrivileges = FXCollections.observableArrayList(UserPrivilege.values());
        tc_privilege.setCellFactory(ComboBoxTableCell.forTableColumn(userPrivileges));

        tc_privilege.setOnEditCommit((TableColumn.CellEditEvent<User, UserPrivilege> data) -> {
            table.getSelectionModel().getSelectedItem().setPrivilege(data.getNewValue());
            table.refresh();
            User i = table.getSelectionModel().getSelectedItem();
            userImplementation.editUser(i);
        });
    }

    /**
     * Sets the listeners for all the inputs.
     */
    public void setListeners() {
        logger.log(Level.INFO, "Setting listeners for the components of the window.");
        this.txt_name.textProperty().addListener((obs, oldText, newText) -> {
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
            this.genericValidations.textLimiter(this.txt_email, 254, newText);
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
        this.getUsers();

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
    }

    public void onDeleteButtonClickAction() {
        User u = table.getSelectionModel().getSelectedItem();
        this.getUserImplementation().deleteUser(u.getId().toString());
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
