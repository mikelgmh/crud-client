package crudclient.interfaces;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iker de la Cruz
 */
public interface CompanyInterface {

    public <T> T findAllCompanies_XML(GenericType<T> responseType) throws ClientErrorException;

    public void remove(String id) throws ClientErrorException;

    public void create_XML(Object requestEntity) throws ClientErrorException;
    
    public void edit_XML(Object requestEntity) throws ClientErrorException;

}
