package crudclient.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author Imanol
 */

/*
    This entity contains the relation between the entities Order and Products. It contains 
    the following fields: order identification and product identification.
*/

//Makes the entity embeddable 
@Embeddable
public class OrderProductId implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    //Identification field for the order
    private Integer orderId;
    
    //Identification field for the product
    private Integer productId;

    //Empty constructor
    public OrderProductId() {
    }
    
    //Constructor
    public OrderProductId(Integer orderId, Integer productId){
        this.orderId=orderId;
        this.productId=productId;
    }

    //Getters and setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderProductId other = (OrderProductId) obj;
        if (!Objects.equals(this.orderId, other.orderId)) {
            return false;
        }
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrderProductId{" + "orderId=" + orderId + ", productId=" + productId + '}';
    }
    
    
}