package crudclient.interfaces;

import crudclient.model.Order;
import java.util.Set;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Imanol
 */
public interface OrderInterface {
    
    public Set<Order> findAllOrders(GenericType responseType) throws ClientErrorException;
    
    public void createOrder(Object requestEntity) throws ClientErrorException;
    
    public void removeOrder(String id) throws ClientErrorException;
}
