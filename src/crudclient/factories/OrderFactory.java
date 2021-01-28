package crudclient.factories;

import crudclient.client.OrderRESTClient;
import crudclient.interfaces.OrderInterface;

/**
 * Order factory to catch the implementation on controller
 * @author Imanol
 */
public class OrderFactory {
    
    public OrderInterface getImplementation(){
        return new OrderRESTClient();
    }
            
}
