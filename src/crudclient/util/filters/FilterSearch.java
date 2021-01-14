/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudclient.util.filters;

import crudclient.model.Company;
import crudclient.model.Order;
import crudclient.model.Product;
import crudclient.model.User;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Observable;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author Mikel
 */
public class FilterSearch {

    private TableView tableView;
    private ArrayList<BindedProperty> bindedProperties = new ArrayList<>();
    private ObservableList observableModelList;
    private ObservableList observableModelListCopy;

    public FilterSearch() {
    }

    public ObservableList getObservableModelListCopy() {
        return observableModelListCopy;
    }

    public void setObservableModelListCopy(ObservableList observableModelListCopy) {
        this.observableModelListCopy = observableModelListCopy;
    }

    public void addBindedProperty(BindedProperty bp) {
        this.bindedProperties.add(bp);
    }

    public ArrayList<BindedProperty> getBindedProperties() {
        return bindedProperties;
    }

    public void setBindedProperties(ArrayList<BindedProperty> bindedProperties) {
        this.bindedProperties = bindedProperties;
    }

    public ObservableList getObservableModelList() {
        return observableModelList;
    }

    public void observableModelList(ObservableList observableUserList) {
        this.observableModelList = observableUserList;
    }

    public TableView getTableView() {
        return tableView;
    }

    public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }

    private Object getParsedObject(Object objectToParse, Object obj) {

        // if(bindedProperties.get(index).getSubModelProperty() != null)
        switch (objectToParse.getClass().toString()) {
            case "class crudclient.model.User":
                obj = (User) objectToParse;
                break;
            case "class crudclient.model.Company":
                obj = (Company) objectToParse;
                break;
            case "class crudclient.model.Product":
                obj = (Product) objectToParse;
                break;
            case "class crudclient.model.Order":
                obj = (Order) objectToParse;
                break;
            default:
                obj = objectToParse.toString();
        }
        return obj;
    }

    public void filter() {
        ArrayList<Object> deleteList = new ArrayList<>();
        for (int i = 0; i < observableModelList.size(); i++) {
            System.out.println("Size del array: " + observableModelList.size());
            System.out.println("Pasando por: " + observableModelList.get(i));
            Object obj = new Object();
            obj = getParsedObject(observableModelList.get(i), obj);
            ArrayList<Boolean> foundList = new ArrayList<>();
            for (int x = 0; x < bindedProperties.size(); x++) {
                String modelPropertyValue = getGetterValueAsString(x, obj);
                String controlValue = getControlValue(bindedProperties.get(x).getControl());
                foundList.add(compareValues(controlValue, modelPropertyValue));
            }
            boolean found = true;
            int y = 0;
            while (found && y < foundList.size()) {
                if (foundList.get(y) == false) {
                    found = false;
                }
                y++;
            }
            if (!found) {
                // System.out.println("Eliminado: " + observableModelList.get(i));
                deleteList.add(observableModelList.get(i));

            }
            // System.out.println(found);
        }
        for (int i = 0; i < deleteList.size(); i++) {
            observableModelList.remove(deleteList.get(i));
        }
        
    }

    public String getControlValue(Control control) {
        // System.out.println(control.getClass().toString());
        String text = "";
        switch (control.getClass().toString()) {
            case "class javafx.scene.control.TextField":
                TextField tf = new TextField();
                tf = (TextField) control;
                text = tf.getText();

                break;
        }
        return text;

    }

    public boolean compareValues(String filterFieldValue, String modelValue) {
        return modelValue.contains(filterFieldValue);
    }

    public String getGetterValueAsString(int index, Object object) {
        String valueToReturn = "";
        try {
            String propertyFirstLetterUppercase = bindedProperties.get(index).getModelProperty().substring(0, 1).toUpperCase() + bindedProperties.get(index).getModelProperty().substring(1);
            Method method = object.getClass().getMethod("get" + propertyFirstLetterUppercase, null);
            if (bindedProperties.get(index).getSubModelProperty() != null) {
                // object =;
                Object object2 = new Object();
                object2 = getParsedObject(method.invoke(object, null), object2);

                String propertyFirstLetterUppercase2 = bindedProperties.get(index).getSubModelProperty().substring(0, 1).toUpperCase() + bindedProperties.get(index).getSubModelProperty().substring(1);
                Method method2 = object2.getClass().getMethod("get" + propertyFirstLetterUppercase2, null);
                valueToReturn = method2.invoke(object2, null).toString();
            } else {
                valueToReturn = method.invoke(object, null).toString();
            }

        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FilterSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(FilterSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(FilterSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(FilterSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(FilterSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valueToReturn;
    }

}
