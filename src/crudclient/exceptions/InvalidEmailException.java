/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.exceptions;

/**
 *
 * @author Mikel
 */
public class InvalidEmailException extends Exception {

    public InvalidEmailException() {
        super("The email format is not correct.");
    }
}
