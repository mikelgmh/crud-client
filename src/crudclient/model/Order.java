package crudclient.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Imanol
 */
/**
 * Class containing all the orders and the orderProduct of which it consists. It
 contains the following fields: order identification, product identification,
 total price of the order and total quantity of orderProduct.
 */
@XmlRootElement
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    public Order(){
        this.status = new SimpleObjectProperty<OrderStatus>();
    }
            
    public Order(OrderStatus status){
        this.status= new SimpleObjectProperty(status);
    }
            
    private Integer id;
    private Date date;
    private Float total_price;
    private SimpleObjectProperty<OrderStatus> status;
    private List<OrderProduct> products;
    private User user;

    //Getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Float total_price) {
        this.total_price = total_price;
    }

    @XmlElement(name="products")
    public List<OrderProduct> getOrderProduct() {
        return products;
    }

    public void setOrderProduct(List<OrderProduct> products) {
        this.products = products;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return this.status.get();
    }

    public void setStatus(OrderStatus status) {
        this.status.set(status);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Order other = (Order) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + '}';
    }

    

}
