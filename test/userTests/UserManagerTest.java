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
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import crudclient.CRUDClient;
import crudclient.model.User;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import static org.testfx.matcher.control.ButtonMatchers.isCancelButton;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import static org.junit.Assert.*;
import static org.testfx.matcher.control.ButtonMatchers.isDefaultButton;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;

import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;

import static org.testfx.matcher.base.NodeMatchers.isDisabled;

/**
 *
 * @author Mikel
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserManagerTest extends ApplicationTest {

    private static final String OVERSIZED_TEXT = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX ";
    private static final String VALID_EMAIL = "validEmail@gmail.com";
    private static final boolean VALDIATE_MAX_LENGTH = false;

    @FXML
    private TextField txt_name;
    @FXML
    private TextField txt_surname;
    @FXML
    private TextField txt_username;
    @FXML
    private TextField txt_email;
    @FXML
    private TextField txt_company;
    @FXML
    private DatePicker txt_lastAccess;
    @FXML
    private ChoiceBox chb_status;
    @FXML
    private ChoiceBox chb_privilege;
    @FXML
    private TableView<User> table;

    @FXML
    private TextField c_txt_firstname;
    @FXML
    private TextField c_txt_lastname;
    @FXML
    private TextField c_txt_username;
    @FXML
    private TextField c_txt_email;
    @FXML
    private TextField c_txt_company;
    @FXML
    private PasswordField c_txt_password;
    @FXML
    private PasswordField c_txt_repeatPassword;
    @FXML
    private ChoiceBox c_chb_status;
    @FXML
    private ChoiceBox c_chb_privilege;
    @FXML
    private ChoiceBox c_chb_company;

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
    public void test002_goToUserManagementScreenAndVerifyButtonStatus() { // Check that the delete button is disabled
        clickOn("#menuManagement");
        clickOn("#menuUsers");
        verifyThat("#btn_delete", isDisabled());
        verifyThat("#btn_create", isEnabled());
    }

    @Test
    public void test003_filterTextFieldValidations() {
        if (VALDIATE_MAX_LENGTH) {
            // FILTERS
            // Name
            txt_name = lookup("#txt_name").query();
            clickOn("#txt_name");
            write(OVERSIZED_TEXT);
            assertTrue(txt_name.getText().length() <= 255);
            txt_name.clear();

            // Surname
            txt_surname = lookup("#txt_surname").query();
            clickOn("#txt_surname");
            write(OVERSIZED_TEXT);
            assertTrue(txt_surname.getText().length() <= 255);
            txt_surname.clear();

            // Username
            txt_username = lookup("#txt_username").query();
            clickOn("#txt_username");
            write(OVERSIZED_TEXT);
            assertTrue(txt_username.getText().length() <= 255);
            txt_username.clear();

            // Email
            txt_email = lookup("#txt_email").query();
            clickOn("#txt_email");
            write(OVERSIZED_TEXT);
            assertTrue(txt_email.getText().length() <= 255);
            txt_email.clear();

            // Company
            txt_company = lookup("#txt_company").query();
            clickOn("#txt_company");
            write(OVERSIZED_TEXT);
            assertTrue(txt_company.getText().length() <= 255);
            txt_company.clear();

            chb_status = lookup("#chb_status").query();
            chb_privilege = lookup("#chb_privilege").query();
            assertTrue(chb_status.getValue() != null);
            assertTrue(chb_privilege.getValue() != null);

        }

    }

    @Test
    public void test004_datePickerValidations() {
        txt_lastAccess = lookup("#txt_lastAccess").query();
        assertTrue(txt_lastAccess.getValue() == null);
    }

    @Test
    public void test005_datePickerValidations() {
        txt_lastAccess = lookup("#txt_lastAccess").query();
        assertTrue(txt_lastAccess.getValue() == null);
    }

    @Test
    public void test006_tableDataNotNullValidation() {
        table = lookup("#table").queryTableView();
        int rowCount = table.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
    }

    @Test
    public void test007_firstRowValidations() {
        // The first row is ALWAYS the SUPERUSER user with the id 1
        Node row = lookup(".table-row-cell").nth(0).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        verifyThat("#btn_delete", isDisabled());
        verifyThat("#btn_create", isEnabled());

    }

    @Test
    public void test008_secondRowValidations() {
        // The second row must enable the delete button
        Node row = lookup(".table-row-cell").nth(1).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        verifyThat("#btn_delete", isEnabled());
        verifyThat("#btn_create", isEnabled());
    }

    @Test
    public void test009_userCreationWindowValidationAndMaxLength() {
        // The second row must enable the delete button
        clickOn("#btn_create");
        if (VALDIATE_MAX_LENGTH) {
            // FILTERS
            // Name
            c_txt_firstname = lookup("#txt_firstname").query();
            clickOn("#txt_firstname");
            write(OVERSIZED_TEXT);
            assertTrue(c_txt_firstname.getText().length() <= 255);
            c_txt_firstname.clear();

            // Surname
            c_txt_lastname = lookup("#txt_lastname").query();
            clickOn("#txt_lastname");
            write(OVERSIZED_TEXT);
            assertTrue(c_txt_lastname.getText().length() <= 255);
            c_txt_lastname.clear();

            // Email
            c_txt_email = lookup("#txt_email_create").query();
            clickOn("#txt_email_create");
            write(OVERSIZED_TEXT);
            assertTrue(c_txt_email.getText().length() <= 255);
            c_txt_email.clear();

            // Username
            c_txt_username = lookup("#txt_username_create").query();
            clickOn("#txt_username_create");
            write(OVERSIZED_TEXT);
            assertTrue(c_txt_username.getText().length() <= 255);
            c_txt_username.clear();

            // Password
            c_txt_password = lookup("#txt_password").query();
            clickOn("#txt_password");
            write(OVERSIZED_TEXT);
            assertTrue(c_txt_password.getText().length() <= 255);
            c_txt_password.clear();

            // Repeat password
            c_txt_repeatPassword = lookup("#txt_repeatPassword").query();
            clickOn("#txt_repeatPassword");
            write(OVERSIZED_TEXT);
            assertTrue(c_txt_repeatPassword.getText().length() <= 255);
            c_txt_repeatPassword.clear();
        }
    }

    @Test
    public void test010_firstnameValidationTest() {
        clickOn("#txt_firstname");
        write("Fr");
        c_txt_firstname = lookup("#txt_firstname").query();
        assertTrue(Boolean.parseBoolean(c_txt_firstname.getProperties().get("minLengthValidator").toString()) == false);
        eraseText(2);
        clickOn("#txt_firstname");
        write("Francisco");
        assertTrue(Boolean.parseBoolean(c_txt_firstname.getProperties().get("minLengthValidator").toString()) == true);
        verifyThat("#btn_create_create", isDisabled());
    }

    @Test
    public void test011_lastnameValidationTest() {
        c_txt_lastname = lookup("#txt_lastname").query();
        clickOn("#txt_lastname");
        write("Fe");
        assertTrue(Boolean.parseBoolean(c_txt_lastname.getProperties().get("minLengthValidator").toString()) == false);
        eraseText(2);
        write("Fernández");
        assertTrue(Boolean.parseBoolean(c_txt_lastname.getProperties().get("minLengthValidator").toString()) == true);
        verifyThat("#btn_create_create", isDisabled());
    }

    @Test
    public void test012_usernameValidationTest() {
        clickOn("#txt_username_create");
        write("Mi");
        c_txt_username = lookup("#txt_username_create").query();
        assertTrue(Boolean.parseBoolean(c_txt_username.getProperties().get("minLengthValidator").toString()) == false);
        eraseText(2);
        write("Mikel");
        assertTrue(Boolean.parseBoolean(c_txt_username.getProperties().get("minLengthValidator").toString()) == true);
        verifyThat("#btn_create_create", isDisabled());
    }

    @Test
    public void test013_emailValidationTest() {
        clickOn("#txt_email_create");
        write("wrongEmailformat@eeee");
        c_txt_email = lookup("#txt_email_create").query();
        assertTrue(Boolean.parseBoolean(c_txt_email.getProperties().get("emailValidator").toString()) == false);
        c_txt_email.clear();
        clickOn("#txt_email_create");
        write("mikelgmh@gmail.com");
        assertTrue(Boolean.parseBoolean(c_txt_email.getProperties().get("emailValidator").toString()) == true);
        verifyThat("#btn_create_create", isDisabled());
    }

    @Test
    public void test014_choiceBoxSelectionTest() {
        c_chb_status = lookup("#chb_status").query();
        c_chb_privilege = lookup("#chb_privilege").query();
        c_chb_company = lookup("#chb_company").query();
        assertTrue(c_chb_status.getValue() != null);
        assertTrue(c_chb_privilege.getValue() != null);
        assertTrue(c_chb_company.getValue() == null);

        clickOn("#chb_company");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);

        assertTrue(c_chb_company.getValue() != null);
        verifyThat("#btn_create_create", isDisabled());
    }

    @Test
    public void test015_passwordMissmatchErrorTest() {
        c_txt_password = lookup("#txt_password").query();
        clickOn("#txt_password");
        write("1234$%M");
        c_txt_repeatPassword = lookup("#txt_repeatPassword").query();
        clickOn("#txt_repeatPassword");
        write("1234$%");
        assertTrue(c_txt_password.getText().equals(c_txt_repeatPassword.getText()) == false);
        verifyThat("#btn_create_create", isDisabled());
    }

    @Test
    public void test016_passwordsNotFulfillingRequirementsTest() {
        c_txt_password = lookup("#txt_password").query();
        c_txt_repeatPassword = lookup("#txt_repeatPassword").query();

        clickOn("#txt_password");
        eraseText(8);
        write("1234$%M");
        clickOn("#txt_repeatPassword");
        eraseText(8);
        write("1234$%M");

        assertTrue(c_txt_password.getText().equals(c_txt_repeatPassword.getText()) == true);
        assertTrue(Boolean.parseBoolean(c_txt_repeatPassword.getProperties().get("passwordRequirements").toString()) == false);
        verifyThat("#btn_create_create", isDisabled());
    }

    @Test
    public void test017_validPasswordTest() {
        c_txt_password = lookup("#txt_password").query();
        c_txt_repeatPassword = lookup("#txt_repeatPassword").query();
        clickOn("#txt_password");
        eraseText(8);
        write("1234$%Mm");
        clickOn("#txt_repeatPassword");
        eraseText(8);
        write("1234$%Mm");
        assertTrue(c_txt_password.getText().equals(c_txt_repeatPassword.getText()) == true);
        assertTrue(Boolean.parseBoolean(c_txt_password.getProperties().get("passwordRequirements").toString()) == true);
        verifyThat("#btn_create", isEnabled());
        clickOn("#btn_create_create");
    }

    @Test
    public void test018_changeRepeatedEmailTest() {
        verifyThat("#btn_create_create", isDisabled());
        clickOn("#txt_email_create");
        eraseText(25);
        write("randomEmail@gmail.com");
        verifyThat("#btn_create_create", isEnabled());
        clickOn("#btn_create_create");

    }

    @Test
    public void test019_changeRepeatedUsernameTest() {
        verifyThat("#btn_create_create", isDisabled());
        clickOn("#txt_username_create");
        eraseText(5);
        write("randomUser");
        verifyThat("#btn_create_create", isEnabled());
        clickOn("#btn_create_create");
        clickOn(isDefaultButton());
    }

    @Test
    public void test021_testTableCellsMaxLength() {
        if (VALDIATE_MAX_LENGTH) {
            testColumnLength("tc_name");
            testColumnLength("tc_surname");
            testColumnLength("tc_email");
            testColumnLength("tc_username");
        }
    }

    @Test
    public void test022_testClickableChoiceboxes() {
        if (VALDIATE_MAX_LENGTH) {
            choiceboxColumnTest("tc_company");
            choiceboxColumnTest("tc_status");
            choiceboxColumnTest("tc_company");
        }
    }

    @Test
    public void test023_testRepeatedEmail() {
        table = lookup("#table").queryTableView();
        int rowCount = table.getItems().size();
        Node node = lookup("#tc_email").nth(rowCount).query();
        clickOn(node);
        node = lookup("#tc_email").nth(rowCount).query();
        clickOn(node);
        eraseText(30);
        write("mikelgmh@gmail.com");
        type(KeyCode.ENTER);
        verifyThat("The email already exists, pick another email.", isVisible());
        clickOn(isDefaultButton());
    }

    @Test
    public void test024_testRepeatedUsername() {
        table = lookup("#table").queryTableView();
        int rowCount = table.getItems().size();
        Node node = lookup("#tc_username").nth(rowCount).query();
        clickOn(node);
        node = lookup("#tc_username").nth(rowCount).query();
        clickOn(node);
        eraseText(30);
        write("mikel");
        type(KeyCode.ENTER);
        verifyThat("The username already exists, pick another username.", isVisible());
        clickOn(isDefaultButton());
    }

    @Test
    public void test025_testColumnUpdate() {
        updateColumnTest("tc_name");
        updateColumnTest("tc_surname");
        updateColumnTest("tc_username");
        updateColumnTest("tc_email");
    }

    @Test
    public void test026_testEmailFormatError() {
        table = lookup("#table").queryTableView();
        int rowCount = table.getItems().size();
        Node node = lookup("#tc_email").nth(rowCount).query();
        clickOn(node);
        node = lookup("#tc_email").nth(rowCount).query();
        clickOn(node);
        eraseText(50);
        for (int i = 0; i < 30; i++) {
            type(KeyCode.DELETE);
        }
        write("wrongEmail");
        type(KeyCode.ENTER);
        verifyThat("The email format is invalid.", isVisible());
        clickOn(isDefaultButton());
    }

    public void writeColumnText() {
        write(OVERSIZED_TEXT);
        type(KeyCode.ENTER);
        verifyThat("The max length allowed for this field is 254", isVisible());
        clickOn(isDefaultButton());
    }

    public void testColumnLength(String column) {
        table = lookup("#table").queryTableView();
        int rowCount = table.getItems().size();
        Node node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        writeColumnText();
    }

    public void updateColumnTest(String column) {
        table = lookup("#table").queryTableView();
        int rowCount = table.getItems().size();
        Node node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        eraseText(25);
        write("NewValue");
        type(KeyCode.ENTER);
        verifyThat("#btn_delete", isEnabled());
    }

    public void choiceboxColumnTest(String column) {
        table = lookup("#table").queryTableView();
        int rowCount = table.getItems().size();
        Node node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        node = lookup("#" + column).nth(rowCount).query();
        clickOn(node);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("#btn_delete", isEnabled());
    }

    @Test
    public void test099_deleteLastItem() {
        table = lookup("#table").queryTableView();
        int rowCount = table.getItems().size();
        assertNotEquals("Table has no data: Cannot test.",
                rowCount, 0);
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        assertNotNull("Row is null: table has not that row. ", row);
        clickOn(row);
        clickOn("#btn_delete");
        verifyThat("If you are sure you want to delete this user, click OK. This will delete every single data related to this user. Be sure if you really want to delete it!.",
                isVisible());
        clickOn(isCancelButton());
        clickOn("#btn_delete");
        clickOn(isDefaultButton());

    }
}
