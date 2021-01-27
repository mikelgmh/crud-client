/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.client.CompanyRESTClient;
import crudclient.client.ProductRESTClient;
import crudclient.factories.ProductFactory;
import crudclient.interfaces.CompanyInterface;
import crudclient.interfaces.ProductInterface;
import crudclient.model.Company;
import crudclient.model.Product;
import crudclient.model.User;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.application.Platform;
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
import javafx.scene.control.ButtonType;
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
import javafx.stage.WindowEvent;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javax.ws.rs.core.GenericType;

/**
 * FXML Controller class for Product
 *
 * @author Aketza
 */
public class ProductController {

    /**
     * The declaration of the stage.
     */
    private Stage stage;
    /**
     * The declaration of the table which is made of products.
     */
    @FXML
    private TableView<Product> tv_Tabla;
    /**
     * The declaration of the column of the table which is the name of the
     * product.
     */
    @FXML
    private TableColumn<Product, String> tc_Name;
    /**
     * The declaration of the column of the table which is the weight of the
     * product.
     */
    @FXML
    private TableColumn<Product, Float> tc_Weight;
    /**
     * The declaration of the column of the table which is the price of the
     * product.
     */
    @FXML
    private TableColumn<Product, Float> tc_Price;
    /**
     * The declaration of the button to create an order.
     */
    @FXML
    private Button btn_OrderCreate;
    /**
     * The declaration of the label to choose a company.
     */
    @FXML
    private Label lbl_Choose;
    /**
     * The declaration of the text field to write the name of the company.
     */
    @FXML
    private TextField tf_company;
    /**
     * The declaration of the create product button.
     */
    @FXML
    private Button btn_CreateProduct;
    /**
     * The declaration of the delete product button.
     */
    @FXML
    private Button btn_DeleteProduct;
    /**
     * The declaration of the logger.
     */
    private static final Logger LOG = Logger.getLogger(ProductController.class.getName());
    /**
     * The declaration of the tableview observableList.
     */
    private TableView<ObservableList<StringProperty>> table = new TableView<>();
    /**
     * The declaration of the observable list which is made of products.
     */
    private ObservableList<Product> productList;
    /**
     * The declaration of the product interface.
     */
    private ProductInterface productImplementation;
    /**
     * The declaration of the order managment controller.
     */
    private OrderManagementController orderManagementController;
    /**
     * The declaration of the current user.
     */
    private User currentUser;

    /**
     * The getter of the order managment controller.
     *
     * @return orderManagmentController
     */
    public OrderManagementController getOrdermanagementController() {
        return orderManagementController;
    }

    /**
     * The setter of the order managment controller.
     *
     * @param ordermanagementController
     */
    public void setOrdermanagementController(OrderManagementController ordermanagementController) {
        this.orderManagementController = ordermanagementController;
    }

    /**
     * Initializes the controller class.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * the setter of the stage.
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * The initial stage of the window.
     *
     * @param root
     */
    public void initStage(Parent root) {
        LOG.log(Level.INFO, "Inicio ");
        /**
         * Create a new scene.
         */
        Scene scene = new Scene(root);
        /**
         * Create a new stage.
         */
        stage = new Stage();
        /**
         * Set the scene to the stage.
         */
        stage.setScene(scene);
        /**
         * Set the title to the window.
         */
        stage.setTitle("Product Managment");
        /**
         * Disable the resizable property.
         */
        stage.setResizable(false);

        LOG.log(Level.INFO, "Stage ");
        /**
         * Set the text field invisible.
         */
        tf_company.setVisible(false);
        /**
         * Set the button create order invisible.
         */
        btn_OrderCreate.setVisible(false);
        /**
         * Set the label invisible.
         */
        lbl_Choose.setVisible(false);
        /**
         * Set the tooltip of the create button.
         */
        btn_CreateProduct.setTooltip(new Tooltip("Create a new product"));
        /**
         * Set the tooltip of the delete button.
         */
        btn_DeleteProduct.setTooltip(new Tooltip("Delete a product"));
        /**
         * Set the tooltip of the create order button.
         */
        btn_OrderCreate.setTooltip(new Tooltip("Create an order"));
        /**
         * Set the action of the delete button.
         */
        btn_DeleteProduct.setOnAction(this::handleOnClickDelete);
        /**
         * Set the action of the create button.
         */
        btn_CreateProduct.setOnAction(this::handleOnClickCreate);
        /**
         * Set the action when try to close the window.
         */
        stage.onCloseRequestProperty().set(this::handleCloseRequest);
        /**
         * Add a listener to the text field.
         */
        tf_company.textProperty().addListener(this::handleTextChange);
        /**
         * Set the table selection mode to multiple.
         */
        tv_Tabla.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        /**
         * Call to the editable table method.
         */
        editableProductTable();
        /**
         * Call to method to fill the table with information of the database.
         */
        productTableInfo();
        /**
         * Show the stage.
         */
        stage.show();
    }

