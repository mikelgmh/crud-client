package crudclient.factories;

import crudclient.client.CompanyRESTClient;
import crudclient.interfaces.CompanyInterface;

/**
 *
 * @author Iker de la Cruz
 */
public class CompanyFactory {

    public CompanyInterface getImplementation() {
        return new CompanyRESTClient();
    }

}
