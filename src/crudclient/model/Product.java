package crudclient.model;

import java.io.Serializable;
import java.util.Objects;
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
    private Integer id;
    /**
     * The weight of the product.
     */
    private float weight;
    /**
     * The price of the product.
     */
    private double price;
    /**
     * The name of the product.
     */
    private String name;
    
    private Boolean checkbox;

    /**
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id the id to be set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return the weight
     */
    public float getWeight() {
        return weight;
    }

    /**
     *
     * @param weight the weight to be set
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @param price the price to be set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * User for the product.
     */
    @ManyToOne
    private User user;

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
