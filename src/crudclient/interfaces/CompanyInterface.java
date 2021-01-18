package crudclient.interfaces;

import crudclient.model.Company;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Iker de la Cruz
 */
public interface CompanyInterface {

    public <T> T findAllCompanies_XML(GenericType<T> responseType) throws ClientErrorException;
    
    public List<Company> getCompanies();

}