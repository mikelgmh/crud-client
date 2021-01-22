/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.client.CompanyRESTClient;
import crudclient.client.ProductRESTClient;
import crudclient.interfaces.CompanyInterface;
import crudclient.interfaces.ProductInterface;
import crudclient.model.Company;
import crudclient.model.Product;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
    @FXML
    private TableColumn<Product, String> tc_Name;
    @FXML
    private TableColumn<Product, Float> tc_Weight;
    @FXML
    private TableColumn<Product, Float> tc_Price;
    @FXML
    private Button btn_Create;
    @FXML
    private Button btn_Delete;
    @FXML
    private Button btn_OrderCreate;
    @FXML
    private Label lbl_Choose;
     @FXML
    private TextField tf_company;
    private static final Logger LOG = Logger.getLogger(ProductController.class.getName());
    private TableView<ObservableList<StringProperty>> table = new TableView<>();
    private ObservableList<Product> pr;
    private ObservableList<Company> co;
    private ProductInterface productImplementation;
    private OrderManagementController orderManagementController ;


    public OrderManagementController getOrdermanagementController() {
        return orderManagementController;
    }

    public void setOrdermanagementController(OrderManagementController ordermanagementController) {
        this.orderManagementController = ordermanagementController;
    }
   
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
        
        btn_Create.setTooltip(new Tooltip("Create a new product"));
        
        btn_Delete.setTooltip(new Tooltip("Delete a product"));
        
        btn_Delete.setOnAction(this::handleOnClickDelete);
        
        btn_OrderCreate.setTooltip(new Tooltip("Create an order"));
        
        tf_company.textProperty().addListener(this::handleTextChange);
        
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
    
    public void initStageCreateOrder(Parent root) {
        LOG.log(Level.INFO, "Inicio Creado Orden ");
        Scene scene = new Scene(root);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Create Order - Products");
        stage.setResizable(false);
        LOG.log(Level.INFO, "Stage Create Order");
        
        btn_Delete.setVisible(false);
        btn_Create.setVisible(false);
        btn_OrderCreate.setOnAction(this::handleOnClickCreateOrder);
        btn_OrderCreate.setTooltip(new Tooltip("Create an order"));
        
        tf_company.textProperty().addListener(this::handleTextChange);
        
        tv_Tabla.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        
        
        LOG.log(Level.INFO, "tabla ");
        
        tableInfo();
        
        LOG.log(Level.INFO, "tablainfo ");
        //Inicializar la variable en ordermanagement?
       
        stage.showAndWait();
    }

    private void tabla() {
        tv_Tabla.setEditable(true);

        tc_Name.setCellValueFactory(new PropertyValueFactory<>("name"));

        tc_Weight.setCellValueFactory(new PropertyValueFactory<>("weight"));

        tc_Price.setCellValueFactory(new PropertyValueFactory<>("price"));

        tc_Name.setCellFactory(TextFieldTableCell.forTableColumn());
        tc_Name.setOnEditCommit((TableColumn.CellEditEvent<Product, String> data) -> {
            LOG.log(Level.SEVERE, "New Name: {0}", data.getNewValue());
            LOG.log(Level.SEVERE, "Old Name: {0}", data.getOldValue());
            if (!Pattern.matches("[a-zA-Z]+", data.getNewValue())) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Look, a Warning Dialog");
                alert.setContentText("Careful with the next step!");

                alert.showAndWait();
                tv_Tabla.refresh();

            } else {
            Product p = data.getRowValue();
            ProductRESTClient rest = new ProductRESTClient();
            p.setName(data.getNewValue());
            rest.edit_XML(p);
            tableInfo();
            }
        });

        tc_Weight.setCellValueFactory(new PropertyValueFactory<Product, Float>("weight"));
        FloatStringConverter converterFloat = new FloatStringConverter();
        tc_Weight.setCellFactory(TextFieldTableCell.<Product, Float>forTableColumn(converterFloat));
        tc_Weight.setOnEditCommit(data -> {
       /*     if (!Pattern.matches("[a-zA-Z0-9]+", data.getNewValue().toString())) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Look, a Warning Dialog");
                alert.setContentText("Careful with the next step!");
                alert.showAndWait();
                tv_Tabla.refresh();
            } else {*/
            data.getRowValue().setWeight(data.getNewValue());
          //  }
        });

        tc_Price.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));
        tc_Price.setCellFactory(TextFieldTableCell.<Product, Float>forTableColumn(converterFloat));
        tc_Price.setOnEditCommit(data -> {
            
           /*   if (!Pattern.matches("[a-zA-Z0-9]+", data.getNewValue().toString())) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Look, a Warning Dialog");
                alert.setContentText("Careful with the next step!");
                alert.showAndWait();
                tv_Tabla.refresh();
            } else {*/
            data.getRowValue().setPrice(data.getNewValue());
            //  }
        });
    }
    

    

    private void tableInfo() {
        LOG.log(Level.INFO, "pr ");
        //ProductRESTClient rest = new ProductRESTClient();
        //pr =FXCollections.observableArrayList(getProductImplementation().findAllProducts_XML(new GenericType<List<Product>>));
        pr = FXCollections.observableArrayList(getProductImplementation().findAllProducts_XML(new GenericType<List<Product>>() {
        }));
        tv_Tabla.setItems(pr);
    }

    
    private void edit() {

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
        tv_Tabla.getFocusModel().focus(pos);
        tv_Tabla.requestFocus();
        //table.getSelectionModel().select(row, pos.getTableColumn());
        // tv_Tabla.getFocusModel().focus(row, tableColumn);
        // tv_Tabla.requestFocus();
        // scroll to new row
        tv_Tabla.scrollTo(product);
    }

    @FXML
    private void handleOnClickDelete(ActionEvent event) {
        btn_Delete.setDisable(false);
        ProductRESTClient rest = new ProductRESTClient();
        rest.remove(tv_Tabla.getSelectionModel().getSelectedItem().getId().toString());
        tableInfo();
    }

    
      private void handleTextChange(ObservableValue observable, String oldValue, String newValue) {
        FilteredList<Product> filteredData = new FilteredList<>(pr, u -> true);
        
        
        filteredData.setPredicate(product -> {
            if (newValue == null || newValue.isEmpty()) {
                return true; // Si el texto del filtro está vacío, muestra todos los utensilios.
            }

            // Cojo el valor del campo de busqueda y lo pongo en minuscula.
            String lowerCaseFilter = newValue.toLowerCase();

            if (product.getUser().getCompany().getName().toLowerCase().contains(lowerCaseFilter)) {
                tableOrder();
                return true; // Filtrado por nombre.
            }

            return false; // No existen resultados.
        });
        // Envuelve la FilteredList en una SortedList.
        SortedList<Product> sortedData = new SortedList<>(filteredData);

        // Vincula el comparador SortedList al comparador TableView.
        sortedData.comparatorProperty().bind(tv_Tabla.comparatorProperty());

        // Agrega datos ordenados (y filtrados) a la tabla.
        tv_Tabla.setItems(sortedData);
    }

    
    @FXML
    private void handleOnClickCreateOrder(ActionEvent event) {
        List<Product> products = tv_Tabla.getSelectionModel().getSelectedItems();
        orderManagementController.setProducts(products);
        
        stage.close();
    }

    
    public void setProductImplementation(ProductInterface product) {
        this.productImplementation = product;
    }

    
    public ProductInterface getProductImplementation() {
        return this.productImplementation;
    }

    private void tableOrder() {
        tc_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tc_Weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        tc_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        pr = FXCollections.observableArrayList(getProductImplementation().findAllProducts_XML(new GenericType<List<Product>>() {
        }));
        tv_Tabla.setItems(pr);
        
    }
    
  
}
