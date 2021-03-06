package crudclient.controllers;

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
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
    private TableView<Company> tableViewCompanies;
    @FXML
    private TableColumn<Company, String> tcNameCompany;
    @FXML
    private TableColumn<Company, CompanyType> tcTypeCompany;
    @FXML
    private TableColumn<Company, String> tcLocalizationCompany;

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
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        tableViewCompanies.getSelectionModel().selectedItemProperty()
                .addListener(this::handleCompaniesTableSelectionChanged);
        // setOnEditCommit handlers for the columns
        // TODO: Crear css para el cambio de colores en la tabla
        tcNameCompany.setOnEditCommit(
                (CellEditEvent<Company, String> t) -> {
                    ((Company) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setName(t.getNewValue().trim());
                    // If all the field are filled, create the Company in the database
                    Company newCompany = tableViewCompanies.getSelectionModel().getSelectedItem();
                    if (isCompanyDataFilled(newCompany)) {
                        getCompanyImplementation().edit_XML(newCompany);
                    }
                });
        tcTypeCompany.setOnEditCommit(
                (CellEditEvent<Company, CompanyType> t) -> {
                    ((Company) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setType(t.getNewValue());
                    // If all the field are filled, create the Company in the database
                    Company newCompany = tableViewCompanies.getSelectionModel().getSelectedItem();
                    if (isCompanyDataFilled(newCompany)) {
                        getCompanyImplementation().edit_XML(newCompany);
                    }
                });
        tcLocalizationCompany.setOnEditCommit(
                (CellEditEvent<Company, String> t) -> {
                    ((Company) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())).setLocalization(t.getNewValue().trim());
                    // If all the field are filled, create the Company in the database
                    Company newCompany = tableViewCompanies.getSelectionModel().getSelectedItem();
                    if (isCompanyDataFilled(newCompany)) {
                        getCompanyImplementation().edit_XML(newCompany);
                    }
                });
        stage.show();
        logger.log(Level.INFO, "Companies stage loaded.");
    }

    /**
     * Method to initialize the stage's components.
     *
     * @param event
     */
    private void handleWindowShowing(WindowEvent event) {
        btnCreateCompany.setDisable(false);
        btnDeleteCompany.setDisable(true);
        tfNameFilter.clear();
        // FIXME: Fix the IndexOutOfBoundsException error.
        // Get the Company types and set into the choicebox.
        cbTypeFilter.getItems().add(null);
        cbTypeFilter.getItems().addAll(FXCollections.observableArrayList(CompanyType.values()));
        tfLocalizationFilter.clear();
        // Set the factories to the column values of the table.
        // Name
        tcNameCompany.setCellValueFactory(new PropertyValueFactory("name"));
        tcNameCompany.setCellFactory(TextFieldTableCell.<Company>forTableColumn());
        // Type
        tcTypeCompany.setCellValueFactory(new PropertyValueFactory("type"));
        tcTypeCompany.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(CompanyType.values())));
        // Localization
        tcLocalizationCompany.setCellValueFactory(new PropertyValueFactory("localization"));
        tcLocalizationCompany.setCellFactory(TextFieldTableCell.<Company>forTableColumn());
        // Load the data from the database into the TableView.
        loadTableView();
    }

    /**
     * Method to handle the delete button, to enable/disable the button.
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void handleCompaniesTableSelectionChanged(ObservableValue observable,
            Object oldValue, Object newValue) {
        if (newValue != null) {
            btnDeleteCompany.setDisable(false);
        } else {
            btnDeleteCompany.setDisable(true);
        }
    }

    /**
     * Method to create a new row in the TableView.
     *
     * @param event
     */
    @FXML
    private void createCompanyAction(ActionEvent event) {
        // Create the Company in the table and in the database with data
        // filled, to comprobate the CRUD is working fine.
        Company newCompany = new Company("Test", CompanyType.CLIENT, "Test");
        tableViewCompanies.getItems().add(newCompany);
        getCompanyImplementation().create_XML(newCompany);
        // Load again the table to load the Company's ids correctly
        loadTableView();
    }

    /**
     * Method to delete the selected Company in the TableView and the database.
     *
     * @param event
     */
    @FXML
    private void deleteCompanyAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete company");
        alert.setHeaderText("Company will be deleted");
        alert.setContentText("You will delete the selected company, are you sure?");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            // Delete selected Company in the database.
            Company delCompany = tableViewCompanies.getSelectionModel().getSelectedItem();
            getCompanyImplementation().remove(delCompany.getId().toString());
            // Delete selected Company in the TableView
            tableViewCompanies.getItems().remove(tableViewCompanies.getSelectionModel().getSelectedItem());
            // Load again the table to load the Company's ids correctly
            loadTableView();
        } else {
            event.consume();
            alert.close();
        }
    }

    /**
     * Method to verify if are the field in Company are not empty or null.
     *
     * @param newCompany The selected Company in the TableView.
     * @return
     */
    public boolean isCompanyDataFilled(Company newCompany) {
        // If there are all the Company data filled create the Company in the database
        return newCompany.getName() != null && newCompany.getType()
                != null && newCompany.getLocalization() != null
                && !newCompany.getName().trim().equals("")
                && !newCompany.getType().equals("")
                && !newCompany.getLocalization().trim().equals("");
    }

    /**
     * Method to load the TableView from the database.
     */
    public void loadTableView() {
        try {
            companyData = FXCollections.observableArrayList(getCompanyImplementation().findAllCompanies_XML(new GenericType<List<Company>>() {
            }));
        } catch (Exception ex) {
            // Change the default message in the tableview with an error.
            tableViewCompanies.setPlaceholder(new Label(ex.getMessage()));
        }
        tableViewCompanies.setItems(companyData);
    }

    /**
     * Handler to the close button of the window.
     *
     * @param event
     */
    private void handleCloseRequest(WindowEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Close confirmation");
        alert.setHeaderText("Application will be closed");
        alert.setContentText("You will close the application");
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            stage.close();
            Platform.exit();
        } else {
            event.consume();
            alert.close();
        }
    }

    /**
     * Method to return the Company implementation.
     *
     * @return The Company implementation.
     */
    public CompanyInterface getCompanyImplementation() {
        return companyImplementation;
    }

    /**
     * Method to set the Company implementation getting the Company interface.
     *
     * @param companyInterface
     */
    public void setImplementation(CompanyInterface companyInterface) {
        this.companyImplementation = companyInterface;
    }

}
