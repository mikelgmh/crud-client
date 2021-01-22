package crudclient.controllers;

import crudclient.client.UserRESTClient;
import crudclient.model.User;

/**
 *
 * @author Iker de la Cruz
 */
public class EmailServiceThread extends Thread {

    private User user;
    
    public EmailServiceThread(User user) {
        this.user = user;
    }

    public void run() {
        UserRESTClient userRest = new UserRESTClient();
        userRest.sendNewPassword_XML(user, user.getEmail());
    }
}
