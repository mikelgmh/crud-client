/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.factories.SignInFactory;
import crudclient.factories.UserFactory;
import crudclient.interfaces.SignInInterface;
import crudclient.interfaces.UserInterface;
import crudclient.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author 2dam
 */
public class MenuController implements Initializable {

    @FXML
    private HBox hboxMenu;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuHome;
    @FXML
    private MenuItem menuLogout;
    @FXML
    private MenuItem menuClose;
    @FXML
    private Menu menuManagement;
    @FXML
    private MenuItem menuCompanies;
    @FXML
    private MenuItem menuUsers;
    @FXML
    private MenuItem menuOrders;
    @FXML
    private MenuItem menuProducts;

    
    private Stage stage;

    private User user;

    public MenuController() {
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuLogout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sign Out");
                String s = "Are you sure you want to logout?";
                alert.setContentText(s);
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/SignIn.fxml"));
                        Parent root = (Parent) loader.load();
                        SignInController controller = ((SignInController) loader.getController());
                        SignInFactory signInFactory = new SignInFactory();
                        SignInInterface signInImplementation = signInFactory.getImplementation();
                        controller.setImplementation(signInImplementation);
                        controller.setStage(new Stage());
                        controller.initStage(root);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        menuUsers.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/UserManagement.fxml"));
                        Parent root = (Parent) loader.load();
                        UserManagementController controller = ((UserManagementController) loader.getController());
                        UserFactory userFactory = new UserFactory();
                        UserInterface userImplementation = userFactory.getUserImplementation(UserFactory.ImplementationType.REST_CLIENT);
                        controller.setUserImplementation(userImplementation);
                        controller.setStage(new Stage());
                        controller.setCurrentUser(user);
                        controller.initStage(root);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        });
    }

}
