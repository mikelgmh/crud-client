/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.exceptions;

/**
 *
 * @author Usuario
 */
public class ServerConnectionException extends Exception {
    
    public ServerConnectionException() {
        super("Can´t connect with database (no connection).");
    }
}
