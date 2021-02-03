/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.exceptions.CellMaxLengthException;
import crudclient.exceptions.EmailAlreadyExistsException;
import crudclient.exceptions.InvalidEmailException;
import crudclient.exceptions.UsernameAlreadyExistsException;
import crudclient.factories.CompanyFactory;
import crudclient.factories.OrderFactory;
import crudclient.factories.ProductFactory;
import crudclient.factories.SignInFactory;
import crudclient.factories.UserFactory;
import crudclient.interfaces.CompanyInterface;
import crudclient.interfaces.OrderInterface;
import crudclient.interfaces.ProductInterface;
import crudclient.interfaces.SignInInterface;
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
import crudclient.model.Company;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Mikel
 */
public class UserManagementController {

    // The needed properties and variables to run this.
    /**
     * The stage for this window.
     */
    private Stage stage;
    /**
     * The logger instance.
     */
    private static final Logger logger = Logger.getLogger("signupsignin.controllers.SignUpController"); // Logger instance
    /**
     * The generic validations instance.
     */
    private final GenericValidations genericValidations; // Generic validations instance
    /**
     * The user implementation for this window.
     */
    private UserInterface userImplementation; // The user implementation
    /**
     * The list of users.
     */
    private ObservableList<User> masterData = FXCollections.observableArrayList();
    /**
     * The list of companies.
     */
    private ObservableList<Company> companiesList = FXCollections.observableArrayList();
    /**
     * The implementation of company.
     */
    private CompanyInterface companyImplementation;
    /**
     * the formatter for dates.
     */
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    /**
     * The current user.
     */
    private User currentUser;
    /**
     * default zone id used for formatting dates.
     */
    private ZoneId defaultZoneId = ZoneId.systemDefault();
    /**
     * Another formatter for dates.
     */
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

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
    private DatePicker txt_lastAccess;
    @FXML
    private ChoiceBox chb_status;
    @FXML
    private ChoiceBox chb_privilege;
    @FXML
    private Label hint_email;
    @FXML
    private Text text_lastUpdate;

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
    private TableColumn<User, Company> tc_company;
    @FXML
    private TableColumn<User, UserStatus> tc_status;
    @FXML
    private TableColumn<User, UserPrivilege> tc_privilege;
    @FXML
    private TableColumn<User, String> tc_lastAccess;

    @FXML
    private VBox vbox;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuHome;
    @FXML
    private MenuItem menuLogout;
    @FXML
    private MenuItem menuClose;
    @FXML
    private Menu menuManagement;
    @FXML
    private MenuItem menuCompanies;
    @FXML
    private MenuItem menuUsers;
    @FXML
    private MenuItem menuOrders;
    @FXML
    private MenuItem menuProducts;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Label hint_email1;


    /**
     * The empty constructor.
     */
    public UserManagementController() {
        this.genericValidations = new GenericValidations();
    }

    /**
     * The method called when opening this user manager.
     *
     * @param parent
     */
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
        
        //Set menu
        setMenu();

        // Company implementation
        CompanyFactory companyFactory = new CompanyFactory();
        this.companyImplementation = companyFactory.getImplementation();

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

    /**
     * Sets the cell value factory for each table column.
     */
    public void setCellValueFactories() {
        tc_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tc_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tc_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        tc_company.setCellValueFactory(new PropertyValueFactory<>("company"));
        tc_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tc_privilege.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        tc_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tc_lastAccess.setCellValueFactory(new PropertyValueFactory<>("lastAccess"));
    }

