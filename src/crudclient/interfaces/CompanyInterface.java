package crudclient.interfaces;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iker de la Cruz
 */
public interface CompanyInterface {

    public <T> T findAllCompanies_XML(GenericType<T> responseType) throws ClientErrorException, InternalServerErrorException;

    public void remove(String id) throws ClientErrorException, InternalServerErrorException;

    public void create_XML(Object requestEntity) throws ClientErrorException, InternalServerErrorException;

    public void edit_XML(Object requestEntity) throws ClientErrorException, InternalServerErrorException;

}
