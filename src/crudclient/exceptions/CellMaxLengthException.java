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
public class CellMaxLengthException extends Exception {

    public CellMaxLengthException() {
        super("The max length of the cell has been surpassed.");
    }
}
