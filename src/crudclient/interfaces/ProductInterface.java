/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.interfaces;

import crudclient.model.Product;
import java.util.List;
import java.util.Set;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author 2dam
 */
public interface ProductInterface {
    

    public void edit_XML(Object requestEntity) throws ClientErrorException;

    public void edit_JSON(Object requestEntity) throws ClientErrorException;

    public <T> T find_XML(Class<T> responseType, String id) throws ClientErrorException;

    public <T> T find_JSON(Class<T> responseType, String id) throws ClientErrorException;

    public void create_XML(Object requestEntity) throws ClientErrorException;

    public void create_JSON(Object requestEntity) throws ClientErrorException;

    public void remove(String id) throws ClientErrorException;

   public List<Product> findAllProducts_XML(GenericType responseType) throws ClientErrorException;

    public <T> T findAllProducts_JSON(Class<T> responseType) throws ClientErrorException;
    
    public <T> T findProductsByName_XML(Class<T> responseType, String name) throws ClientErrorException;

    public <T> T findProductsByName_JSON(Class<T> responseType, String name) throws ClientErrorException;
    
    public <T> T findProductByCompany_XML(Class<T> responseType, String company) throws ClientErrorException;

    public <T> T findProductByCompany_JSON(Class<T> responseType, String company) throws ClientErrorException;
    
    public List getProducts();
    
     public void close();

}
