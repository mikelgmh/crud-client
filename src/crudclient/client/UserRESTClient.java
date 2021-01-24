/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.client;

import crudclient.exceptions.EmailAlreadyExistsException;
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
    private static final String BASE_URI = "http://localhost:8080/CRUD-Server/webresources";

    public UserRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("user");
    }

    @Override
    public List<User> getUsers(GenericType genericType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllUsers");
        return (List<User>) resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(genericType);
    }
    
    @Override
    public <T> T loginUser_XML(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.path("loginUser").request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML), responseType);
    }
    
    public <T> T getAllUsers_JSON(GenericType<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllUsers");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findUsersByName_XML(Class<T> responseType, String name) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUsersByName/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findUsersByName_JSON(Class<T> responseType, String name) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUsersByName/{0}", new Object[]{name}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public void editUser(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public void edit_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public <T> T findUsersByCompanyName_XML(Class<T> responseType, String companyName) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUsersByCompanyName/{0}", new Object[]{companyName}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findUsersByCompanyName_JSON(Class<T> responseType, String companyName) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUsersByCompanyName/{0}", new Object[]{companyName}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T find_XML(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    @Override
    public void createUser(Object requestEntity) throws ClientErrorException,UsernameAlreadyExistsException, EmailAlreadyExistsException {
        Response response = webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        if (response.getStatus() == 401) {

            throw new UsernameAlreadyExistsException();
        }
        if (response.getStatus() == 403) {
           throw new EmailAlreadyExistsException();
        }

    }
    
    @Override
    public void sendNewPassword_XML(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("sendNewPassword/{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    @Override
    public void sendNewPassword_JSON(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("sendNewPassword/{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }
    
    @Override
    public void recoverUserPassword_XML(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("recoverUserPassword/{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    @Override
    public void recoverUserPassword_JSON(Object requestEntity, String email) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("recoverUserPassword/{0}", new Object[]{email})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }
    
    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public String getPublicKey_XML() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getPublicKey");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(String.class);
    }

    public String getPublicKey_JSON() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getPublicKey");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    @Override
    public void deleteUser(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }
    
    public void close() {
        client.close();
    }

    @Override
    public String getPublicKey() {
        return this.getPublicKey_JSON();
    }

    @Override
    public List getAllCompanies(GenericType genericType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("company/findAllCompanies");
        return (List<Company>) resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(genericType);
    }

}