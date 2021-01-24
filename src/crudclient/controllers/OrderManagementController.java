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
import crudclient.model.OrderProductId;
import crudclient.model.Product;
import crudclient.model.UserStatus;
import java.io.IOException;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
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
    private User currentUser;

    @FXML
    private TableView<Order> tableOrder;
    @FXML
    private TableColumn<Order, String> column_ID;
    @FXML
    private TableColumn<Order, Date> column_date;
    @FXML
    private TableColumn<Order, Float> column_totalPrice;
    @FXML
    private TableColumn<Order, OrderStatus> column_status;
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

    private static final Logger LOG = Logger.getLogger(OrderManagementController.class.getName());

    public OrderManagementController() {
    }

    public void initStage(Parent parent) {
        logger.log(Level.INFO, "Loading the OrderManagement stage.");
        Scene scene = new Scene(parent);
        stage = new Stage();
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
        column_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList statusOrders = FXCollections.observableArrayList(OrderStatus.values());
        column_status.setCellFactory(ComboBoxTableCell.forTableColumn(statusOrders));
        column_user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getName().concat(" " + cellData.getValue().getUser().getSurname())));
        orderData = FXCollections.observableArrayList(getOrderImplementation().findAllOrders(new GenericType<List<Order>>() {
        }));

        tableOrder.setItems(orderData);
        stage.show();
        logger.log(Level.INFO, "OrderManagement stage loaded.");
    }

    private void setTableOrderProductEditable() {
        tableProducts.setEditable(true);
        column_NameProduct.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        column_QuantityProduct.setCellValueFactory(new PropertyValueFactory("total_quantity"));
        IntegerStringConverter quantity = new IntegerStringConverter();
        column_totalPriceProduct.setCellValueFactory(new PropertyValueFactory("total_price"));

        column_QuantityProduct.setCellFactory(TextFieldTableCell.<OrderProduct, Integer>forTableColumn(quantity));
        column_QuantityProduct.setOnEditCommit(data -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Edit value");
            String a = "Your changes will be edited in the database. Are you sure?";
            alert.setContentText(a);
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                tableProducts.getSelectionModel().getSelectedItem().setTotal_quantity(data.getNewValue());
                Float unitPrice = tableProducts.getSelectionModel().getSelectedItem().getProduct().getPrice();
                tableProducts.getSelectionModel().getSelectedItem().setTotal_price(unitPrice * tableProducts.getSelectionModel().getSelectedItem().getTotal_quantity());//ERROR
                order.setOrderProduct(productsData);
                Float totalOrderPrice = 0.0f;
                for (OrderProduct op : order.getOrderProduct()) {
                    totalOrderPrice = totalOrderPrice + op.getTotal_price();
                }
                order.setTotal_price(totalOrderPrice);
                getOrderImplementation().editOrder(order);
                orderData = FXCollections.observableArrayList(getOrderImplementation().findAllOrders(new GenericType<List<Order>>() {}));
                tableOrder.setItems(orderData);
                tableOrder.refresh();
                order = tableOrder.getSelectionModel().getSelectedItem();
                productsData = FXCollections.observableList(order.getOrderProduct());
                tableProducts.setItems((ObservableList<OrderProduct>) productsData);
                tableOrder.refresh();
            } else {
                tableProducts.getSelectionModel().getSelectedItem().setTotal_quantity(data.getOldValue());
            }

        });

    }

    private void handleWindowsShowing(WindowEvent event) {
        tableOrder.setEditable(true);
        txt_IDOrder.requestFocus();
        btn_newOrder.setOnAction(this::handlerCreateOrder);
        btn_modifyOrder.setOnAction(this::handlerModifyOrder);
        btn_modifyOrder.disableProperty().bind(Bindings.isEmpty(tableOrder.getSelectionModel().getSelectedItems()));
        btn_deleteOrder.setOnAction(this::handlerDeleteOrder);
        btn_deleteOrder.disableProperty().bind(Bindings.isEmpty(tableOrder.getSelectionModel().getSelectedItems()));
        btn_deletePro.setOnAction(this::handlerDeleteProductFromOrder);
        btn_deletePro.setVisible(false);
        btn_OrderMngmt.setOnAction(this::handlerOrderModification);
        btn_deletePro.setVisible(false);
        
        column_status.setOnEditCommit((TableColumn.CellEditEvent<Order, OrderStatus> data) -> {
            tableOrder.getSelectionModel().getSelectedItem().setStatus(data.getNewValue());
            tableOrder.refresh();
            orderImplementation.editOrder(tableOrder.getSelectionModel().getSelectedItem());
        });
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
            LOG.log(Level.INFO, "Regresa a Order ");
            createOrder(products, currentUser);

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
        Order lastOrder = orderData.get(orderData.size() - 1);
        Integer newId = lastOrder.getId();

        order = new Order();
        Date date = java.util.Calendar.getInstance().getTime();

        order.setDate(date);
        order.setUser(user);
        order.setStatus(OrderStatus.REQUESTED);
        orderProduct = new OrderProduct();
        List<OrderProduct> prueba = null;
        OrderProduct addProducts = new OrderProduct();
        OrderProductId addId = new OrderProductId();
        for (Product o : products) {
            addProducts.setOrder(order);
            addId.setOrderId(newId + 1);
            addId.setProductId(o.getId());
            addProducts.setId(addId);
            addProducts.setProduct(o);
            addProducts.setTotal_price(o.getPrice());
            addProducts.setTotal_quantity(1);
            prueba.add(addProducts);
        }

        productsData = FXCollections.observableList(addProductsToTable);
        tableProducts.setItems(productsData);
        //tableProducts.setItems((ObservableList<OrderProduct>) productsData);

        /*OrderProduct orderProductAuxiliar = new OrderProduct();
        Date date = java.util.Calendar.getInstance().getTime();
        
        order.setDate(date);
        order.setUser(user);
        order.setStatus(OrderStatus.REQUESTED);
        
        //Set<OrderProduct> orderProduct = new HashSet<OrderProduct>();;

        for (Product p : products) {
             orderProductAuxiliar.setProduct(p);
            orderProductAuxiliar.setTotal_price(p.getPrice());
            //total_price=p.getPrice()+total_price;
            //orderProduct.add(orderProductAuxiliar);
        }
        
        //order.setOrderProduct(orderProduct);
       // order.setTotal_price(total_price);
        
        this.getOrderImplementation().createOrder(order);*/
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
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

    public void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

}
