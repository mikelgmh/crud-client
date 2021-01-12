/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.factories;

import crudclient.interfaces.User;
import crudclient.model.Company;
import crudclient.model.UserModel;
import crudclient.model.UserPrivilege;
import crudclient.model.UserStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Mikel
 */
public class UserTestDataGenerator implements User {

    private static final Logger logger = Logger.getLogger("crudclient");
    private ArrayList<UserModel> userList;

    /**
     * Generates users to test the client side.
     *
     * @param numberOfUsers The number of users to be generated.
     */
    public UserTestDataGenerator(Integer numberOfUsers) {
        userList = new ArrayList();
        for (int i = 0; i < numberOfUsers; i++) {
            UserModel userModel = new UserModel();
            userModel.setId(Long.valueOf(i));
            userModel.setCompany(new Company());
            userModel.setEmail(new SimpleStringProperty((String) "email" + i));
            userModel.setName(new SimpleStringProperty((String) "name" + i));
            userModel.setPrivilege(UserPrivilege.PROVIDER);
            userModel.setStatus(UserStatus.ENABLED);
            userModel.setSurname(new SimpleStringProperty((String) "Surname" + i));
            userModel.setUsername(new SimpleStringProperty((String) "username" + i));
            userList.add(userModel);
        }
    }

    @Override
    public List getUsers() {
        return userList;
    }

    @Override
    public User editUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createUser(User user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPublicKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
