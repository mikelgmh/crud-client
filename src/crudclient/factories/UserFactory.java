/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.factories;

import crudclient.client.UserRESTClient;
import crudclient.interfaces.User;

/**
 *
 * @author Mikel
 */
public class UserFactory {

    public static String USERS_TO_GENERATE = "15";

    
    public enum ImplementationType {
        TEST_IMPLEMENTATION,
        REST_CLIENT
    }

    /**
     * Gets a signable implementation.
     *
     * @param implementation The type of implementation to return.
     * @return An implementation of type Signable.
     */
    public User getUserImplementation(ImplementationType implementation) {

        User selectedImplementation = null;
        switch (implementation) {
            case TEST_IMPLEMENTATION:
                selectedImplementation = new UserTestDataGenerator(Integer.parseInt(USERS_TO_GENERATE));
                break;
            case REST_CLIENT:
                selectedImplementation = new UserRESTClient();
                break;

        }
        return selectedImplementation;
    }
}
