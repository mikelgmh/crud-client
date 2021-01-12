package crudclient.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Iker de la Cruz
 */
public class CompanyController {

    private static final Logger logger = Logger.getLogger("crudclient.controllers.CompanyController");
    private Stage stage;

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
    private ChoiceBox cbTypeFilter;
    @FXML
    private TextField tfLocalizationFilter;
    @FXML
    private TableView tableViewCompanies;

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
        // TODO: Load the items from the database.
        // cbTypeFilter.getItems().addAll();
        tfLocalizationFilter.clear();
        // TODO: Load the data from the database into the TableView.
    }

}
