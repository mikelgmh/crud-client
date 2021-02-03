/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.controllers;

import crudclient.factories.CompanyFactory;
import crudclient.factories.OrderFactory;
import crudclient.factories.ProductFactory;
import crudclient.factories.SignInFactory;
import crudclient.factories.UserFactory;
import crudclient.interfaces.CompanyInterface;
import crudclient.interfaces.EmailServiceInterface;
import crudclient.interfaces.OrderInterface;
import crudclient.interfaces.ProductInterface;
import crudclient.interfaces.SignInInterface;
import crudclient.interfaces.UserInterface;
import crudclient.model.User;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Iker de la Cruz
 */
public class DashboardController {

    private static final Logger logger = Logger.getLogger("crudclient.controllers.DashboardController");
    private Stage stage;
    private User user;
    @FXML
    private Label lbl_Welcome;
    @FXML
    private Label lbl_Connection;
    @FXML
    private Button btn_Logout;

    public static User loggedUser;
    @FXML
    private Pane paneDashboard;
    @FXML
    private ImageView imageAlmazon;
    @FXML
    private HBox hboxMenu;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuHome;
    @FXML
    private MenuItem menuLogout;
    @FXML
    private MenuItem menuClose;
    @FXML
    private Menu menuManagement;
    @FXML
    private MenuItem menuCompanies;
    @FXML
    private MenuItem menuUsers;
    @FXML
    private MenuItem menuOrders;
    @FXML
    private MenuItem menuProducts;

    public User getUser() {
        return this.user;
    }

    public DashboardController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Method to initialize the RecoverPassword stage.
     *
     * @param root The loaded view.
     */
    public void initStage(Parent root) {
        imageAlmazon.setImage(new Image("crudclient/img/ALMAZON.png"));
        logger.log(Level.INFO, "Loading the Dashboard stage.");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard");
        stage.setResizable(false);
        stage.setOnShowing(this::handleWindowShowing);
        setMenu();
        stage.show();
        logger.log(Level.INFO, "Dashboard stage loaded.");
    }

    private void handleWindowShowing(WindowEvent event) {
        lbl_Welcome.setText(user.getPassword());
        loggedUser = user;
        lbl_Welcome.setText("Welcome " + user.getName());
        lbl_Connection.setText("Last access: " + user.getLastAccessAsString());
        //setButtons();
    }

    @FXML
    private void handleOnClickLogout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        String s = "Are you sure you want to logout?";
        alert.setContentText(s);
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/SignIn.fxml"));
            Parent root = (Parent) loader.load();
            SignInController controller = ((SignInController) loader.getController());
            SignInFactory signInFactory = new SignInFactory();
            SignInInterface signInImplementation = signInFactory.getImplementation();
            controller.setImplementation(signInImplementation);
            controller.setStage(new Stage());
            controller.initStage(root);
            stage.close();
        }
    }

    private String dateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }


    private void setMenu() {
        menuLogout.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Sign Out");
                String s = "Are you sure you want to logout?";
                alert.setContentText(s);
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/SignIn.fxml"));
                        Parent root = (Parent) loader.load();
                        SignInController controller = ((SignInController) loader.getController());
                        SignInFactory signInFactory = new SignInFactory();
                        SignInInterface signInImplementation = signInFactory.getImplementation();
                        controller.setImplementation(signInImplementation);
                        controller.setStage(new Stage());
                        controller.initStage(root);
                        stage.close();
                    } catch (IOException ex) {
                        Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        menuClose.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Close confirmation");
                alert.setHeaderText("Application will be closed");
                alert.setContentText("You will close the application");
                alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get().equals(ButtonType.OK)) {
                    Platform.exit();
                } else {
                    t.consume();
                    alert.close();
                }
            }
        });

        menuUsers.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/UserManagement.fxml"));
                    Parent root = (Parent) loader.load();
                    UserManagementController controller = ((UserManagementController) loader.getController());
                    UserFactory userFactory = new UserFactory();
                    UserInterface userImplementation = userFactory.getUserImplementation(UserFactory.ImplementationType.REST_CLIENT);
                    controller.setUserImplementation(userImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuCompanies.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/companies.fxml"));
                    Parent root = (Parent) loader.load();
                    CompanyController controller = ((CompanyController) loader.getController());
                    CompanyFactory companyFactory = new CompanyFactory();
                    CompanyInterface companyImplementation = companyFactory.getImplementation();
                    controller.setImplementation(companyImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuOrders.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/orders.fxml"));
                    Parent root = (Parent) loader.load();
                    OrderManagementController controller = ((OrderManagementController) loader.getController());
                    OrderFactory orderFactory = new OrderFactory();
                    OrderInterface orderImplementation = orderFactory.getImplementation();
                    controller.setOrderImplementation(orderImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuProducts.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/product.fxml"));
                    Parent root = (Parent) loader.load();
                    ProductController controller = ((ProductController) loader.getController());
                    ProductFactory productFactory = new ProductFactory();
                    ProductInterface productImplementation = productFactory.getImplementation();
                    controller.setProductImplementation(productImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menuCompanies.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/crudclient/view/companies.fxml"));
                    Parent root = (Parent) loader.load();
                    CompanyController controller = ((CompanyController) loader.getController());
                    CompanyFactory companyFactory = new CompanyFactory();
                    CompanyInterface companyImplementation = companyFactory.getImplementation();
                    controller.setImplementation(companyImplementation);
                    controller.setStage(new Stage());
                    controller.initStage(root);
                    stage.close();
                } catch (IOException ex) {
                    Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
