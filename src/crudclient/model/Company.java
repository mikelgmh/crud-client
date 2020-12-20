package crudclient.model;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Entity representating the Companies. It contains the following fields:
 * company id, company name, company type, company localization and company
 * amount of users.
 *
 * @author Iker de la Cruz
 */

@XmlRootElement
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identification field of the Company.
     */
    private Integer id;

    /**
     * Name of the Company.
     */
    private String name;

    /**
     * Type of the Company.
     */
    private CompanyType type;

    /**
     * Localization of the Company.
     */
    private String localization;

    /**
     *
     * @return the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id the id to be set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the type.
     */
    public CompanyType getType() {
        return type;
    }

    /**
     *
     * @param type the type to set.
     */
    public void setType(CompanyType type) {
        this.type = type;
    }

    /**
     *
     * @return the localization.
     */
    public String getLocalization() {
        return localization;
    }

    /**
     *
     * @param localization the localization to set.
     */
    public void setLocalization(String localization) {
        this.localization = localization;
    }

    /**
     * Integer representation for Company instance.
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    /**
     * Compares two Company objects for equality. This method consider a Company
     * equal to another Company if their id fields have the same value.
     *
     * @param obj The other Company object to compare to.
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
        final Company other = (Company) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    /**
     * Obtain a string representation of the Company.
     *
     * @return The String representation of the Company.
     */
    @Override
    public String toString() {
        return "Company{" + "id=" + id + '}';
    }

}
