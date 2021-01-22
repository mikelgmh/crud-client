package crudclient;

import crudclient.controllers.CompanyController;
import crudclient.controllers.SignInController;
import crudclient.factories.CompanyFactory;
import crudclient.factories.SignInFactory;
import crudclient.interfaces.CompanyInterface;
import crudclient.interfaces.SignInInterface;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/SignIn.fxml"));
        Parent root = (Parent) loader.load();
        SignInController controller = ((SignInController) loader.getController());
        SignInFactory signInFactory = new SignInFactory();
        SignInInterface signInImplementation = signInFactory.getImplementation();
        controller.setImplementation(signInImplementation);
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