    /**
     * The initial stage of the window when enter from order.
     *
     * @param root
     */
    public void initStageCreateOrder(Parent root) {
        LOG.log(Level.INFO, "Inicio Creado Orden ");
        /**
         * Create a new scene.
         */
        Scene scene = new Scene(root);
        /**
         * Create a new stage.
         */
        stage = new Stage();
        /**
         * Set the scene to the stage.
         */
        stage.setScene(scene);
        /**
         * Set the title to the window.
         */
        stage.setTitle("Create Order - Products");
        /**
         * Disable the resizable property.
         */
        stage.setResizable(false);
        LOG.log(Level.INFO, "Stage Create Order");
        /**
         * Set the button delete invisible.
         */
        btn_DeleteProduct.setVisible(false);
        /**
         * Set the button create invisible.
         */
        btn_CreateProduct.setVisible(false);
        /**
         * Set the action of the delete button.
         */
        btn_OrderCreate.setOnAction(this::handleOnClickCreateOrder);
        /**
         * Set the tooltip of the create order button.
         */
        btn_OrderCreate.setTooltip(new Tooltip("Create an order"));
        /**
         * Add a listener to the text field.
         */
        tf_company.textProperty().addListener(this::handleTextChange);
        /**
         * Set the table selection mode to multiple.
         */
        tv_Tabla.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        LOG.log(Level.INFO, "tabla ");
        /**
         * Call to method to fill the table with information of the database.
         */
        productTableInfo();

        LOG.log(Level.INFO, "tablainfo ");
        //Inicializar la variable en ordermanagement?
        /**
         * Show the stage and wait.
         */
        stage.showAndWait();
    }

