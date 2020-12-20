package crudclient.model;

/**
 * Company types for Company.
 *
 * @author Iker de la Cruz
 */
public enum CompanyType {
    /**
     * Type of Company used to manage the Companies and the Company's users.
     */
    ADMIN,
    /**
     * Type of Company used to consult the Company's pending orders and
     * Company's products.
     */
    PROVIDER,
    /**
     * Type of Company used to consult the orders and order's details.
     */
    CLIENT;
}
