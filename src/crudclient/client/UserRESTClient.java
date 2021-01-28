/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.client;

import crudclient.exceptions.EmailAlreadyExistsException;
import crudclient.exceptions.EmailAndUsernameAlreadyExistException;
import crudclient.exceptions.UsernameAlreadyExistsException;
import crudclient.interfaces.EmailServiceInterface;
import crudclient.interfaces.SignInInterface;
import crudclient.model.Company;
import crudclient.model.User;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import crudclient.interfaces.UserInterface;
import java.util.ResourceBundle;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:UserFacadeREST [user]<br>
 * USAGE:
 * <pre>
 *        UserRESTClient client = new UserRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Mikel
 */
public class UserRESTClient implements UserInterface, EmailServiceInterface, SignInInterface {

    private WebTarget webTarget;
    private Client client;
    private static final ResourceBundle rb = ResourceBundle.getBundle("config.config");
    private static final String BASE_URI = rb.getString("BASE_URL");

    /**
     * Empty constructor for userRESTClient.
     */
    public UserRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("user");
    }

    /**
     * Gets a list of users from the database.
     *
     * @param genericType
     * @return A list of users.
     * @throws ClientErrorException
     */
    @Override
    public List<User> getUsers(GenericType genericType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllUsers");
        return (List<User>) resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(genericType);
    }

    /**
     * Logs in a user.
     *
     * @param <T>
     * @param requestEntity
     * @param responseType
     * @return the logged user.
     * @throws ClientErrorException
     */
    @Override
    public <T> T loginUser_XML(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.path("loginUser").request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }

    /**
     * Gets all users in json format
     *
     * @param <T>
     * @param responseType
     * @return A list of users.
     * @throws ClientErrorException
     */
    public <T> T getAllUsers_JSON(GenericType<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllUsers");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Finds users by name as XML.
     *
     * @param <T>
     * @param responseType
     * @param name
     * @return List of users.
     * @throws ClientErrorException
     */
    public <T> T findUsersByName_XML(Class<T> responseType, String name) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUsersByName/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds users by name as json.
     *
     * @param <T>
     * @param responseType
     * @param name
     * @return List of users.
     * @throws ClientErrorException
     */
    public <T> T findUsersByName_JSON(Class<T> responseType, String name) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUsersByName/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Send a put request to update a user.
     *
     * @param requestEntity
     * @throws ClientErrorException
     */
    @Override
    public void editUser(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * Send a put request to update a user.
     *
     * @param requestEntity
     * @throws ClientErrorException
     */
    public void edit_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Finds users by company name.
     *
     * @param <T>
     * @param responseType
     * @param companyName
     * @return list of users.
     * @throws ClientErrorException
     */
    public <T> T findUsersByCompanyName_XML(Class<T> responseType, String companyName) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUsersByCompanyName/{0}", new Object[]{companyName}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds users by company name.
     *
     * @param <T>
     * @param responseType
     * @param companyName
     * @return list of users.
     * @throws ClientErrorException
     */
    public <T> T findUsersByCompanyName_JSON(Class<T> responseType, String companyName) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUsersByCompanyName/{0}", new Object[]{companyName}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Finds a single user in the server and returns it.
     *
     * @param <T>
     * @param responseType
     * @param id
     * @return A user.
     * @throws ClientErrorException
     */
    public <T> T find_XML(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    /**
     * Finds a single user in the server and returns it.
     *
     * @param <T>
     * @param responseType
     * @param id
     * @return A user.
     * @throws ClientErrorException
     */
    public <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    /**
     * Makes a request to inser a new user into the database.
     *
     * @param requestEntity
     * @throws ClientErrorException
     * @throws UsernameAlreadyExistsException
     * @throws EmailAlreadyExistsException
     * @throws crudclient.exceptions.EmailAndUsernameAlreadyExistException
     */
    @Override
    public void createUser(Object requestEntity) throws ClientErrorException, UsernameAlreadyExistsException, EmailAlreadyExistsException,EmailAndUsernameAlreadyExistException {
        Response response = webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        if (response.getStatus() == 401) {

            throw new UsernameAlreadyExistsException();
        }
        if (response.getStatus() == 403) {
            throw new EmailAlreadyExistsException();
        }
               if (response.getStatus() == 405) {
            throw new EmailAndUsernameAlreadyExistException();
        }

    }

    /**
     * Asks the server to send a new email to the sent email.
     *
     * @param requestEntity
     * @param email
     * @throws ClientErrorException
     */
    @Override
    public void sendNewPassword_XML(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("sendNewPassword/{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * Asks the server to send a new email to the sent email.
     *
     * @param requestEntity
     * @param email
     * @throws ClientErrorException
     */
    @Override
    public void sendNewPassword_JSON(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("sendNewPassword/{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Makes a request to recover the user's password sending an email.
     *
     * @param requestEntity
     * @param email
     * @throws ClientErrorException
     */
    @Override
    public void recoverUserPassword_XML(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("recoverUserPassword/{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     * Makes a request to recover the user's password sending an email.
     *
     * @param requestEntity
     * @param email
     * @throws ClientErrorException
     */
    @Override
    public void recoverUserPassword_JSON(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("recoverUserPassword/{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Creates a user.
     *
     * @param requestEntity
     * @throws ClientErrorException
     */
    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    /**
     * Gets the public key as xml.
     *
     * @return
     * @throws ClientErrorException
     */
    public String getPublicKey_XML() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getPublicKey");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
    }

    /**
     * Gets the public ket as json.
     *
     * @return
     * @throws ClientErrorException
     */
    public String getPublicKey_JSON() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getPublicKey");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    /**
     * Deletes a user by id.
     *
     * @param id
     * @throws ClientErrorException
     */
    @Override
    public void deleteUser(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    /**
     * Closes the rest client instance.
     */
    public void close() {
        client.close();
    }

    /**
     * Calls the getPublicKey method. The JSON one.
     *
     * @return
     */
    @Override
    public String getPublicKey() {
        return this.getPublicKey_JSON();
    }

}
