package crudclient.interfaces;

import crudclient.model.User;
import javax.ws.rs.ClientErrorException;

/**
 *
 * @author Iker de la Cruz
 */
public interface SignInInterface {

    public String getPublicKey();
    
    public <T> T loginUser_XML(Object requestEntity, Class<T> responseType) throws ClientErrorException;

}
