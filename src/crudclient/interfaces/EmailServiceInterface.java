/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.interfaces;

import javax.ws.rs.ClientErrorException;

/**
 *
 * @author Iker de la Cruz
 */
public interface EmailServiceInterface {

    public void sendNewPassword_XML(Object requestEntity, String email) throws ClientErrorException;

    public void sendNewPassword_JSON(Object requestEntity, String email) throws ClientErrorException;

    public void recoverUserPassword_XML(Object requestEntity, String email) throws ClientErrorException;

    public void recoverUserPassword_JSON(Object requestEntity, String email) throws ClientErrorException;

}
