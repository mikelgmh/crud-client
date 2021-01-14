package crudclient.controllers;

/**
 *
 * @author Imanol
 */
import crudclient.client.OrderRESTClient;
import crudclient.factories.OrderFactory;
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
    private TableColumn<Order, Long> column_user;
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
        
        txt_totalPriceOrder.textProperty().addListener(this::handleTextChanged);
        txt_IDOrder.textProperty().addListener(this::handleTextChanged);
        txt_userOrder.textProperty().addListener(this::handleTextChanged);
        
        date_Order.valueProperty().addListener(this::handleDateChanged);

        combo_statusOrder.setItems( FXCollections.observableArrayList(OrderStatus.values()));
        column_ID.setCellValueFactory(new PropertyValueFactory("id"));
        column_date.setCellValueFactory(new PropertyValueFactory("date"));
        column_totalPrice.setCellValueFactory(new PropertyValueFactory("total_price"));
        column_status.setCellValueFactory(new PropertyValueFactory("status"));
        column_user.setCellValueFactory(new PropertyValueFactory("user"));
        orderData=FXCollections.observableArrayList(getOrderImplementation().getOrders());

        tableOrder.setItems(orderData);
        stage.show();
        logger.log(Level.INFO, "OrderManagement stage loaded.");
    }

   /*private void setListeners() {
        this.txt_Firstname.textProperty().addListener((obs, oldText, newText) -> {
            this.validationUtils.minLength(this.txt_Firstname, 3, newText, "minLengthValidator"); // Adds a min lenght validator
            this.validationUtils.textLimiter(this.txt_Firstname, 20, newText); // Limits the input to 20 characters
            this.validate(); // Executes the validation.
        });
        
        
    }*/

    private void handleWindowsShowing(WindowEvent event) {
        txt_IDOrder.requestFocus();
    }
    
    private void handleTextChanged(ObservableValue observable,
             Object oldValue,
             Object newValue){
        
    }
    
    private void handleDateChanged(ObservableValue observable,
             Object oldValue,
             Object newValue) {
        //If there is a row selected, move row data to corresponding fields in the
        //window and enable create, modify and delete buttons
        LocalDate today = LocalDate.now();
        if(date_Order.getValue()==null || date_Order.getValue().isAfter(today)){
            btn_filterOrder.setDisable(true);
        }else{
            btn_filterOrder.setDisable(false);
        }
    }
    
    /*public void validate() {
        if (Boolean.parseBoolean(this.txt_Email.getProperties().get("emailValidator").toString())
                && Boolean.parseBoolean(this.txt_Firstname.getProperties().get("minLengthValidator").toString())
                && Boolean.parseBoolean(this.txt_Username.getProperties().get("minLengthValidator").toString())
                && Boolean.parseBoolean(this.txt_Lastname.getProperties().get("minLengthValidator").toString())
                && Boolean.parseBoolean(this.txt_Password.getProperties().get("passwordRequirements").toString())
                && Boolean.parseBoolean(this.txt_RepeatPassword.getProperties().get("passwordRequirements").toString())
                && Boolean.parseBoolean(this.txt_RepeatPassword.getProperties().get("passwordsMatch").toString())) {
            this.btn_SignUp.setDisable(false);
        } else {
            this.btn_SignUp.setDisable(true);
        }
    }*/
    
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
    


}
