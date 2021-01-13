/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.factories;

import crudclient.client.ProductRESTClient;
import crudclient.interfaces.ProductInterface;

/**
 *
 * @author 2dam
 */
public class ProductFactory {
    
    public ProductInterface getImplementation(){
       return (ProductInterface) new ProductRESTClient();
    }
}