    /**
     * Configs the table view, adds listeners and makes updates.
     */
    public void configTableView() {
        table.setEditable(true);

        // Name column, listener and exception handler
        tc_name.setCellFactory(TextFieldTableCell.forTableColumn()); // Set cell factory
        tc_name.setOnEditCommit((TableColumn.CellEditEvent<User, String> data) -> {
            try { // Tries to apply the changes and submit them to the server
                checkCellMaxLength(254, data.getNewValue().length());
                table.getSelectionModel().getSelectedItem().setName(data.getNewValue());
                userImplementation.editUser(table.getSelectionModel().getSelectedItem());
                updateLastModification(); // Updates the last modification text.
            } catch (CellMaxLengthException ex) { // thrown if the max length is exceded.
                Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
                table.refresh();
            } catch (Exception ex) { // If the server stops responding this is thrown.
                Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
            }

        });

        // Surname column
        tc_surname.setCellFactory(TextFieldTableCell.forTableColumn()); // Sets the surname cell factory
        tc_surname.setOnEditCommit((TableColumn.CellEditEvent<User, String> data) -> { // Sets the on commit listener
            try {
                checkCellMaxLength(254, data.getNewValue().length()); // checks the max length
                table.getSelectionModel().getSelectedItem().setSurname(data.getNewValue()); // Sets the surname to the new value.
                userImplementation.editUser(table.getSelectionModel().getSelectedItem()); // Updates the user in the database.
                updateLastModification(); // Updates the last modification text.
            } catch (CellMaxLengthException ex) { // Thrown when the max length is reached
                Logger.getLogger(UserManagementController.class.getName()).log(Level.WARNING, null, ex);
                table.refresh();
            } catch (Exception ex) { // Thrown when the server stops responding.
                Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
            }

        });

        // Username column
        tc_username.setCellFactory(TextFieldTableCell.forTableColumn()); // Sets the cell factory for the username column
        tc_username.setOnEditCommit((TableColumn.CellEditEvent<User, String> data) -> { // The listener for this column.
            try {
                checkCellMaxLength(254, data.getNewValue().length()); // Checks the maxlength
                searchUsernameAlreadyExists(table.getSelectionModel().getSelectedItem(), data.getNewValue()); // search for the same username in the users list.
                table.getSelectionModel().getSelectedItem().setUsername(data.getNewValue()); // Sets the username for this selected item.
                userImplementation.editUser(table.getSelectionModel().getSelectedItem()); // Edits this field in the database.
                updateLastModification(); // Updates the last modification text.
            } catch (CellMaxLengthException ex) { // Thrown when the max length is exceded.
                Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UsernameAlreadyExistsException ex) { // Thrown if the user already exists.
                Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Can't update this user", "Username already exists", "The username already exists, pick another username.");
                table.refresh();
            } catch (Exception ex) { // thrown if the server can't be reached
                Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
            }

        });

        // Email column
        tc_email.setCellFactory(TextFieldTableCell.forTableColumn()); // Sets the email cell factory
        tc_email.setOnEditCommit((TableColumn.CellEditEvent<User, String> data) -> { // Sets the listener for this column
            try {
                checkCellMaxLength(254, data.getNewValue().length()); // Checks the max length
                searchEmailAlreadyExists(table.getSelectionModel().getSelectedItem(), data.getNewValue()); // Searches for the same email
                boolean validEmail = genericValidations.simpleStringRegexValidator(GenericValidations.EMAIL_REGEXP, data.getNewValue()); // Checks if the email is valir or not.
                if (!validEmail) { // Throw an exception if the email is not valid.
                    throw new InvalidEmailException();
                }
                table.getSelectionModel().getSelectedItem().setEmail(data.getNewValue()); // Sets the email for this user with the new value.
                userImplementation.editUser(table.getSelectionModel().getSelectedItem()); // Edits the user in the database.
                updateLastModification(); // Changes the text of the last modification text to the current time.
            } catch (CellMaxLengthException ex) { // when max length is surpassed
                Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EmailAlreadyExistsException ex) { // If the mail already exists
                Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Can't update this user", "Email already exists", "The email already exists, pick another email.");
                table.refresh();
            } catch (InvalidEmailException ex) { // If the email format is not valid
                Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Invalid email", "Email is invalid", "The email format is invalid.");
                table.refresh();
            } catch (Exception ex) { // If the server can't be reached
                Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
            }
        });

        // User Status column
        ObservableList userStatuses = FXCollections.observableArrayList(UserStatus.values()); // Sets the user statuses to an observable list
        tc_status.setCellFactory(ComboBoxTableCell.forTableColumn(userStatuses)); // Sets the cell factory
        tc_status.setOnEditCommit((TableColumn.CellEditEvent<User, UserStatus> data) -> { // Sets the listener for this column
            table.getSelectionModel().getSelectedItem().setStatus(data.getNewValue()); // Sets the status for the current user
            table.refresh(); // Refreshes the table.
            try {
                userImplementation.editUser(table.getSelectionModel().getSelectedItem()); // Edits the user in the database.
                updateLastModification(); // Updates the last modification text.
            } catch (Exception ex) { // Thrown if the server can't be reached
                Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
            }

        });

        // User Privilege column
        ObservableList userPrivileges = FXCollections.observableArrayList(UserPrivilege.values()); // Gets the user privileges
        tc_privilege.setCellFactory(ComboBoxTableCell.forTableColumn(userPrivileges)); // Sets the cell factory
        tc_privilege.setOnEditCommit((TableColumn.CellEditEvent<User, UserPrivilege> data) -> { // Sets the listener.
            table.getSelectionModel().getSelectedItem().setPrivilege(data.getNewValue()); // Sets the new privilege
            table.refresh(); // Refreshes the table
            try {
                userImplementation.editUser(table.getSelectionModel().getSelectedItem()); // Tries to edit the user sending a request to the server.
                updateLastModification(); // Updaets the last modification text.
            } catch (Exception ex) { // Thrown if the server can't be reached, shows an alert.
                Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
            }

        });

        // Company column
        CompanyFactory companyFactory = new CompanyFactory(); // Creates a new company factory
        this.companyImplementation = companyFactory.getImplementation(); // Gets the factory implementationS
        ObservableList<Company> companies = null; // Creates a new Company observable list.
        try { // Tries to get the list of companies from the database.
            companies = FXCollections.observableArrayList(companyImplementation.findAllCompanies_XML(new GenericType<List<Company>>() {
            }));
        } catch (Exception ex) { // Thrown if the server can't be reached.
            Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
        }
        tc_company.setCellFactory(ComboBoxTableCell.forTableColumn(companies)); // Sets the cell factory
        tc_company.setOnEditCommit((TableColumn.CellEditEvent<User, Company> data) -> { // Sets the listener for this column.
            table.getSelectionModel().getSelectedItem().setCompany(data.getNewValue()); // Sets the new company value for this user
            table.refresh(); // Refreshes the table.
            updateLastModification(); // Updates the last modification text.
            try {
                userImplementation.editUser(table.getSelectionModel().getSelectedItem()); // Makes a request to try to update the user.
            } catch (Exception ex) { // Thrown if the server can't be reached
                Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
                showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
            }
        });
    }

