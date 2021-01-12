/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mikel
 */
@XmlRootElement
public class UserModel implements Serializable {

    public UserModel(Long id, SimpleStringProperty username, SimpleStringProperty email, SimpleStringProperty name, SimpleStringProperty surname, UserStatus status, UserPrivilege privilege, String password, Timestamp lastAccess, Timestamp lastPasswordChange, Company company) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.status = status;
        this.privilege = privilege;
        this.password = password;
        this.lastAccess = lastAccess;
        this.lastPasswordChange = lastPasswordChange;
        this.company = company;
    }

    public UserModel() {
    }
    
    

    private static final long serialVersionUID = 1L;
    /**
     * The auto generated id of the user.
     */
    private Long id;
    /**
     * The username of the user.
     */
    private SimpleStringProperty username;
    /**
     * The email of the user.
     */
    private SimpleStringProperty email;
    /**
     * The name of the user.
     */
    private SimpleStringProperty name;
    /**
     * The Surname of the user
     */
    private SimpleStringProperty surname;

    /**
     * The status of the user. Enum.
     */
    private UserStatus status;
    /**
     * The privilege of the user.
     */
    private UserPrivilege privilege;
    /**
     * The password of the user.
     */
    private String password;
    /**
     * The last access of the user.
     */
    private Timestamp lastAccess;
    /**
     * The last passsword change that has been made for this user.
     */
    private Timestamp lastPasswordChange;

    /**
     * The company object where this user belongs.
     */
    @ManyToOne()
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public SimpleStringProperty getUsername() {
        return username;
    }

    public void setUsername(SimpleStringProperty username) {
        this.username = username;
    }

    public SimpleStringProperty getEmail() {
        return email;
    }

    public void setEmail(SimpleStringProperty email) {
        this.email = email;
    }

    public SimpleStringProperty getName() {
        return name;
    }

    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    public SimpleStringProperty getSurname() {
        return surname;
    }

    public void setSurname(SimpleStringProperty surname) {
        this.surname = surname;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserPrivilege getPrivilege() {
        return privilege;
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege = privilege;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Timestamp lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Timestamp getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Timestamp lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final UserModel other = (UserModel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + '}';
    }

    

}

