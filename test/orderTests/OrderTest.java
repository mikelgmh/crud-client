package orderTests;

import crudclient.CRUDClient;
import crudclient.controllers.OrderManagementController;
import crudclient.model.Order;
import crudclient.model.OrderProduct;
import crudclient.model.OrderStatus;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author Imanol
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderTest extends ApplicationTest {
    
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
    private Button btn_commitOrder;
    @FXML
    private DatePicker date_OrderPicker;

    public OrderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws TimeoutException, Exception {
        ApplicationTest.launch(CRUDClient.class);

    }

    @Override
    public void stop() {
    }

    @Test
    public void testA_Login() {
        clickOn("#txt_User");
        write("imanol");
        clickOn("#txt_Password");
        write("1234$%Mm");
        clickOn("#btn_SignIn");
        verifyThat("#paneDashboard", isVisible());
    }
    
    @Test
    public void testB_GoOrder() {
        clickOn("#menuManagement");
        clickOn("#menuOrders");
        verifyThat("#paneOrders", isVisible());
        verifyThat("#btn_modifyOrder", isDisabled());
        verifyThat("#btn_deleteOrder", isDisabled());
    }
    
    @Test
    public void testC_GoProductWindow() {
        clickOn("#btn_newOrder");
        verifyThat("#paneProducts", isVisible());
    }  
    
    @Test
    public void testD_sendProductsToOrder(){
        clickOn("#tf_company");
        write("Granja Alonsotegi");
        Node rowName = lookup("#tc_Name").nth(1).query();
        clickOn(rowName);
        sleep(2000);
        clickOn("#btn_OrderCreate");
        verifyThat("#paneOrders", isVisible());
    }
    
    @Test
    public void testE_createOrder() {
        Node rowName = lookup("#column_QuantityProduct").nth(1).query();
        doubleClickOn(rowName);
        write("3");
        this.press(KeyCode.ENTER);
        clickOn("#btn_commitOrder");
        clickOn("Aceptar");
        clickOn("Aceptar");
        verifyThat("#paneOrders", isVisible());
    }
    
         
    @Test
    public void testF_enabledButtons(){
        Node rowName = lookup("#column_ID").nth(1).query();
        clickOn(rowName);
        verifyThat("#btn_modifyOrder", isEnabled());
        verifyThat("#btn_deleteOrder", isEnabled());
    }
    
    @Test
    public void testG_deleteOrder() {
        tableOrder = lookup("#tableOrder").queryTableView();
        int rowCount = tableOrder.getItems().size();
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        clickOn(row);
        clickOn("#btn_deleteOrder");
        clickOn("Aceptar");
    }
    
    @Test
    public void testH_modifyOrder() {
        tableOrder = lookup("#tableOrder").queryTableView();
        int rowCount = tableOrder.getItems().size();
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        clickOn(row);
        clickOn("#btn_modifyOrder");
        Node rowName = lookup("#column_QuantityProduct").nth(1).query();
        doubleClickOn(rowName);
        write("50");
        this.press(KeyCode.ENTER);
        clickOn("#btn_OrderMngmt");
        clickOn("Aceptar");
        verifyThat("#paneOrders", isVisible());
        
    }
    
    
    
    
}
