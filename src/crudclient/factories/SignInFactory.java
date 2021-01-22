package crudclient.factories;

import crudclient.client.UserRESTClient;
import crudclient.interfaces.SignInInterface;

/**
 *
 * @author Iker de la Cruz
 */
public class SignInFactory {

    public SignInInterface getImplementation() {
        return new UserRESTClient();
    }

}
