package crudclient.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Iker de la Cruz
 */
public class CompanyController {

    private static final Logger logger = Logger.getLogger("crudclient.controllers.CompanyController");
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public CompanyController() {
    }

    public void initStage(Parent root) {
        logger.log(Level.INFO, "Loading the SignIn stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Companies");
        stage.setResizable(true);
        stage.show();
        logger.log(Level.INFO, "SignIn stage loaded.");
    }
}
