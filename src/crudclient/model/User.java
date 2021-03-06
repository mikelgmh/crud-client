/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mikel
 */
@XmlRootElement
public class User implements Serializable {

    public User() {
        this.email = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.surname = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
        this.status = new SimpleObjectProperty();
        this.privilege = new SimpleObjectProperty();
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
    private SimpleObjectProperty<UserStatus> status;
    /**
     * The privilege of the user.
     */
    private SimpleObjectProperty<UserPrivilege> privilege;
    /**
     * The password of the user.
     */
    private String password;
    /**
     * The last access of the user.
     */
    private Date lastAccess;
    /**
     * The last passsword change that has been made for this user.
     */
    private Date lastPasswordChange;

    /**
     * The company object where this user belongs.
     */
    private Company company;

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public UserStatus getStatus() {
        return status.get();
    }

    public void setStatus(UserStatus status) {
        this.status.set(status);
    }

    public UserPrivilege getPrivilege() {
        return privilege.get();
    }

    public void setPrivilege(UserPrivilege privilege) {
        this.privilege.set(privilege);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public String getLastAccessAsString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(lastAccess);
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
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
        final User other = (User) obj;
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
