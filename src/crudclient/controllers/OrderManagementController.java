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
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.ws.rs.core.GenericType;
import crudclient.interfaces.OrderInterface;
import crudclient.interfaces.ProductInterface;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

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

    @FXML
    private TableView<Order> tableOrder;
    @FXML
    private TableColumn<Order, String> column_ID;
    @FXML
    private TableColumn<Order, Date> column_date;
    @FXML
    private TableColumn<Order, Double> column_totalPrice;
    @FXML
    private TableColumn<Order, String> column_status;
    @FXML
    private TableColumn<Order, String> column_user;
    @FXML
    private DatePicker date_Order;
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
    private Button btn_filterOrder;

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

        column_date.setSortType(TableColumn.SortType.DESCENDING);
        combo_statusOrder.setItems(FXCollections.observableArrayList(OrderStatus.values()));
        column_ID.setCellValueFactory(new PropertyValueFactory("id"));
        column_date.setCellValueFactory(new PropertyValueFactory("date"));
        column_totalPrice.setCellValueFactory(new PropertyValueFactory("total_price"));
        column_status.setCellValueFactory(new PropertyValueFactory("status"));
        column_user.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getName().concat(" " + cellData.getValue().getUser().getSurname())));
        orderData = FXCollections.observableArrayList(getOrderImplementation().findAllOrders(new GenericType<Set<Order>>() {
        }));
        FilteredList<Order> filteredData = new FilteredList<>(orderData, p -> true);

        tableOrder.setItems(orderData);
        stage.show();
        logger.log(Level.INFO, "OrderManagement stage loaded.");
    }

    private void handleWindowsShowing(WindowEvent event) {
        txt_IDOrder.requestFocus();
        btn_newOrder.setOnAction(this::handlerCreateOrder);
        btn_newOrder.disableProperty().bind(Bindings.isEmpty(tableOrder.getSelectionModel().getSelectedItems()));
        btn_deleteOrder.setOnAction(this::handlerDeleteOrder);
        btn_deleteOrder.disableProperty().bind(Bindings.isEmpty(tableOrder.getSelectionModel().getSelectedItems()));
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

    private void handlerDeleteOrder(ActionEvent event) {
        Order order = tableOrder.getSelectionModel().getSelectedItem();
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
            pController.initStage(root);
            this.stage.close();
        } catch (IOException ex) {
            Logger.getLogger(OrderManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
