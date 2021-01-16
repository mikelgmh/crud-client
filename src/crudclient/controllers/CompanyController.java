package crudclient.controllers;

import crudclient.client.CompanyRESTClient;
import crudclient.interfaces.CompanyInterface;
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
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.ws.rs.core.GenericType;

/**
 * The controller for the companies view.
 *
 * @author Iker de la Cruz
 */
public class CompanyController {

    private static final Logger logger = Logger.getLogger("crudclient.controllers.CompanyController");
    private Stage stage;
    private ObservableList<Company> companyData;
    private CompanyInterface companyImplementation;
    @FXML
    private Button btnFilter;
    @FXML
    private Button btnCreateCompany;
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
        logger.log(Level.INFO, "Loading the Companies stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Companies");
        stage.setResizable(true);
        stage.setOnShowing(this::handleWindowShowing);
        // Set handlers
        tfNameFilter.textProperty().addListener(this::handleFilterButton);
        tfLocalizationFilter.textProperty().addListener(this::handleFilterButton);
        tableViewCompanies.getSelectionModel().selectedItemProperty()
                .addListener(this::handleCompaniesTableSelectionChanged);
        stage.show();
        logger.log(Level.INFO, "Companies stage loaded.");
    }

    /**
     * Method to initialize the stage's components.
     *
     * @param event
     */
    private void handleWindowShowing(WindowEvent event) {
        btnFilter.setDisable(true);
        btnCreateCompany.setDisable(false);
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
        // Load the data from the database into the TableView.
        try {
            companyData = FXCollections.observableArrayList(companyImplementation.findAllCompanies_XML(new GenericType<List<Company>>() {
            }));
        } catch (Exception ex) {
            // Change the default message in the tableview with an error.
            tableViewCompanies.setPlaceholder(new Label(ex.getMessage()));
        }
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

    /**
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handleCompaniesTableSelectionChanged(ObservableValue observable,
            Object oldValue, Object newValue) {
        if (newValue != null) {
            btnDeleteCompany.setDisable(false);
        }
    }

    /**
     * Method to create a new row in the TableView.
     *
     * @param event
     */
    @FXML
    private void createCompanyAction(ActionEvent event) {
        // TODO: Autoincrementar el id al añadir nueva fila.
        tableViewCompanies.getItems().add(new Company());
    }

    /**
     * Method to delete the selected Company in the TableView and the database.
     *
     * @param event
     */
    @FXML
    private void deleteCompanyAction(ActionEvent event) {
        tableViewCompanies.getItems().remove(tableViewCompanies.getSelectionModel().getSelectedItem());
        // TODO: Borrar la compañia borrada en la base de datos.
        tableViewCompanies.refresh();
    }

    /**
     *
     * @return
     */
    public CompanyInterface getCompanyImplementation() {
        return companyImplementation;
    }

    /**
     *
     * @param companyImplementation
     */
    public void setCompanyImplementation(CompanyInterface companyImplementation) {
        this.companyImplementation = companyImplementation;
    }

    /**
     *
     * @param companyInterface
     */
    public void setImplementation(CompanyInterface companyInterface) {
        this.companyImplementation = companyInterface;
    }

}
