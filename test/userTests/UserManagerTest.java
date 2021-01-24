/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userTests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import crudclient.CRUDClient;
import static org.junit.Assert.*;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author Mikel, Imanol
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserManagerTest extends ApplicationTest {

    private static final String OVERSIZED_TEXT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
    private static final String VALID_EMAIL = "validEmail@gmail.com";

    @FXML
    private TextField txt_Email;

    @FXML
    private TextField txt_Firstname;

    @FXML
    private TextField txt_Lastname;

    @FXML
    private TextField txt_Username;

    @FXML
    private TextField txt_RepeatPassword;
    @FXML
    private TextField txt_Password;

    public UserManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws TimeoutException, Exception {
        ApplicationTest.launch(CRUDClient.class);

    }

    @Override
    public void stop() {
    }

    @Test
    public void test001_LoginTest() { // Not really a test, just going to the user management screen
        clickOn("#txt_User");
        write("Mikel");
        clickOn("#txt_Password");
        write("1234$%Mm");
        clickOn("#btn_SignIn");

    }

    @Test
    public void test002_goToUserManagementScreen() { // Not really a test, just going to the user management screen
        clickOn("#menuManagement");
        clickOn("#menuUsers");

    }

    @Test
    public void test003_testNameFilterField() {
        clickOn("#txt_name");
        write("randomCharactersf56F·");

    }

}
