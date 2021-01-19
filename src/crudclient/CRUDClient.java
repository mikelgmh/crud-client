package crudclient;

import crudclient.controllers.CompanyController;
import crudclient.controllers.SignInController;
import crudclient.factories.CompanyFactory;
import crudclient.interfaces.CompanyInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 *
 * @author 2dam
 */
public class CRUDClient extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/companies.fxml"));
        Parent root = (Parent) loader.load();
        CompanyController controller = ((CompanyController) loader.getController());
        CompanyFactory companyFactory = new CompanyFactory();
        CompanyInterface companyImplementation = companyFactory.getImplementation();
        controller.setImplementation(companyImplementation);
        controller.setStage(primaryStage);
        controller.initStage(root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
