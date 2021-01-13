/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient;

import crudclient.controllers.UserManagementController;
import crudclient.factories.UserFactory;
import java.util.logging.Logger;
import javafx.application.Application;
import java.util.logging.Level;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import crudclient.interfaces.UserInterface;

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
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/UserManagement.fxml"));
        Parent root = (Parent) loader.load();
        UserManagementController controller = ((UserManagementController) loader.getController());
        UserFactory userFactory = new UserFactory();
        UserInterface user = userFactory.getUserImplementation(UserFactory.ImplementationType.TEST_IMPLEMENTATION);
        controller.setUserImplementation(user);
        controller.setStage(stage);
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