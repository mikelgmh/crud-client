package crudclient.model;

/**
 *
 * @author Imanol
 */

//Order types of status: Requested, sending, received or managed.
public enum OrderStatus {
    /*
        This status is used when the client ceates one order
    */
    REQUESTED, 
    /*
        This status is used if the provider has received the order and 
    shipped the products.
    */
    SENDING, 
    /*
        This status is used if the client has received the package.
    */
    RECEIVED, 
    /*
        This status is used when the order is managed by the client
    */
    MANAGED;
}
