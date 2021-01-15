/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.interfaces.UserInterface;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Mikel
 */
public class UserCreationController {

    private Stage stage;
    private static final Logger logger = Logger.getLogger("signupsignin.controllers.SignUpController");
    private UserInterface userImplementation;

    @FXML
    private TextField txt_name;
    @FXML
    private TextField txt_surname;
    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_company;
    @FXML
    private ChoiceBox chb_status;
    @FXML
    private ChoiceBox chb_privilege;

    public UserCreationController() {
    }
    
    
    
     public void initStage(Parent parent) {

        // Sets the default hint behavior

        logger.log(Level.INFO, "Loading window...");
        // Creates a scena and a stage and opens the window.
        Scene scene = new Scene(parent);
        Stage stage = new Stage();


        // Set stage
        this.setStage(stage);

        // Set some properties of the stage
        stage.setScene(scene);
        stage.setTitle("User Creation"); // Sets the title of the window
        stage.setResizable(false); // Prevents the user to resize the window.
        scene.getStylesheets().add(getClass().getResource("/crudclient/view/styles/inputStyle.css").toExternalForm()); // Imports the CSS file used for errors in some inputs.

        // Set the default values for some cells
        stage.show(); // Show the stage

        // PRUEBAS DE ENCRIPTAR Y RECIBIR LA CLAVE PÚBLICA DEL SERVIDOR
//        String prueba = this.userImplementation.getPublicKey();
//        AsymmetricEncryption enc = new AsymmetricEncryption(prueba);
//        String encryptedString = enc.encryptString("EEEE");
//        System.out.println(encryptedString);
//        System.out.println(prueba);
    }
    

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public UserInterface getUserImplementation() {
        return userImplementation;
    }

    public void setUserImplementation(UserInterface userImplementation) {
        this.userImplementation = userImplementation;
    }

    
    
    
}
