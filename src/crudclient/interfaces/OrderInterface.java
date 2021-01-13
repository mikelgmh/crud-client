package crudclient.interfaces;

import javax.ws.rs.ClientErrorException;

/**
 *
 * @author Imanol
 */
public interface OrderInterface {
    
    public void edit_XML(Object requestEntity) throws ClientErrorException;

    public void edit_JSON(Object requestEntity) throws ClientErrorException;

    public <T> T find_XML(Class<T> responseType, String id) throws ClientErrorException;

    public <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException;

    public <T> T findAllOrders_XML(Class<T> responseType) throws ClientErrorException;

    public <T> T findAllOrders_JSON(Class<T> responseType) throws ClientErrorException;

    public void create_XML(Object requestEntity) throws ClientErrorException;

    public void create_JSON(Object requestEntity) throws ClientErrorException;

    public <T> T findOrdersByPrice_XML(Class<T> responseType, String price) throws ClientErrorException;

    public <T> T findOrdersByPrice_JSON(Class<T> responseType, String price) throws ClientErrorException;

    public void remove(String id) throws ClientErrorException;

    public void close();
}
