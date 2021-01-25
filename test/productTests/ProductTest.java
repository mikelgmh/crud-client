/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productTests;

import crudclient.CRUDClient;
import crudclient.model.Product;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;

/**
 *
 * @author Aketza
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductTest extends ApplicationTest {

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

    public ProductTest() {
    }

    @BeforeClass
    public static void setUpClass() throws TimeoutException, Exception {
        ApplicationTest.launch(CRUDClient.class);

    }

    @Override
    public void stop() {
    }

    @Test
    public void test001_LoginTest() {
        clickOn("#txt_User");
        write("ake");
        clickOn("#txt_Password");
        write("1234$%Mm");
        clickOn("#btn_SignIn");

    }

    @Test
    public void test002_goToProductManagment() {
        clickOn("#menuManagement");
        clickOn("#menuProducts");
        verifyThat("#btn_Delete", isEnabled());
        verifyThat("#btn_Create", isEnabled());
        verifyThat("#btn_OrderCreate", isInvisible());
        verifyThat("#tf_company", isInvisible());
    }
    
    @Test
    public void test003_createProduct() {
        clickOn("#btn_Create");
        
        verifyThat("#btn_Delete", isEnabled());
        verifyThat("#btn_Create", isEnabled());
        verifyThat("#btn_OrderCreate", isInvisible());
        verifyThat("#tf_company", isInvisible());
    }

    @Test
    public void test004_goToProductManagmentFromOrderProduct() { // Check that the delete button is disabled
        clickOn("#menuManagement");
        clickOn("#menuOrders");
        clickOn("#btn_newOrder");
        verifyThat("#btn_Delete", isInvisible());
        verifyThat("#btn_Create", isInvisible());
        verifyThat("#btn_OrderCreate", isEnabled());
        verifyThat("#tf_company", isEnabled());
    }

    @Test
    public void test005_createNewOrder() { // Check that the delete button is disabled
        clickOn("#tf_company");
        write("Aketza");
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn("#btn_OrderCreate");
    }

}
