package crudclient;

import crudclient.controllers.OrderManagementController;
import crudclient.controllers.UserManagementController;
import crudclient.factories.OrderFactory;
import crudclient.factories.UserFactory;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.logging.Level;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import crudclient.interfaces.OrderInterface;
import crudclient.interfaces.UserInterface;
import java.util.logging.Logger;

/**
 *
 * @author 2dam
 */
public class CRUDClient extends Application {

    
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
