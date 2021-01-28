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
import javafx.scene.input.KeyCode;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
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
    private Button btn_CreateProduct;
    @FXML
    private Button btn_DeleteProduct;
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
        verifyThat("#btn_DeleteProduct", isEnabled());
        verifyThat("#btn_CreateProduct", isEnabled());
        verifyThat("#btn_OrderCreate", isInvisible());
        verifyThat("#tf_company", isInvisible());
    }

    @Test
    public void test003_createProduct() {
        //int totalRows = tv_Tabla.getItems().size();
        clickOn("#btn_CreateProduct");
        Node rowNameOrder = lookup("#tc_Name").nth(0).query();
        clickOn(rowNameOrder);
        Node rowName = lookup("#tc_Name").nth(1).query();
        doubleClickOn(rowName);
        write("Aceite");
        type(KeyCode.ENTER);
        clickOn(rowNameOrder);
        Node rowWeight = lookup("#tc_Weight").nth(1).query();
        doubleClickOn(rowWeight);
        write("13");
        type(KeyCode.ENTER);
        clickOn(rowNameOrder);
        Node rowPrice = lookup("#tc_Price").nth(1).query();
        doubleClickOn(rowPrice);
        write("50");
        type(KeyCode.ENTER);
       // verifyThat(tv_Tabla, TableViewMatchers.hasNumRows(totalRows+1));
        
    }

    @Test
    public void test004_editProduct() {
        Node rowName = lookup("#tc_Name").nth(1).query();
        doubleClickOn(rowName);
        write("Prueba");
        type(KeyCode.ENTER);
        Node rowWeight = lookup("#tc_Weight").nth(1).query();
        doubleClickOn(rowWeight);
        write("5");
        type(KeyCode.ENTER);
        Node rowPrice = lookup("#tc_Price").nth(1).query();
        doubleClickOn(rowPrice);
        write("8");
        type(KeyCode.ENTER);
    }

    @Test
    public void test005_deleteProduct() {
        Node rowName = lookup("#tc_Name").nth(1).query();
        clickOn(rowName);
        clickOn("#btn_DeleteProduct");
    }

    @Test
    public void test006_goToProductManagmentFromOrderManagment() {
        clickOn("#menuManagement");
        clickOn("#menuOrders");
        clickOn("#btn_newOrder");
        verifyThat("#btn_DeleteProduct", isInvisible());
        verifyThat("#btn_CreateProduct", isInvisible());
        verifyThat("#btn_OrderCreate", isEnabled());
        verifyThat("#tf_company", isEnabled());
    }
    @Test
    public void test007_useFilterFromProduct() {
        clickOn("#tf_company");
        write("Abdulai");
        Node rowName = lookup("#tc_Name").nth(1).query();
        clickOn(rowName);
        clickOn("#btn_OrderCreate");
    }
    
}
