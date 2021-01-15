package crudclient.model;

import java.io.Serializable;
import java.util.Objects;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity representing products that a company have.
 * fields: id, weight, price, name
 * @author Aketza
 */
@XmlRootElement
public class Product implements Serializable {
    
    
     
    private static final long serialVersionUID = 1L;
    
    /**
     * Identification field for the product.
     */
    private final SimpleIntegerProperty id;
    /**
     * The weight of the product.
     */
    private SimpleFloatProperty weight;
    /**
     * The price of the product.
     */
    private SimpleDoubleProperty price;
    /**
     * The name of the product.
     */
    private SimpleStringProperty name;
    
    @ManyToOne
    private User user;
    
    
     public Product() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.weight = new SimpleFloatProperty();
        this.price = new SimpleDoubleProperty();
    }

    public Product(Integer id, Float weight, Double price, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.weight = new SimpleFloatProperty(weight);
        this.price = new SimpleDoubleProperty(price);

    }

    /**
     *
     * @return the id
     */
    public Integer getId() {
        return this.id.get();
    }

    /**
     *
     * @param id the id to be set
     */
    public void setId(Integer id) {
        this.id.set(id);
    }

    /**
     *
     * @return the weight
     */
    public float getWeight() {
        return this.weight.get();
    }

    /**
     *
     * @param weight the weight to be set
     */
    public void setWeight(float weight) {
        this.weight.set(weight);
    }

    /**
     *
     * @return the price
     */
    public double getPrice() {
        return this.price.get();
    }

    /**
     *
     * @param price the price to be set
     */
    public void setPrice(double price) {
        this.price.set(price);
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return this.name.get();
    }

    /**
     *
     * @param name the name to be set
     */
    public void setName(String name) {
        this.name.set(name);
    }
    /**
     * User for the product.
     */
    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Integer representation for Product instance.
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Compares two Products objects for equality.
     *
     * @param object The other Product object to compare to.
     * @return true if ids are equals.
     */
    
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    /**
     * Obtains a string representation of the Product.
     *
     * @return The String representing the Product.
     */
    @Override
    public String toString() {
        return "Product{" + "id=" + id + '}';
    }

}
