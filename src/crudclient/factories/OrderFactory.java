/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.factories;

import crudclient.client.OrderRESTClient;
import crudclient.interfaces.OrderInterface;

/**
 *
 * @author 2dam
 */
public class OrderFactory {
    
    public OrderInterface getImplementation(){
        return new OrderRESTClient();
    }
            
}
