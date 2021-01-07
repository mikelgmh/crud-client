/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.interfaces;

import java.util.Set;

/**
 *
 * @author Mikel
 */
public interface User {

    /**
     * Gets a list of users.
     *
     * @return A collection of users.
     */
    public Set<User> getUsers();

    /**
     * Edits or updates a user.
     *
     * @param user
     * @return the edited user.
     */
    public User editUser(User user);

    /**
     * Deletes a user.
     *
     * @param user
     */
    public void deleteUser(User user);

    /**
     * Creates a new user.
     *
     * @param user
     */
    public void createUser(User user);
}
