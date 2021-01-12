/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.model.Product;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sun.rmi.runtime.Log;

/**
 * FXML Controller class
 *
 * @author 2dam
 */
public class ProductController {

    private Stage stage;
    @FXML
    private TableView<Product> tv_Tabla;
    @FXML
    private TableColumn<Product, Integer> tc_Id;
    @FXML
    private TableColumn<Product, String> tc_Name;
    @FXML
    private TableColumn<Product, Float> tc_Weight;
    @FXML
    private TableColumn<Product, Double> tc_Price;
    @FXML
    private Button btn_Create;
    @FXML
    private Button btn_Delete;
    @FXML
    private Button btn_Modify;
    @FXML
    private Button btn_Filter;
    @FXML
    private TextField tf_Id;
    @FXML
    private TextField tf_Name;
    @FXML
    private TextField tf_Weight;
    @FXML
    private TextField tf_Price;

    /**
     * Initializes the controller class.
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.setResizable(false);

        /*En los dos labels se muestran datos que hemos recibido desde el lado
        servidor*/
        tf_Id.setTooltip(new Tooltip("ID of the product"));
        tf_Name.setTooltip(new Tooltip("Name of the product"));
        tf_Weight.setTooltip(new Tooltip("Weight of the product"));
        tf_Price.setTooltip(new Tooltip("Price of the product"));
        btn_Create.setTooltip(new Tooltip("Create a new product"));
        btn_Modify.setTooltip(new Tooltip("Modify a product"));
        btn_Delete.setTooltip(new Tooltip("Delete a product"));
        btn_Filter.setTooltip(new Tooltip("Filter a product"));
        //stage.onCloseRequestProperty().set(this::handleCloseRequest);
        stage.show();
    }

    private void tabla() {
        tv_Tabla.setEditable(true);
        tc_Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tc_Weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        tc_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        tc_Name.setCellFactory(TextFieldTableCell.forTableColumn());
        tc_Name.setOnEditCommit((TableColumn.CellEditEvent<Product, String> data) -> {
            System.out.println("Nuevo Nombre: " + data.getNewValue());
            System.out.println("Antiguo Nombre: " + data.getOldValue());
            Product p = data.getRowValue();
            p.setName(data.getNewValue());
        });
        
    }
}
