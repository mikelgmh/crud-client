package crudclient.controllers;


import crudclient.client.OrderRESTClient;
import crudclient.exceptions.ServerConnectionException;
import crudclient.factories.OrderFactory;
import crudclient.factories.ProductFactory;
import crudclient.model.Order;
import crudclient.model.OrderStatus;
import crudclient.model.User;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.core.GenericType;
import crudclient.interfaces.OrderInterface;
import crudclient.interfaces.ProductInterface;
import crudclient.model.OrderProduct;
import crudclient.model.OrderProductId;
import crudclient.model.Product;
import crudclient.model.UserStatus;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javax.ws.rs.ClientErrorException;

/**
 * Controller class for orders management view . 
 * It contains event handlers and initialization code for the view defined in 
 * orders.fmxl file.
 * @author Imanol
 */

public class OrderManagementController {

    /**
     * Logger of this class.
     */
    private static final Logger logger = Logger.getLogger("crudclient.controllers.OrderManagementController");
    
    /**
     * Interface to implement REST methods in this class.
     */
    private OrderInterface orderImplementation;
    
    /**
     * Stage in which this window will be displayed.
     */
    private Stage stage;
    
    /**
     * ObservableList where the orders collected from the database are stored.
     */
    private ObservableList<Order> orderData;
    
    /**
     * ObservableList where the products collected from the database are stored.
     */
    private ObservableList<OrderProduct> productsData;
    
    /**
     * List where the products of the product view are stored.
     */
    private List<Product> products;
    
    /**
     * List where products are stored to perform order creation operations.
     */
    private OrderProduct addProducts = null;
    
    /**
     * List where products are stored to perform order creation operations.
     */
    private ArrayList<OrderProduct> auxOrderProduct = null;
    
    /**
     * OrderProductId object to perform order create operations
     */
    private OrderProductId addId = null;
    
    /**
     * OrderProduct object to perform order create operations
     */
    private OrderProduct orderProduct;
    
    /**
     * Order object to do different operations on the table
     */
    private Order order;
    
    /**
     * User object to store the current user data
     */
    private User currentUser;

    /**
     * Orders data table view.
     */
    @FXML
    private TableView<Order> tableOrder;
    
    /**
     * Orders ID data table column.
     */
    @FXML
    private TableColumn<Order, String> column_ID;
    
    /**
     * Orders date data table column.
     */
    @FXML
    private TableColumn<Order, Date> column_date;
    
    /**
     * Orders total price data table column.
     */
    @FXML
    private TableColumn<Order, Float> column_totalPrice;
    
    /**
     * Orders status data table column.
     */
    @FXML
    private TableColumn<Order, OrderStatus> column_status;
    
    /**
     * Orders user creator data table column.
     */
    @FXML
    private TableColumn<Order, String> column_user;
    
    /**
     * Status order filter combo box.
     */
    @FXML
    private ComboBox<OrderStatus> combo_statusOrder;
    
    /**
     * Total price order filter text field.
     */
    @FXML
    private TextField txt_totalPriceOrder;
    
    /**
     * Order creator user name filter text field.
     */
    @FXML
    private TextField txt_userOrder;
    
    /**
     * Order ID filter text field.
     */
    @FXML
    private TextField txt_IDOrder;
    
    /**
     * Modify order data button.
     */
    @FXML
    private Button btn_modifyOrder;
    
    /**
     * Create order button.
     */
    @FXML
    private Button btn_newOrder;
    
    /**
     * Create bar code button.
     */
    @FXML
    private Button btn_barcode;
    
    /**
     * Delete order button.
     */
    @FXML
    private Button btn_deleteOrder;
    
    /**
     * Order products data table view.
     */
    @FXML
    private TableView<OrderProduct> tableProducts;
    
    /**
     * Product name table column.
     */
    @FXML
    private TableColumn<OrderProduct, String> column_NameProduct;
    
    /**
     * Product quantity table column.
     */
    @FXML
    private TableColumn<OrderProduct, Integer> column_QuantityProduct;
    
    /**
     * Product unit price table column.
     */
    @FXML
    private TableColumn<OrderProduct, Float> column_totalPriceProduct;
    
    /**
     * Edit products of actual order button.
     */
    @FXML
    private Button btn_OrderMngmt;
    
    /**
     * Send new order to database button.
     */
    @FXML
    private Button btn_commitOrder;

    /**
     * Logger definition.
     */
    private static final Logger LOG = Logger.getLogger(OrderManagementController.class.getName());

    /**
     * Class empty constructor.
     */
    public OrderManagementController() {
    }
    