    /**
     * Gets a list of companies using the implementation of companies declared
     * in the init stage.
     */
    public void getCompanies() {
        try { // Get the list
            companiesList = FXCollections.observableArrayList(companyImplementation.findAllCompanies_XML(new GenericType<List<Company>>() {
            }));
        } catch (Exception ex) { // Thrown if the server can't be reached
            Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, ex);
            showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
        }
    }

    /**
     * Searches the given email in the user list.
     *
     * @param user The user in which it is the email
     * @param email The email to search
     * @throws EmailAlreadyExistsException
     */
    public void searchEmailAlreadyExists(User user, String email) throws EmailAlreadyExistsException {
        for (int i = 0; i < masterData.size(); i++) { // Checks every single user
            if (email.equalsIgnoreCase(masterData.get(i).getEmail()) && !Objects.equals(user.getId(), masterData.get(i).getId())) { // If the user is not the same but the email yes, then..
                throw new EmailAlreadyExistsException();
            }
        }
    }

    /**
     * Updates the last modification text in the bottom right.
     */
    public void updateLastModification() {
        SimpleDateFormat lastUpdateTime = new SimpleDateFormat("HH:mm:ss"); // Creates a new SimpleDateFormat
        Date date = new Date(); // creates a new date
        text_lastUpdate.setText("Last update: " + lastUpdateTime.format(date)); // Formats the current date to get just the time.
    }

    /**
     * Search if the new username already exists in the user list and throws an
     * exception if it is found.
     *
     * @param user The user.
     * @param username The username to find.
     * @throws UsernameAlreadyExistsException
     */
    public void searchUsernameAlreadyExists(User user, String username) throws UsernameAlreadyExistsException {
        for (int i = 0; i < masterData.size(); i++) { // Searches the username in the user list.
            if (username.equalsIgnoreCase(masterData.get(i).getUsername()) && !Objects.equals(user.getId(), masterData.get(i).getId())) {
                throw new UsernameAlreadyExistsException();
            }
        }
    }

    /**
     * Checks the max length allowed in a cell and throws an exception if its
     * bigger than expected.
     *
     * @param maxLength
     * @param currentLength
     * @throws CellMaxLengthException
     */
    public void checkCellMaxLength(int maxLength, int currentLength) throws CellMaxLengthException {
        if (currentLength > maxLength) {
            showAlert(Alert.AlertType.WARNING, "Character limit reached", "", "The max length allowed for this field is " + maxLength);
            throw new CellMaxLengthException();
        }
    }

    /**
     * Sets the listeners for all the inputs.
     */
    public void setListeners() {
        logger.log(Level.INFO, "Setting listeners for the components of the window.");
        // Sets a listener for the textproperty.
        this.txt_name.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_name, 200, newText); // Limits the input to 200 characters
        });
        // Sets a listener for the surname
        this.txt_surname.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_surname, 200, newText); // Limits the input to 200 characters
        });
        // Sets a listener for the username
        this.txt_username.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_username, 200, newText); // Limits the input to 200 characters
        });
        this.txt_company.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_company, 200, newText); // Limits the input to 200 characters
        });
        // Sets a listener for the selected item in the table.
        this.table.getSelectionModel().selectedItemProperty().addListener(this::handleUsersTableSelectionChanged);

        // Validation for the Email field
        this.txt_email.textProperty().addListener((obs, oldText, newText) -> {
            this.genericValidations.textLimiter(this.txt_email, 254, newText);
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
        // User loggedUser = menuController.getUser();
        User loggedUser = DashboardController.loggedUser;
        // If the logged user's id is 1, it means it is superuser, the master one
        if (loggedUser.getId() == 1) {
            // Disable delete button if the selected user is the logged user.
            try {
                if (Objects.equals(loggedUser.getId(), newValue.getId())) { // If the logged user is the same as the selected user
                    this.btn_delete.setDisable(true); // Disable delete button
                    this.table.setEditable(false); // Set the editable table to false.
                } else { // Enable the delete button if the selected user is not the logged one.
                    this.btn_delete.setDisable(false); // Enable the delete button
                    this.table.setEditable(true); // Set the table editable
                }
            } catch (Exception e) { // Throe if there's an error selecting the user and comparing it to the current logged in user.
                Logger.getLogger(UserCreationController.class.getName()).log(Level.SEVERE, null, e);
            }
        } else { // If the logged user's id it is not 1, it mean it's a superuser, but not the master one
            if (newValue.getPrivilege().equals(UserPrivilege.SUPERUSER)) {
                this.btn_delete.setDisable(true); // Set the delete button disabled
                this.table.setEditable(false); // Set editable to false
            } else {
                this.table.setEditable(true); // Set editable to true
                this.btn_delete.setDisable(false); // Set disable create button to false
            }
        }

    }

    /**
     * Formats a date given to LocalDate
     *
     * @param dateString
     * @return
     */
    public static final LocalDate LOCAL_DATE(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // Creates a formatter
        LocalDate localDate = LocalDate.parse(dateString, formatter); // Creates a Localdate and parses the date string.
        return localDate;
    }

    /**
     * Sets the default values for the fields in this window.
     */
    public void setDefaultFieldValues() {
        // Sets the values for the privilege and the status choiceboxes.
        this.chb_privilege.setItems(FXCollections.observableArrayList(UserPrivilege.values()));
        this.chb_status.setItems(FXCollections.observableArrayList(UserStatus.values()));

        // Sets the first values so we don't get an exception when loading the screen.
        this.chb_status.getSelectionModel().selectFirst();
        this.chb_privilege.getSelectionModel().selectFirst();
        // Gets the list of users and companies.
        this.getUsers();
        this.getCompanies();
        createFilteredListAndTableListeners(); // Creates the filtered list and sorted list used for filters in the table.
    }

    /**
     * Gets the users from the database.
     */
    public void getUsers() {
        try {
            this.masterData = FXCollections.observableArrayList(getUserImplementation().getUsers(new GenericType<List<User>>() { // Get the users
            }));
        } catch (Exception e) { // Thrown if the server can't be reached
            showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
        }
    }

    /**
     * Creates the filtered list and set the table listeners.
     */
    public void createFilteredListAndTableListeners() {
        // Creates the filtered list
        FilteredList<User> filteredData = new FilteredList<>(masterData, p -> true);
        setSearchFilterListeners(filteredData); // Sets the search filter listeners
        SortedList<User> sortedData = new SortedList<>(filteredData); // Creates the sorted data using a sorted list

        // Binds the sorted data and sets it to the table.
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        this.table.setItems(sortedData);
    }

    /**
     * The create button click handler.
     *
     * @throws IOException
     */
    @FXML
    public void handleOnClickCreateButton() throws IOException {
        // Creates a new window and goes to it.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/SignUp.fxml")); // Picks thr fxml for the creation window.
        Parent root = (Parent) loader.load();
        UserCreationController controller = ((UserCreationController) loader.getController());
        controller.setUserImplementation(getUserImplementation());
        controller.setUserManagementController(this);
        controller.setStage(getStage());
        controller.initStage(root);
    }

    /**
     * Sets the listener for each filter input.
     *
     * @param filteredData
     */
    public void setSearchFilterListeners(FilteredList<User> filteredData) {
        try {
            // This sets the listener for the desired filter inputs.
            filteredData.predicateProperty().bind(Bindings.createObjectBinding(()
                    -> user -> user.getName().toLowerCase().contains(txt_name.getText().toLowerCase().trim())
                    && user.getSurname().toLowerCase().contains(txt_surname.getText().toLowerCase().trim())
                    && user.getEmail().toLowerCase().contains(txt_email.getText().toLowerCase().trim())
                    && user.getUsername().toLowerCase().contains(txt_username.getText())
                    && user.getCompany().getName().toLowerCase().contains(txt_company.getText().toLowerCase().trim())
                    && user.getStatus().toString().equalsIgnoreCase(chb_status.getSelectionModel().getSelectedItem().toString())
                    && datePickerChecker(user)
                    && user.getPrivilege().toString().equalsIgnoreCase(chb_privilege.getSelectionModel().getSelectedItem().toString()),
                    txt_name.textProperty(),
                    txt_surname.textProperty(),
                    txt_email.textProperty(),
                    txt_username.textProperty(),
                    txt_company.textProperty(),
                    chb_status.getSelectionModel().selectedItemProperty(),
                    txt_lastAccess.getEditor().textProperty(),
                    chb_privilege.getSelectionModel().selectedItemProperty()
            ));
        } catch (Exception e) { // Error if there's an error with the date somehow. Shouldn't be triggered.
            showAlert(Alert.AlertType.ERROR, "Empty date", "The date can't be empty", "Select a date to continue");
        }
    }

    /**
     * Checks the date picker
     *
     * @param user
     * @return
     */
    public boolean datePickerChecker(User user) {
        try { // Returns the desired value.
            return formatter.format(user.getLastAccess()).contains(txt_lastAccess.getValue().toString());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * The event handler for the delete button click
     */
    @FXML
    public void onDeleteButtonClickAction() {
        User u = table.getSelectionModel().getSelectedItem(); // Gets the selected item in the table
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // Creates a new alert and asks the user if really want to delete the user.
        alert.setTitle("Delete row?");
        alert.setHeaderText("Delete?");
        alert.setContentText("If you are sure you want to delete this user, click OK. This will delete every single data related to this user. Be sure if you really want to delete it!.");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) { // If ok, try to delete the user in the database
            try {
                this.getUserImplementation().deleteUser(u.getId().toString());
            } catch (Exception e) { // If server can't be reached.
                showAlert(Alert.AlertType.ERROR, "Can't connect to server", "Connection error", "The server couldn't be reached.");
            }
            // Remove the user also from the user list in the table.
            masterData.remove(u);
        } else {
            alert.close(); // Close the alert
        }
    }

    /**
     * Shows an alert with the desired parameters.
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
     * gets the stage
     *
     * @return
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Sets the user implementation
     *
     * @param user
     */
    public void setUserImplementation(UserInterface user) {
        this.userImplementation = user;
    }

    /**
     * gets the user implementation
     *
     * @return
     */
    public UserInterface getUserImplementation() {
        return this.userImplementation;
    }

    /**
     * sets the stage
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * gets the current user
     *
     * @return
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user
     *
     * @param currentUser
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * gets the master data.
     *
     * @return
     */
    public ObservableList<User> getMasterData() {
        return masterData;
    }

    /**
     * sets the master data.
     *
     * @param masterData
     */
    public void setMasterData(ObservableList<User> masterData) {
        this.masterData = masterData;
    }

        private void setMenu() {
        menuLogout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sign Out");
                String s = "Are you sure you want to logout?";
                alert.setContentText(s);
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/SignIn.fxml"));
                        Parent root = (Parent) loader.load();
                        SignInController controller = ((SignInController) loader.getController());
                        SignInFactory signInFactory = new SignInFactory();
                        SignInInterface signInImplementation = signInFactory.getImplementation();
                        controller.setImplementation(signInImplementation);
                        controller.setStage(new Stage());
                        controller.initStage(root);
                        stage.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        menuClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Close confirmation");
                alert.setHeaderText("Application will be closed");
                alert.setContentText("You will close the application");
                alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get().equals(ButtonType.OK)) {
                    Platform.exit();
                } else {
                    t.consume();
                    alert.close();
                }
            }
        });

        menuUsers.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/UserManagement.fxml"));
                    Parent root = (Parent) loader.load();
                    UserManagementController controller = ((UserManagementController) loader.getController());
                    UserFactory userFactory = new UserFactory();
                    UserInterface userImplementation = userFactory.getUserImplementation(UserFactory.ImplementationType.REST_CLIENT);
                    controller.setUserImplementation(userImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuCompanies.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/companies.fxml"));
                    Parent root = (Parent) loader.load();
                    CompanyController controller = ((CompanyController) loader.getController());
                    CompanyFactory companyFactory = new CompanyFactory();
                    CompanyInterface companyImplementation = companyFactory.getImplementation();
                    controller.setImplementation(companyImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuOrders.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/orders.fxml"));
                    Parent root = (Parent) loader.load();
                    OrderManagementController controller = ((OrderManagementController) loader.getController());
                    OrderFactory orderFactory = new OrderFactory();
                    OrderInterface orderImplementation = orderFactory.getImplementation();
                    controller.setOrderImplementation(orderImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuProducts.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/product.fxml"));
                    Parent root = (Parent) loader.load();
                    ProductController controller = ((ProductController) loader.getController());
                    ProductFactory productFactory = new ProductFactory();
                    ProductInterface productImplementation = productFactory.getImplementation();
                    controller.setProductImplementation(productImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuCompanies.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/companies.fxml"));
                    Parent root = (Parent) loader.load();
                    CompanyController controller = ((CompanyController) loader.getController());
                    CompanyFactory companyFactory = new CompanyFactory();
                    CompanyInterface companyImplementation = companyFactory.getImplementation();
                    controller.setImplementation(companyImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
