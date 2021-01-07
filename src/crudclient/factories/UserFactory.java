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
        /**
     * Gets a signable implementation.
     *
     * @return An implementation of type Signable.
     */
    public User getUserImplementation() {
        return new UserRESTClient();
    }
}