    /**
    * Method that starts the context of the view.
    * @param parent container for all content in a scene graph.
    */
    public void initStage(Parent parent) {
        logger.log(Level.INFO, "Loading the OrderManagement stage.");
        //Set the parent scene to the actual scene.
        Scene scene = new Scene(parent);
        stage = new Stage();
        this.setStage(stage);
        stage.setScene(scene);
        //Set title of the window
        stage.setTitle("Order Management");
        //Make window NOT resizable
        stage.setResizable(false);
        //Call the handler method to set the initial parameters of the table:
        //buttons activated, factory and values ​​of the cells and editable tables.
        stage.setOnShowing(this::handleWindowsShowing);

        //Call to the DB to obtain the orders.
        try {
            orderData = FXCollections.observableArrayList(getOrderImplementation()
                    .findAllOrders(new GenericType<List<Order>>() {
            }));
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Server ERROR", "", "Can´t get data!");
        }

        //Method call to set the order table as filtered in order to use the filters.
        filteredListAndTableListeners();

        //Show the window.
        stage.show();
        
        //Initial alert to warn the user of the characteristics of the tables.
        showAlert(Alert.AlertType.WARNING, "Caution!!!", "", "Aviso. Los cambios"
                + " que hagas en la tabla de ordenes (estado de la orden) "
                + "se actualizarán en la base de datos automaticamente. Los productos"
                + "(tabla inferior) requieren de pulsar los botones en product manager"
                + "que se encuentran a su derecha.");
        
        logger.log(Level.INFO, "OrderManagement stage loaded.");
    }

