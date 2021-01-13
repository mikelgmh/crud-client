package crudclient.controllers;

import crudclient.client.CompanyRESTClient;
import crudclient.model.Company;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import crudclient.model.CompanyType;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Iker de la Cruz
 */
public class CompanyController {

    private static final Logger logger = Logger.getLogger("crudclient.controllers.CompanyController");
    private Stage stage;
    private ObservableList<Company> companyData;

    @FXML
    private Button btnFilter;
    @FXML
    private Button btnCreateCompany;
    @FXML
    private Button btnModifyCompany;
    @FXML
    private Button btnDeleteCompany;
    @FXML
    private TextField tfNameFilter;
    @FXML
    private ComboBox<CompanyType> cbTypeFilter;
    @FXML
    private TextField tfLocalizationFilter;
    @FXML
    private TableView tableViewCompanies;
    @FXML
    private TableColumn tcIdCompany;
    @FXML
    private TableColumn tcNameCompany;
    @FXML
    private TableColumn tcTypeCompany;
    @FXML
    private TableColumn tcLocalizationCompany;

    public CompanyController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Method to initialize the Company stage.
     *
     * @param root The loaded view.
     */
    public void initStage(Parent root) {
        logger.log(Level.INFO, "Loading the SignIn stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Companies");
        stage.setResizable(true);
        stage.setOnShowing(this::handleWindowShowing);
        // Set handlers
        tfNameFilter.textProperty().addListener(this::handleFilterButton);
        tfLocalizationFilter.textProperty().addListener(this::handleFilterButton);
        stage.show();
        logger.log(Level.INFO, "SignIn stage loaded.");
    }

    /**
     * Method to initialize the stage's components.
     *
     * @param event
     */
    private void handleWindowShowing(WindowEvent event) {
        btnFilter.setDisable(true);
        btnCreateCompany.setDisable(false);
        btnModifyCompany.setDisable(true);
        btnDeleteCompany.setDisable(true);
        tfNameFilter.clear();
        // FIXME: Fix the IndexOutOfBoundsException error.
        // Get the Company types and set into the choicebox.
        cbTypeFilter.getItems().add(null);
        cbTypeFilter.getItems().addAll(FXCollections.observableArrayList(CompanyType.values()));
        tfLocalizationFilter.clear();
        // Set the factories to the column values of the table.
        tcIdCompany.setCellValueFactory(new PropertyValueFactory("id"));
        tcNameCompany.setCellValueFactory(new PropertyValueFactory("name"));
        tcTypeCompany.setCellValueFactory(new PropertyValueFactory("type"));
        tcLocalizationCompany.setCellValueFactory(new PropertyValueFactory("localization"));
        // TODO: Load the data from the database into the TableView.
        /*try {
            CompanyRESTClient rest = new CompanyRESTClient();
            companyData = FXCollections.observableArrayList();
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
        // Load data into the table view.
        Company company = new Company(1, "Iker", CompanyType.ADMIN, "Berriz");
        Company company2 = new Company(2, "Aketza", CompanyType.CLIENT, "Tartanga");
        companyData = FXCollections.observableArrayList(company, company2);
        tableViewCompanies.setItems(companyData);
    }

    /**
     * Combo box change event handler. It validates the name, type or
     * localization fields has any content to enable/disable the filter button.
     *
     * @param event
     */
    @FXML
    private void handleFilterButtonComboBox(ActionEvent event) {
        if (!tfNameFilter.getText().trim().isEmpty()
                || !tfLocalizationFilter.getText().trim().isEmpty()
                || !(cbTypeFilter.getValue() == null)) {
            btnFilter.setDisable(false);
        } else {
            btnFilter.setDisable(true);
        }
    }

    /**
     * Name and localization text changed event. It validates the name, type or
     * localization fields has any content to enable/disable the filter button.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handleFilterButton(ObservableValue observable, String oldValue, String newValue) {
        if (!tfNameFilter.getText().trim().isEmpty()
                || !tfLocalizationFilter.getText().trim().isEmpty()
                || !(cbTypeFilter.getValue() == null)) {
            btnFilter.setDisable(false);
        } else {
            btnFilter.setDisable(true);
        }
    }

}
