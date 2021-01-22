/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
