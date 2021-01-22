package crudclient.controllers;

/**
 *
 * @author Imanol
 */
import crudclient.client.OrderRESTClient;
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
import crudclient.model.Product;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class OrderManagementController {

    private static final Logger logger = Logger.getLogger("crudclient.controllers.OrderManagementController");
    private OrderInterface orderImplementation;
    private Stage stage;
    private ObservableList<Order> orderData;
    private ObservableList<OrderProduct> productsData;
    private List<Product> products;
    private OrderProduct orderProduct;
    private Order order;
    private User user;

    @FXML
    private TableView<Order> tableOrder;
    @FXML
    private TableColumn<Order, String> column_ID;
    @FXML
    private TableColumn<Order, Date> column_date;
    @FXML
    private TableColumn<Order, Float> column_totalPrice;
    @FXML
    private TableColumn<Order, String> column_status;
    @FXML
    private TableColumn<Order, String> column_user;
    @FXML
    private ComboBox<OrderStatus> combo_statusOrder;
    @FXML
    private TextField txt_totalPriceOrder;
    @FXML
    private TextField txt_userOrder;
    @FXML
    private TextField txt_IDOrder;
    @FXML
    private Button btn_modifyOrder;
    @FXML
    private Button btn_newOrder;
    @FXML
    private Button btn_barcode;
    @FXML
    private Button btn_deleteOrder;
    @FXML
    private TableView<OrderProduct> tableProducts;
    @FXML
    private TableColumn<OrderProduct, String> column_NameProduct;
    @FXML
    private TableColumn<OrderProduct, Integer> column_QuantityProduct;
    @FXML
    private TableColumn<OrderProduct, Float> column_totalPriceProduct;
    @FXML
    private Button btn_OrderMngmt;
    @FXML
    private Button btn_deletePro;
    @FXML
    private Button btn_commitOrder;

    public OrderManagementController() {
    }

    public void initStage(Parent parent) {
        logger.log(Level.INFO, "Loading the OrderManagement stage.");
        Scene scene = new Scene(parent);
        stage = new Stage();
        //this.setListeners();
        this.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("Order Management");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowsShowing);

        setTableOrderProductEditable();
        column_date.setSortType(TableColumn.SortType.DESCENDING);
        combo_statusOrder.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        column_ID.setCellValueFactory(new PropertyValueFactory("id"));
        column_date.setCellValueFactory(new PropertyValueFactory("date"));
        column_totalPrice.setCellValueFactory(new PropertyValueFactory("total_price"));
        column_status.setCellValueFactory(new PropertyValueFactory("status"));
        column_user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getName().concat(" " + cellData.getValue().getUser().getSurname())));
        orderData = FXCollections.observableArrayList(getOrderImplementation().findAllOrders(new GenericType<List<Order>>() {}));
        //FilteredList<Order> filteredData = new FilteredList<>(orderData, p -> true);

        tableOrder.setItems(orderData);
        stage.show();
        logger.log(Level.INFO, "OrderManagement stage loaded.");
    }

    private void handleWindowsShowing(WindowEvent event) {
        txt_IDOrder.requestFocus();
        btn_newOrder.setOnAction(this::handlerCreateOrder);
        btn_modifyOrder.setOnAction(this::handlerModifyOrder);
        btn_modifyOrder.disableProperty().bind(Bindings.isEmpty(tableOrder.getSelectionModel().getSelectedItems()));
        btn_deleteOrder.setOnAction(this::handlerDeleteOrder);
        btn_deleteOrder.disableProperty().bind(Bindings.isEmpty(tableOrder.getSelectionModel().getSelectedItems()));
        btn_deletePro.setOnAction(this::handlerDeleteProductFromOrder);
        btn_OrderMngmt.setOnAction(this::handlerOrderModification);
    }

  

    private void handlerDeleteOrder(ActionEvent event) {
        order = tableOrder.getSelectionModel().getSelectedItem();
        this.getOrderImplementation().removeOrder(order.getId().toString());
        tableOrder.getItems().remove(order);
    }

    private void handlerCreateOrder(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/product.fxml"));
        Parent root;
        try {
            root = (Parent) loader.load();
            ProductController pController = ((ProductController) loader.getController());
            ProductFactory productFactory = new ProductFactory();
            ProductInterface product = productFactory.getImplementation();
            pController.setProductImplementation(product);
            pController.setOrdermanagementController(this);
            pController.initStageCreateOrder(root);

            createOrder(products, user);

        } catch (IOException ex) {
            Logger.getLogger(OrderManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handlerModifyOrder(ActionEvent event) {
        order = tableOrder.getSelectionModel().getSelectedItem();
        productsData = FXCollections.observableList(order.getOrderProduct());
        tableProducts.setItems((ObservableList<OrderProduct>) productsData);
        
    }

    private void handlerDeleteProductFromOrder(ActionEvent event) {
        //Poner alerta para confirmar el borrado del producto
        orderProduct = tableProducts.getSelectionModel().getSelectedItem();
        tableProducts.getItems().remove(orderProduct);
    }

    private void handlerOrderModification(ActionEvent event) {
        this.getOrderImplementation().editOrder(order);
    }
        
    private void createOrder(List<Product> products, User user) {
       /* order = new Order();
        OrderProduct orderProductAuxiliar = new OrderProduct();
        Date date = java.util.Calendar.getInstance().getTime();
        
        order.setDate(date);
        order.setUser(user);
        order.setStatus(OrderStatus.REQUESTED);
        Float total_price= 0.0f;
        //Set<OrderProduct> orderProduct = new HashSet<OrderProduct>();;

        for (Product p : products) {
            orderProductAuxiliar.setProduct(p);
            orderProductAuxiliar.setTotal_price(p.getPrice());
            total_price=p.getPrice()+total_price;
            orderProduct.add(orderProductAuxiliar);
        }
        
        order.setOrderProduct(orderProduct);
        order.setTotal_price(total_price);
        
        this.getOrderImplementation().createOrder(order);
        */
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setOrderImplementation(OrderInterface order) {
        this.orderImplementation = order;
    }

    public OrderInterface getOrderImplementation() {
        return this.orderImplementation;
    }

    private void setTableOrderProductEditable() {
        tableProducts.setEditable(true);
        column_NameProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        column_QuantityProduct.setCellValueFactory(new PropertyValueFactory("total_quantity"));
        IntegerStringConverter quantity = new IntegerStringConverter();
        column_totalPriceProduct.setCellValueFactory(new PropertyValueFactory("total_price"));

        column_QuantityProduct.setCellFactory(TextFieldTableCell.<OrderProduct, Integer>forTableColumn(quantity));
        column_QuantityProduct.setOnEditCommit(data -> {
         
            tableProducts.getSelectionModel().getSelectedItem().setTotal_quantity(data.getNewValue());
            getOrderImplementation().editOrder(order);
            });

    }

    
}
