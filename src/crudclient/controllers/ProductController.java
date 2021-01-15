/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.client.ProductRESTClient;
import crudclient.interfaces.ProductInterface;
import crudclient.model.Company;
import crudclient.model.Product;
import crudclient.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javax.ws.rs.core.GenericType;

/**
 * FXML Controller class
 *
 * @author 2dam
 */
public class ProductController {

    private Stage stage;
    @FXML
    private TableView<Product> tv_Tabla;
   // @FXML
   // private TableColumn<Product, Integer> tc_Id;
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
    private Button btn_OrderCreate;
    @FXML
    private Button btn_Filter;
   /* @FXML
    private TextField tf_Id;
    @FXML
    private TextField tf_Name;
    @FXML
    private TextField tf_Weight;
    @FXML
    private TextField tf_Price;*/
    @FXML
    private Label lbl_Choose;
    @FXML
    private ComboBox<Company> combo_Company;    
    private static final Logger LOG = Logger.getLogger(ProductController.class.getName());
    private TableView<ObservableList<StringProperty>> table = new TableView<>();
    private ObservableList<Product> pr;
    private ProductInterface productImplementation;

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
        LOG.log(Level.INFO, "Inicio ");
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Product Managment");
        stage.setResizable(false);
        LOG.log(Level.INFO, "Stage ");
        /*En los dos labels se muestran datos que hemos recibido desde el lado
        servidor*/
       /* tf_Id.setTooltip(new Tooltip("ID of the product"));
        tf_Name.setTooltip(new Tooltip("Name of the product"));
        tf_Weight.setTooltip(new Tooltip("Weight of the product"));
        tf_Price.setTooltip(new Tooltip("Price of the product"));*/
        btn_Create.setTooltip(new Tooltip("Create a new product"));
        btn_Delete.setTooltip(new Tooltip("Delete a product"));
        btn_Delete.setOnAction(this::handleOnClickDelete);
        btn_OrderCreate.setTooltip(new Tooltip("Create an order"));
        btn_Filter.setTooltip(new Tooltip("Create an order"));
        //this.pr = FXCollections.observableSet(getProductImplementation().findAllProducts_XML(new GenericType<Set<Product>>() { }));
        tv_Tabla.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        LOG.log(Level.INFO, "Tooltip ");
        tabla();
        LOG.log(Level.INFO, "tabla ");
        tableInfo();
        LOG.log(Level.INFO, "tablainfo ");
        stage.show();

    }

    private void tabla() {
        tv_Tabla.setEditable(true);

       // tc_Id.setCellValueFactory(new PropertyValueFactory<>("id"));

        tc_Name.setCellValueFactory(new PropertyValueFactory<>("name"));

        tc_Weight.setCellValueFactory(new PropertyValueFactory<>("weight"));

        tc_Price.setCellValueFactory(new PropertyValueFactory<>("price"));

        tc_Name.setCellFactory(TextFieldTableCell.forTableColumn());
        tc_Name.setOnEditCommit((TableColumn.CellEditEvent<Product, String> data) -> {
            LOG.log(Level.SEVERE, "New Name: ", data.getNewValue());
            // System.out.println("Nuevo Nombre: " + data.getNewValue());
            // System.out.println("Antiguo Nombre: " + data.getOldValue());
            LOG.log(Level.SEVERE, "Old Name: ", data.getOldValue());
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

    }

    private void tableInfo() {

        LOG.log(Level.INFO, "pr ");
        ProductRESTClient rest = new ProductRESTClient();
        pr = FXCollections.observableArrayList(rest.findAllProducts_XML(new GenericType<List<Product>>() {
        }));

        tv_Tabla.setItems(pr);
        // this.pr = FXCollections.observableSet(getProductImplementation().findAllProducts_XML(new GenericType<List<Product>>() {
        //}));
        //this.table.setItems(pr);
        /*  Product product = new Product();
        User user = new User();
        product.setId(1);
        product.setName("Aketza");
        product.setWeight(50);
        product.setPrice(300);
        product.setUser(user);
        ObservableList<Product> info = FXCollections.observableArrayList(product);
        tv_Tabla.setItems(info);*/

    }

    @FXML
    private void handleOnClickCreate(ActionEvent event) {
        // get current position
        TablePosition pos = table.getFocusModel().getFocusedCell();

        // clear current selection
        table.getSelectionModel().clearSelection();

        // create new record and add it to the model
        Product product = new Product();
        tv_Tabla.getItems().add(product);

        // get last row
        int row = table.getItems().size() - 1;
        table.getSelectionModel().select(row, pos.getTableColumn());

        // scroll to new row
        tv_Tabla.scrollTo(product);

    }

    @FXML
    private void handleOnClickDelete(ActionEvent event) {
        btn_Delete.setDisable(false);
        //tv_Tabla.getItems().remove(tv_Tabla.getSelectionModel().getSelectedItem());
        ProductRESTClient rest = new ProductRESTClient();
        rest.remove(tv_Tabla.getSelectionModel().getSelectedItem().getId().toString());
        tableInfo();
    }

    @FXML
    private void handleOnClickFilter(ActionEvent event) {
    }

    @FXML
    private void handleOnClickCreateOrder(ActionEvent event) {
        List<String> names = tv_Tabla.getSelectionModel().getSelectedItems().stream()
                .map(Product::getName)
                .collect(Collectors.toList());
    }

    public void setProductImplementation(ProductInterface product) {
        this.productImplementation = product;
    }

    public ProductInterface getProductImplementation() {
        return this.productImplementation;
    }
}
