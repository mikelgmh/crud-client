/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.factories.SignInFactory;
import crudclient.interfaces.EmailServiceInterface;
import crudclient.interfaces.SignInInterface;
import crudclient.model.User;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Iker de la Cruz
 */
public class DashboardController {

    private static final Logger logger = Logger.getLogger("crudclient.controllers.DashboardController");
    private Stage stage;
    private User user;
    @FXML
    private Label lbl_Welcome;
    @FXML
    private Label lbl_Connection;
    @FXML
    private Button btn_Logout;
    @FXML
    private MenuController menuController;

    public DashboardController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Method to initialize the RecoverPassword stage.
     *
     * @param root The loaded view.
     */
    public void initStage(Parent root) {
        logger.log(Level.INFO, "Loading the Dashboard stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        menuController = new MenuController();
        menuController.setUser(user);
        stage.setTitle("Dashboard");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        stage.show();
        logger.log(Level.INFO, "Dashboard stage loaded.");
    }

    private void handleWindowShowing(WindowEvent event) {
        // Output example with the user's information
        lbl_Welcome.setText(user.getEmail() + "\n" + user.getPassword() + "\n" + user.getName() + "\n" + user.getSurname() + "\n" + user.getUsername());
        // Output the last access date
        lbl_Connection.setText(dateFormat(user.getLastAccess()));
    }

    @FXML
    private void handleOnClickLogout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        String s = "Are you sure you want to logout?";
        alert.setContentText(s);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/SignIn.fxml"));
            Parent root = (Parent) loader.load();
            SignInController controller = ((SignInController) loader.getController());
            SignInFactory signInFactory = new SignInFactory();
            SignInInterface signInImplementation = signInFactory.getImplementation();
            controller.setImplementation(signInImplementation);
            controller.setStage(new Stage());
            controller.initStage(root);
            stage.close();
        }
    }

    private String dateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    public MenuController getMenuManagementController() {
        return menuController;
    }

    public void setMenuManagementController(MenuController menuController) {
        this.menuController = menuController;
    }

}
