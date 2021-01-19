/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.factories;

import crudclient.client.UserRESTClient;
import crudclient.interfaces.EmailServiceInterface;

/**
 *
 * @author Iker de la Cruz
 */
public class EmailServiceFactory {

    public EmailServiceInterface getImplementation() {
        return new UserRESTClient();
    }

}
