/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.util.filters;

import javafx.scene.control.Control;

/**
 *
 * @author Mikel
 */
public class BindedProperty {

    private Control control;
    private String modelProperty;
    private String subModelProperty;

    public BindedProperty(String modelProperty, String subModelProperty, Control control) {
        this.modelProperty = modelProperty;
        this.subModelProperty = subModelProperty;
        this.control = control;
    }

    public BindedProperty(String modelProperty, Control control) {
        this.modelProperty = modelProperty;
        this.control = control;
    }

    public String getSubModelProperty() {
        return subModelProperty;
    }

    public void setSubModelProperty(String subModelProperty) {
        this.subModelProperty = subModelProperty;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public String getModelProperty() {
        return modelProperty;
    }

    public void setModelProperty(String modelProperty) {
        this.modelProperty = modelProperty;
    }

}
