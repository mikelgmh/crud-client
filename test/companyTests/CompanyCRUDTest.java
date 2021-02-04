package companyTests;

import crudclient.CRUDClient;
import crudclient.model.Company;
import crudclient.model.CompanyType;
import java.util.concurrent.TimeoutException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;

import javafx.stage.Stage;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;

import org.junit.FixMethodOrder;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.base.NodeMatchers.isNull;
import static org.testfx.matcher.base.NodeMatchers.isInvisible;
import static org.testfx.matcher.base.NodeMatchers.isNotFocused;
import static org.testfx.matcher.base.NodeMatchers.isFocused;
import static org.testfx.matcher.base.NodeMatchers.isNotNull;
import static org.testfx.matcher.control.ListViewMatchers.isEmpty;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author Iker de la Cruz
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CompanyCRUDTest extends ApplicationTest {

    @FXML
    private Button btnCreateCompany;
    @FXML
    private Button btnDeleteCompany;
    @FXML
    private TextField tfNameFilter;
    @FXML
    private ComboBox<CompanyType> cbTypeFilter;
    @FXML
    private TextField tfLocalizationFilter;
    @FXML
    private TableView<Company> tableViewCompanies;
    @FXML
    private TableColumn<Company, String> tcNameCompany;
    @FXML
    private TableColumn<Company, CompanyType> tcTypeCompany;
    @FXML
    private TableColumn<Company, String> tcLocalizationCompany;

    @BeforeClass
    public static void setUpClass() throws TimeoutException, Exception {
        ApplicationTest.launch(CRUDClient.class);
    }

    /**
     * Test to login in the application with a correct user.
     */
    @Test
    public void testALoginCorrectUser() {
        clickOn("#txt_User");
        write("mikel");
        clickOn("#txt_Password");
        write("1234$%Mm");
        clickOn("#btn_SignIn");
    }

    /**
     * Test to navigate to the companies stage with the menu bar.
     */
    @Test
    public void testBNavigateToCompaniesStage() {
        clickOn("#menuManagement");
        clickOn("#menuCompanies");
    }

    /**
     * Test to check the initial components of the companies stage.
     */
    @Test
    public void testCCheckInitialStageCompanies() {
        verifyThat("#tfNameFilter", hasText(""));
        verifyThat("#tfLocalizationFilter", hasText(""));
        verifyThat("#btnCreateCompany", isEnabled());
        verifyThat("#btnDeleteCompany", isDisabled());
    }

    /**
     * Test to create a company.
     */
    @Test
    public void testDCreateCompany() {
        tableViewCompanies = lookup("#tableViewCompanies").queryTableView();
        int oldSize = tableViewCompanies.getItems().size();
        clickOn("#btnCreateCompany");
        int newSize = tableViewCompanies.getItems().size();
        assertTrue(newSize > oldSize);
    }

    /**
     * Test to modify a company.
     */
    @Test
    public void testEModifyCompany() {
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        Node tcNameCompany = lookup("#tcNameCompany").nth(1).query();
        doubleClickOn(tcNameCompany);
        write("asdasdf");
        this.push(KeyCode.ENTER);
        verifyThat(tcNameCompany, isNotNull());
        Node tcTypeCompany = lookup("#tcTypeCompany").nth(1).query();
        doubleClickOn(tcTypeCompany);
        this.push(KeyCode.UP);
        this.push(KeyCode.ENTER);
        verifyThat(tcTypeCompany, isNotNull());
        Node tcLocalizationCompany = lookup("#tcLocalizationCompany").nth(1).query();
        doubleClickOn(tcLocalizationCompany);
        write("wefuibeiouwfbou");
        this.push(KeyCode.ENTER);
        verifyThat(tcLocalizationCompany, isNotNull());
    }

    /**
     * Test to delete a company.
     */
    @Test
    public void testFDeleteCompany() {
        tableViewCompanies = lookup("#tableViewCompanies").queryTableView();
        int oldSize = tableViewCompanies.getItems().size();
        clickOn("#btnDeleteCompany");
        clickOn("Aceptar");
        int newSize = tableViewCompanies.getItems().size();
        assertTrue(newSize < oldSize);
    }
}
