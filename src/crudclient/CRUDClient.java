/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient;

import crudclient.controllers.ProductController;
import java.io.IOException;
import java.util.logging.Level;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author 2dam
 */
public class CRUDClient extends Application {
    
    @Override
    public void start(Stage primaryStage) {
          try {
          //  LOG.log(Level.INFO, "Iniciando la ventana");
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/product.fxml"));
            //LOG.log(Level.INFO, "Cargando Parent");
            Parent root = (Parent) loader.load();
            //LOG.log(Level.INFO, "Cargando controller");
              ProductController controller = ((ProductController) loader.getController());
            //LOG.log(Level.INFO, "Iniciando controller");
            controller.setStage(primaryStage);
            controller.initStage(root);
        } catch (IOException ex) {
            //LOG.log(Level.SEVERE, "Se ha producido un error de E/S");

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
