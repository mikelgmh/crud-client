/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.factories;

import crudclient.model.Company;
import crudclient.model.CompanyType;
import crudclient.model.User;
import crudclient.model.UserPrivilege;
import crudclient.model.UserStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import crudclient.interfaces.UserInterface;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Mikel
 */
public class UserTestDataGenerator implements UserInterface {

    private static final Logger logger = Logger.getLogger("crudclient");
    private ArrayList<User> userList;

    /**
     * Generates users to test the client side.
     *
     * @param numberOfUsers The number of users to be generated.
     */
    public UserTestDataGenerator(Integer numberOfUsers) {
        userList = new ArrayList();
        for (int i = 0; i < numberOfUsers; i++) {
            User userModel = new User();
            userModel.setId(Long.valueOf(i));
            userModel.setCompany(new Company("Compañia " + i, CompanyType.ADMIN, "Ciudad " + i));
            userModel.setEmail("email " + i);
            userModel.setName("name " + i);
            userModel.setPrivilege(UserPrivilege.PROVIDER);
            userModel.setStatus(UserStatus.ENABLED);
            userModel.setSurname("Surname " + i);
            userModel.setUsername("username " + i);
            userList.add(userModel);
        }
    }

    @Override
    public List getUsers(GenericType gt) throws ClientErrorException {
        return userList;
    }


    @Override
    public String getPublicKey() {
        return "Test Public key";
    }

    @Override
    public void createUser(Object user) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteUser(String userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editUser(Object user) throws ClientErrorException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
