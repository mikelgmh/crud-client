package crudclient.util.validation;

import java.util.Collections;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * Class containing "regex", referenced from controllers to validate data
 * format.
 *
 * @author Mikel
 */
public class GenericValidations {

    /**
     * Email regexp.
     */
    public static final Color greyColor = Color.web("#686464");
    public static final Pattern EMAIL_REGEXP = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final String TXT_ENTER_VALID_EMAIL = "Type a valid email.";
    public final Pattern PASS_REGEXP = Pattern.compile("^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[@#*$%^&+=])" + "(?=\\S+$).{8,25}$");
    public static final String PASSWORD_CONDITIONS = "- Between 8 and 25 characters\n- Uppercase and Lowercase letters\n- One number and special character at least";

    public GenericValidations() {
    }

    /**
     * Adds a previously defined CSS class to a TextField.
     *
     * @param tf The TextField
     * @param cssClass The name of the CSS class
     * @param addClass True to add, false to remove
     */
    public void addClass(TextField tf, String cssClass, Boolean addClass) {
        ObservableList<String> styleClass = tf.getStyleClass();
        if (addClass) {
            styleClass.add(cssClass);
        } else {
            styleClass.removeAll(Collections.singleton(cssClass));
        }
    }

    /**
     * Validates a TextField using regexp. Sets the property passed in the
     * method to true or false.
     *
     * @param regexp The regexp
     * @param tf The TextField
     * @param value The value to validate. Not using the TextFiel's getString
     * because it might change and make undesired actions.
     * @param property The property of the validation.
     * @return Returns true if the regex matches.
     */
    public boolean regexValidator(Pattern regexp, TextField tf, String value, String property) {
        if (regexp.matcher(value).matches()) {
            tf.getProperties().put(property, true);
            return true;
        } else {
            tf.getProperties().put(property, false);
            return false;
        }
    }

    /**
     * Validates a TextField checking if the text has a minimum of characters.
     * Sets the property passed in the method to true or false.
     *
     * @param tf The TextField
     * @param minLength The minimum length
     * @param currentValue The current value of the TextField
     * @param property The property of the validation.
     * @return Returns false if the length is less than the minimum required.
     */
    public boolean minLength(TextField tf, final int minLength, String currentValue, String property) {
        if (currentValue.length() < minLength) {
            tf.getProperties().put(property, false);
            return false;
        } else {
            tf.getProperties().put(property, true);
            return true;
        }
    }

    /**
     * Limits the text introduced by te user to the desired number of
     * characters.
     *
     * @param tf The TextField
     * @param maxLength The maximum length of characters allowed.
     * @param currentValue The value of the TextField.
     */
    public void textLimiter(TextField tf, final int maxLength, String currentValue) {
        if (currentValue.length() > maxLength) {
            String s = currentValue.substring(0, maxLength);
            tf.setText(s);
        }
    }

    /**
     * Compares two password fields. Sets the passed property to true if the
     * passwords match.
     *
     * @param pf1 The first password field.
     * @param pf2 The second password field.
     * @param property The property of the validation.
     * @return True if passwords match.
     */
    public boolean comparePasswords(final PasswordField pf1, final PasswordField pf2, String property) {
        if (pf1.getText().equals(pf2.getText()) && pf1.getText().trim().isEmpty() == false) {
            pf2.getProperties().put(property, true);
            return true;
        } else {
            pf2.getProperties().put(property, false);
            return false;
        }
    }

}
