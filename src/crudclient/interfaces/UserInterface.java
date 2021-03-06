/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.interfaces;

import crudclient.exceptions.EmailAlreadyExistsException;
import crudclient.exceptions.EmailAndUsernameAlreadyExistException;
import crudclient.exceptions.UsernameAlreadyExistsException;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Mikel
 */
public interface UserInterface {

    /**
     * Gets a list of users.
     *
     * @param genericType
     * @return A collection of users.
     */
    public List getUsers(GenericType genericType) throws ClientErrorException;

    /**
     * Edits or updates a user.
     *
     * @param user
     */
    public void editUser(Object user) throws ClientErrorException;

    /**
     * Deletes a user.
     *
     */
    public void deleteUser(String userId);

    /**
     * Creates a new user.
     *
     * @param user The entity to insert. User in this case.
     */
    public void createUser(Object user) throws ClientErrorException, UsernameAlreadyExistsException, EmailAlreadyExistsException, EmailAndUsernameAlreadyExistException;

    /**
     * Gets the public encryption key.
     *
     * @return The public key.
     */
    public String getPublicKey();
}
