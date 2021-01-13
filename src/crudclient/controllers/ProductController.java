/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.model.Product;
import crudclient.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;

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
    private TableColumn<Product, Boolean> tc_Checkbox;
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

    private final List<Product> products = new ArrayList<>();

    private TableView<ObservableList<StringProperty>> table = new TableView<>();
    private ArrayList<String> myList = new ArrayList<>();
    /**
     * Initializes the controller class.
     */
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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
        
        tabla();
        tableInfo();
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
            // System.out.println("Nuevo Nombre: " + data.getNewValue());
            // System.out.println("Antiguo Nombre: " + data.getOldValue());
            Product p = data.getRowValue();
            p.setName(data.getNewValue());
        });
        //tc_Weight.setCellFactory(TextFieldTableCell.forTableColumn());
        // TableColumn<Product, Float> tc_Weight = new TableColumn<>("Weight");
        tc_Weight.setCellValueFactory(new PropertyValueFactory<Product, Float>("weight"));
        FloatStringConverter converterFloat = new FloatStringConverter();
        tc_Weight.setCellFactory(TextFieldTableCell.<Product, Float>forTableColumn(converterFloat));
        tc_Weight.setOnEditCommit(data -> {
            data.getRowValue().setWeight(data.getNewValue());
        });

        tc_Price.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        DoubleStringConverter converterDouble = new DoubleStringConverter();
        tc_Price.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(converterDouble));
        tc_Price.setOnEditCommit(data -> {
            data.getRowValue().setPrice(data.getNewValue());
        });

        products.forEach((p) -> {
            tv_Tabla.getItems().add(p);
        });
    }

    private void tableInfo() {
        Product product = new Product();
        User user = new User();
        product.setId(1);
        product.setName("Aketza");
        product.setWeight(50);
        product.setPrice(300);
        product.setUser(user);
        ObservableList<Product> info = FXCollections.observableArrayList(product);
        tv_Tabla.setItems(info);

    }

    @FXML
    private void handleOnClickCreate(ActionEvent event) throws IOException {
           // get current position
           TablePosition pos = table.getFocusModel().getFocusedCell();

           // clear current selection
           table.getSelectionModel().clearSelection();

           // create new record and add it to the model
           Product product = new Product();
           tv_Tabla.getItems().add( product);
          
           // get last row
           int row = table.getItems().size() - 1;
           table.getSelectionModel().select( row, pos.getTableColumn());

           // scroll to new row
           tv_Tabla.scrollTo( product);

       }
     @FXML
    private void handleOnClickDelete(ActionEvent event) throws IOException {
         

    }

    @FXML
    private void handleOnClickModify(ActionEvent event) {
    }

    @FXML
    private void handleOnClickFilter(ActionEvent event) {
    }
    }

