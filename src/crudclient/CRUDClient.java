package crudclient;

import crudclient.controllers.OrderManagementController;
import crudclient.factories.OrderFactory;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import crudclient.interfaces.OrderInterface;

/**
 *
 * @author 2dam
 */
public class CRUDClient extends Application {

    private static final Logger logger = Logger.getLogger("signupsignin.SignUpSignInClient");

    /**
     * The start method in which the JavaFX starts.
     *
     * @param stage the first stage of the JavaFX application.
     * @throws Exception if there ocurred something wrong creating the first
     * stage.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/orders.fxml"));
        Parent root = (Parent) loader.load();
        OrderManagementController controller = ((OrderManagementController) loader.getController());
        OrderFactory orderFactory = new OrderFactory();
        OrderInterface order = orderFactory.getImplementation();
        controller.setOrderImplementation(order);
        controller.initStage(root);
    }

    /**
     * The main method in which the program starts.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.log(Level.INFO, "Initializing the program.");
        launch(args);
    }

}