    /**
     * Method that establishes the state of the window.
     * @param event A event that indicates that a window has changed its status.
     */
    private void handleWindowsShowing(WindowEvent event) {
        //Method that makes the product table editable.
        setTableOrderProductEditable();
        
        //Make the order table editable.
        tableOrder.setEditable(true);
        //Focus on text field ID.
        txt_IDOrder.requestFocus();
        //Set on action event to new order button.
        btn_newOrder.setOnAction(this::handlerCreateOrder);
        //Set on action event to modify order button.
        btn_modifyOrder.setOnAction(this::handlerModifyOrder);
        //Make the button disabled if there is no row selected.
        btn_modifyOrder.disableProperty().bind(Bindings.isEmpty(tableOrder.getSelectionModel().getSelectedItems()));
        //Set on action event to delete order button.
        btn_deleteOrder.setOnAction(this::handlerDeleteOrder);
        //Make the button disabled if there is no row selected.
        btn_deleteOrder.disableProperty().bind(Bindings.isEmpty(tableOrder.getSelectionModel().getSelectedItems()));
        //Set on action event to update order button.
        btn_OrderMngmt.setOnAction(this::handlerOrderModification);
        //Make the button disabled if there is no row selected.
        btn_OrderMngmt.disableProperty().bind(Bindings.isEmpty(tableProducts.getSelectionModel().getSelectedItems()));
        //Set on action event to commit order button.
        btn_commitOrder.setOnAction(this::handlerCommitNewOrder);
        //Make the button disabled if there is no row selected.
        btn_commitOrder.disableProperty().bind(Bindings.isEmpty(tableProducts.getSelectionModel().getSelectedItems()));

        //Disable buttons if user has worker privilege.
        setButtonsWorker();

        //Set action to update database if the status column is edited.
        column_status.setOnEditCommit((TableColumn.CellEditEvent<Order, OrderStatus> data) -> {
            tableOrder.getSelectionModel().getSelectedItem().setStatus(data.getNewValue());
            tableOrder.refresh();

            orderImplementation.editOrder(tableOrder.getSelectionModel().getSelectedItem());
        });
        
        //Set different order status to combo box.
        combo_statusOrder.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        
        //Set factories to columns.
        column_ID.setCellValueFactory(new PropertyValueFactory("id"));
        column_date.setCellValueFactory(new PropertyValueFactory("date"));
        
        //Set format to date column.
        column_date.setCellFactory(column -> {
            TableCell<Order, Date> cell = new TableCell<Order, Date>() {
                private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(format.format(item));
                    }
                }
            };

            return cell;
        });
        
        //Set cell factories
        column_totalPrice.setCellValueFactory(new PropertyValueFactory("total_price"));
        column_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList statusOrders = FXCollections.observableArrayList(OrderStatus.values());
        column_status.setCellFactory(ComboBoxTableCell.forTableColumn(statusOrders));
        column_user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getName().concat(" " + cellData.getValue().getUser().getSurname())));

    }
    
    /**
     * Set table products editing parameters.
     */
    private void setTableOrderProductEditable() {
        tableProducts.setEditable(true);
        //Set table products columns factories.
        column_NameProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        column_QuantityProduct.setCellValueFactory(new PropertyValueFactory("total_quantity"));
        //Create converter so that a cell can only have numbers
        IntegerStringConverter quantity = new IntegerStringConverter();
        column_totalPriceProduct.setCellValueFactory(new PropertyValueFactory("total_price"));

        //When updating the quantity field of the products table, the total 
        //price of the products and the order is recalculated.
        column_QuantityProduct.setCellFactory(TextFieldTableCell.<OrderProduct, Integer>forTableColumn(quantity));
        try {
            column_QuantityProduct.setOnEditCommit(data -> {
                tableProducts.getSelectionModel().getSelectedItem().setTotal_quantity(data.getNewValue());
                Float unitPrice = tableProducts.getSelectionModel().getSelectedItem().getProduct().getPrice();
                tableProducts.getSelectionModel().getSelectedItem().setTotal_price(unitPrice * tableProducts.getSelectionModel().getSelectedItem().getTotal_quantity());//ERROR
                order.setOrderProduct(productsData);
                Float totalOrderPrice = 0.0f;
                for (OrderProduct op : order.getOrderProduct()) {
                    totalOrderPrice = totalOrderPrice + op.getTotal_price();
                }
                order.setTotal_price(totalOrderPrice);
            });
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "It´s not a number", "", "You must type a number!");
        }

    }

    
    /**
     * Deletes the order selected in the table from the database.
     * @param event is generated by delete order button.
     */
    private void handlerDeleteOrder(ActionEvent event) {
        //Selects the table item.
        order = tableOrder.getSelectionModel().getSelectedItem();
        //Gets the implementation to call delete operation.
        this.getOrderImplementation().removeOrder(order.getId().toString());
        //Obtains the orders updated.
        orderData = FXCollections.observableArrayList(getOrderImplementation().findAllOrders(new GenericType<List<Order>>() {
        }));
        //Set the orders to the table.
        tableOrder.setItems(orderData);
        //Refresh the table.
        tableOrder.refresh();
        //Show an alert indicating that the operation has been done successfully.
        showAlert(Alert.AlertType.INFORMATION, "Deleted succesfully", "", "Order deleted from database.");
    }

    /**
     * Launch the products window. Select the products and return them to this controller.
     * @param event is generated by create order button.
     */
    private void handlerCreateOrder(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/product.fxml"));
        Parent root;
        try {
            root = (Parent) loader.load();
            ProductController pController = ((ProductController) loader.getController());
            //Loads the factory to obtain the implementation.
            ProductFactory productFactory = new ProductFactory();
            //Loads and interface and gets the implementation.
            ProductInterface product = productFactory.getImplementation();
            //Set the implementation on product controller.
            pController.setProductImplementation(product);
            //Set order controller to product implementation to obtain the products.
            pController.setOrdermanagementController(this);
            //Loads the product stage.
            pController.initStageCreateOrder(root);
            LOG.log(Level.INFO, "Regresa a Order ");
            createOrder(products);
        } catch (IOException ex) {
            showAlert(Alert.AlertType.WARNING, "Can´t change to product window.", "", "Can´t open the product manager.");
        }
    }

    /**
     * Load the products in the lower table of the selected order.
     * @param event is generated by modify order button.
     */
    private void handlerModifyOrder(ActionEvent event) {
        try {
            order = tableOrder.getSelectionModel().getSelectedItem();
            productsData = FXCollections.observableList(order.getOrderProduct());
            tableProducts.setItems((ObservableList<OrderProduct>) productsData);
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Can´t obtain products.", "", "Can´t obtain products from this order.");
        }

    }

    /**
     * Send the edited order in the products table.
     * @param event is generated by modify order button.
     */
    private void handlerOrderModification(ActionEvent event) {
        //Alert asking for confirmation to edit the order on database.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit value");
        String a = "Your changes will be edited in the database. Are you sure?";
        alert.setContentText(a);
        Optional<ButtonType> result = alert.showAndWait();
        //If result is ok, edit order.
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            //Edit order on database.
            getOrderImplementation().editOrder(order);
            try {
                //Get the order data and table data updated and show on table.
                orderData = FXCollections.observableArrayList(getOrderImplementation().findAllOrders(new GenericType<List<Order>>() {
                }));
                tableOrder.setItems(orderData);
                tableOrder.refresh();
                order = tableOrder.getSelectionModel().getSelectedItem();
                productsData = FXCollections.observableList(order.getOrderProduct());
                tableProducts.setItems((ObservableList<OrderProduct>) productsData);
                tableOrder.refresh();
            } catch (Exception e) {
                showAlert(Alert.AlertType.WARNING, "Error with database", "", "Can´t refresh table with orders value.");
            }
        }
    }

    /**
     * Send the created order to the database.
     * @param event is generated by send new order button.
     */
    private void handlerCommitNewOrder(ActionEvent event) {
        //Alert asking for confirmation to create order on database.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Edit value");
        String a = "Your changes will be edited in the database. Are you sure?";
        alert.setContentText(a);
        Optional<ButtonType> result = alert.showAndWait();
        //If user press ok, create new order.
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            Float totalOrderPrice = 0.0f;
            //Sets total price of order getting all total price of products.
            for (OrderProduct op : order.getOrderProduct()) {
                totalOrderPrice = totalOrderPrice + op.getTotal_price();
            }
            order.setTotal_price(totalOrderPrice);


            try {
                //Sends the order to database and refresh the table.
                this.getOrderImplementation().createOrder(order);
                orderData = FXCollections.observableArrayList(getOrderImplementation().findAllOrders(new GenericType<List<Order>>() {
                }));
                tableOrder.setItems(orderData);
                tableOrder.refresh();
                tableProducts.getItems().clear();
            } catch (Exception e) {
                showAlert(Alert.AlertType.INFORMATION, "Can´t create ", "", "Order deleted from database.");
            }
        }
    }
    
    /**
     * Method that creates the order. Sets the parameters that the order
     * must have: date, current user, order id and status.
     * @param products received from view products.
     */
    private void createOrder(List<Product> products) {
        //Gets the last order on table.
        Order lastOrder = orderData.get(orderData.size() - 1);
        Integer newId = lastOrder.getId();

        order = new Order();
        //Set the new ID to this order.
        order.setId(newId + 1);

        User loggedUser = DashboardController.loggedUser;
        //Set the user creator.
        order.setUser(loggedUser);
        Date date = java.util.Calendar.getInstance().getTime();
        //Set the actual date to order.
        order.setDate(date);

        //Set the status to order.
        order.setStatus(OrderStatus.REQUESTED);

        //Set the products to the order.
        orderProduct = new OrderProduct();
        auxOrderProduct = new ArrayList<OrderProduct>();
        for (Product o : products) {
            addProducts = new OrderProduct();
            addId = new OrderProductId();
            addId.setOrderId(newId + 1);
            addId.setProductId(o.getId());
            addProducts.setId(addId);
            addProducts.setProduct(o);
            addProducts.setTotal_price(o.getPrice());
            addProducts.setTotal_quantity(1);
            auxOrderProduct.add(addProducts);
        }

        order.setOrderProduct(auxOrderProduct);
        productsData = FXCollections.observableList(auxOrderProduct);
        //Set the products obtained from the product view to the table below.
        tableProducts.setItems(productsData);
        tableProducts.setItems((ObservableList<OrderProduct>) productsData);

    }

    /**
     * Method to reuse alarms.
     * @param type contains the type of alarm.
     * @param title contains the title of alarm.
     * @param header contains the header of alarm.
     * @param content set the content of the alarm.
     */
    public void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    /**
     * Makes the order table filterable and sorted by ID.
     */
    private void filteredListAndTableListeners() {
        FilteredList<Order> filteredData = new FilteredList<>(orderData, p -> true);
        //Calls the filtering method of the table fields.
        setSearchFilterListeners(filteredData);
        SortedList<Order> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(tableOrder.comparatorProperty());
        this.tableOrder.setItems(sortedData);
    }

    /**
     * Makes the table responsive to the filters on the right side.
     * @param filteredData has the filtered data from table order.
     */
    private void setSearchFilterListeners(FilteredList<Order> filteredData) {
        filteredData.predicateProperty().bind(Bindings.createObjectBinding(()
                -> order -> order.getId().toString().contains(txt_IDOrder.getText())
                && order.getUser().getName().toLowerCase().contains(txt_userOrder.getText().toLowerCase().trim())
                && order.getTotal_price().toString().contains(txt_totalPriceOrder.getText()),
                //&& order.getStatus().toString().equalsIgnoreCase(combo_statusOrder.getSelectionModel().getSelectedItem().toString()),
                txt_IDOrder.textProperty(),
                txt_userOrder.textProperty(),
                txt_totalPriceOrder.textProperty()
        //combo_statusOrder.getSelectionModel().getSelectedItem()
        ));
    }

    /**
     * Disable buttons if user privilege is worker
     */
    private void setButtonsWorker() {
        currentUser = DashboardController.loggedUser;
        if (currentUser.getPrivilege().toString().equalsIgnoreCase("WORKER")) {
            btn_modifyOrder.setDisable(true);
            btn_deleteOrder.setDisable(true);
        }
    }
    
    /**
     * Gets current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets current user.
     * @param user 
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Gets product list.
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Sets products list.
     * @param products 
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    /**
     * Sets stage.
     * @param stage 
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Gets stage.
     */
    public Stage getStage() {
        return this.stage;
    }

    /**
     * Sets implementation.
     * @param order 
     */
    public void setOrderImplementation(OrderInterface order) {
        this.orderImplementation = order;
    }

    /**
     * Gets implementation.
     */
    public OrderInterface getOrderImplementation() {
        return this.orderImplementation;
    }
}