    /**
     * The editable product table.
     */
    private void editableProductTable() {
        /**
         * Set the table editable.
         */
        tv_Tabla.setEditable(true);
        /**
         * Set the table column name cells value factory.
         */
        tc_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        /**
         * Set the table column weight cells value factory.
         */
        tc_Weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        /**
         * Set the table column price cells value factory.
         */
        tc_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        /**
         * Set the table column name cells factory.
         */
        tc_Name.setCellFactory(TextFieldTableCell.forTableColumn());
        /**
         * Set the cell on edit behavior.
         */
        tc_Name.setOnEditCommit((TableColumn.CellEditEvent<Product, String> data) -> {
            LOG.log(Level.SEVERE, "New Name: {0}", data.getNewValue());
            LOG.log(Level.SEVERE, "Old Name: {0}", data.getOldValue());
            /**
             * Validation for only entering letters and no numbers.
             */
            if (!Pattern.matches("[a-zA-Z]+", data.getNewValue())) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Look, a Warning Dialog");
                alert.setContentText("Careful with the next step!");
                alert.showAndWait();
                tv_Tabla.refresh();

            } else {
                /**
                 * Get the implementation.
                 */
                productImplementation = (ProductInterface) new ProductFactory().getImplementation();
                /**
                 * Get the data entered.
                 */
                Product p = data.getRowValue();
                /**
                 * Set the name.
                 */
                p.setName(data.getNewValue());
                /**
                 * Edit in the database.
                 */
                productImplementation.edit_XML(p);
                /**
                 * Call to method to fill the table with information of the
                 * database.
                 */
                productTableInfo();
            }
        });
        /**
         * Set the table column weight cells factory.
         */
        tc_Weight.setCellValueFactory(new PropertyValueFactory<Product, Float>("weight"));
        /**
         * Convert from float to string.
         */
        FloatStringConverter converterFloatWeight = new FloatStringConverter();
        /**
         * Set the table column name cells factory.
         */
        tc_Weight.setCellFactory(TextFieldTableCell.<Product, Float>forTableColumn(converterFloatWeight));
        /**
         * Set the cell on edit behavior.
         */
        tc_Weight.setOnEditCommit((TableColumn.CellEditEvent<Product, Float> data) -> {
            /**
             * Get the implementation.
             */
            productImplementation = (ProductInterface) new ProductFactory().getImplementation();
            /**
             * Get the data entered.
             */
            Product p = data.getRowValue();
            /**
             * Set the weight.
             */
            p.setWeight(data.getNewValue());
            /**
             * Edit in the database.
             */
            productImplementation.edit_XML(p);
            /**
             * Call to method to fill the table with information of the
             * database.
             */
            productTableInfo();

        });
        /**
         * Set the table column weight cells factory.
         */
        tc_Price.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));
        /**
         * Convert from float to string.
         */
        FloatStringConverter converterFloatPrice = new FloatStringConverter();
        /**
         * Set the table column name cells factory.
         */
        tc_Price.setCellFactory(TextFieldTableCell.<Product, Float>forTableColumn(converterFloatPrice));
        /**
         * Set the cell on edit behavior.
         */
        tc_Price.setOnEditCommit((TableColumn.CellEditEvent<Product, Float> data) -> {
            /**
             * Get the implementation.
             */
            productImplementation = (ProductInterface) new ProductFactory().getImplementation();
            /**
             * Get the data entered.
             */
            Product p = data.getRowValue();
            /**
             * Set the weight.
             */
            p.setPrice(data.getNewValue());
            /**
             * Edit in the database.
             */
            productImplementation.edit_XML(p);
            /**
             * Call to method to fill the table with information of the
             * database.
             */
            productTableInfo();
        });
    }

    /**
     * The method to fill the table with products from the database.
     */
    private void productTableInfo() {
        /**
         * Get all the products from the database.
         */
        productList = FXCollections.observableArrayList(getProductImplementation().findAllProducts_XML(new GenericType<List<Product>>() {
        }));
        /**
         * Set the products in the table.
         */
        tv_Tabla.setItems(productList);
    }

    /**
     * Method that sets the behavior when create product button is pressed.
     *
     * @param event
     */
    @FXML
    private void handleOnClickCreate(ActionEvent event) {
        try {
            /**
             * Create a new product.
             */
            Product newProduct = new Product();
            /**
             * set the name in the new product.
             */
            newProduct.setName("");
            /**
             * set the price in the new product.
             */
            newProduct.setPrice(0);
            /**
             * set the weight in the new product.
             */
            newProduct.setWeight(0);
            /**
             * Get the user that is logged at the moment.
             */
            User loggedUser = DashboardController.loggedUser;
            /**
             * set the user in the new product.
             */
            newProduct.setUser(loggedUser);
            /**
             * Get the implementation.
             */
            productImplementation = (ProductInterface) new ProductFactory().getImplementation();
            /**
             * Get the focus of the last row.
             */
            int row = table.getItems().size() - 1;
            tv_Tabla.requestFocus();
            /**
             * The focused row is selected.
             */
            tv_Tabla.getSelectionModel().select(row);
            tv_Tabla.getFocusModel().focus(row);
            /**
             * Create in the database.
             */
            productImplementation.create_XML(newProduct);
            productTableInfo();
            /**
             * The table goes to the focused row.
             */
            tv_Tabla.scrollTo(newProduct);
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Error with database", "", "Can´t refresh the table");
        }
    }

    /**
     * Method that sets the behavior when delete product button is pressed.
     *
     * @param event
     */
    @FXML
    private void handleOnClickDelete(ActionEvent event) {
        try {
            /**
             * Set the button delete invisible.
             */
            btn_DeleteProduct.setDisable(false);
            /**
             * Delete from the database.
             */
            productImplementation.remove(tv_Tabla.getSelectionModel().getSelectedItem().getId().toString());
            /**
             * Call to method to fill the table with information of the
             * database.
             */
            productTableInfo();
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Error with database", "", "Can´t delete from the table");
        }
    }
    /**
     * Method for the filter of the table.
     * @param observable
     * @param oldValue
     * @param newValue 
     */
    private void handleTextChange(ObservableValue observable, String oldValue, String newValue) {
        try {
            /**
             * Create a new filtered list of products.
             */
            FilteredList<Product> filteredData = new FilteredList<>(productList, u -> true);
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getUser().getCompany().getName().toLowerCase().contains(lowerCaseFilter)) {
                    tableOrder();
                    return true;
                }
                return false;
            });
            /**
             * Create a new sorted list of products.
             */
            SortedList<Product> sortedData = new SortedList<>(filteredData);
            /**
             * Compare the sorted list.
             */
            sortedData.comparatorProperty().bind(tv_Tabla.comparatorProperty());
            /**
             * Set the products in the table.
             */
            tv_Tabla.setItems(sortedData);
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Error with database", "", "Can´t show the table");
        }
    }
    /**
     * Method that sets the behavior when create order button is pressed.
     * @param event 
     */
    @FXML
    private void handleOnClickCreateOrder(ActionEvent event) {
        try {
            /**
             * Get the selected products from the table.
             */
            List<Product> products = tv_Tabla.getSelectionModel().getSelectedItems();
            /**
             * Send them to the order controller.
             */
            orderManagementController.setProducts(products);

            stage.close();
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Error with database", "", "Can´t create a new order");
        }
    }
    /**
     * The method that sets the action when try to close the window.
     * @param event 
     */
    private void handleCloseRequest(WindowEvent event) {
        /**
         * Create a new confirmation alert.
         */
        Alert alert = new Alert(AlertType.CONFIRMATION);
        /**
         * Set the title of the alert.
         */
        alert.setTitle("Close confirmation");
        /**
         * Set the header text of the alert.
         */
        alert.setHeaderText("Application will be closed");
        /**
         * Set the content text of the alert.
         */
        alert.setContentText("You will close the application");
        /**
         * Set the 2 options that are in the alert to click on.
         */
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(ButtonType.OK)) {
            stage.close();
            Platform.exit();
        } else {
            event.consume();
            alert.close();
        }
    }
    /**
     * Setter of the product implementation.
     * @param product 
     */
    public void setProductImplementation(ProductInterface product) {
        this.productImplementation = product;
    }
    /**
     * Getter of the product implementation.
     * @return 
     */
    public ProductInterface getProductImplementation() {
        return this.productImplementation;
    }
    /**
     * The method of the table when entered from order.
     */
    private void tableOrder() {
        /**
         * Set the table column name cells factory.
         */
        tc_Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        /**
         * Set the table column weight cells factory.
         */
        tc_Weight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        /**
         * Set the table column price cells factory.
         */
        tc_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
        /**
         * Get all the products from the database.
         */
        productList = FXCollections.observableArrayList(getProductImplementation().findAllProducts_XML(new GenericType<List<Product>>() {
        }));
        /**
         * Set the products in the table.
         */
        tv_Tabla.setItems(productList);

    }
    /**
     * Getter of the current user
     * @return current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }
    /**
     * Setter of the current user.
     * @param user 
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    /**
     * The method to show the alerts.
     * @param type
     * @param title
     * @param header
     * @param content 
     */
    public void showAlert(Alert.AlertType type, String title, String header, String content) {
        /**
         * Create a new alert.
         */
        Alert alert = new Alert(type);
        /**
         * Set the title of the alert.
         */
        alert.setTitle(title);
        /**
         * Set the header text of the alert.
         */
        alert.setHeaderText(header);
        /**
         * Set the content text of the alert.
         */
        alert.setContentText(content);
        alert.showAndWait();
    }

}
