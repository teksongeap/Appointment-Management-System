package project.model;

/**
 * This class represents a Customer in the application.
 *
 * @author Teksong Eap
 */
public class Customer {
    /**
     * The ID of the customer.
     */
    private int customerId;

    /**
     * The ID of the first level division.
     */
    private int divisionId;

    /**
     * The ID of the country.
     */
    private int countryId;

    /**
     * The name of the customer.
     */
    private String customerName;

    /**
     * The address of the customer.
     */
    private String address;

    /**
     * The postal code of the customer.
     */
    private String postalCode;

    /**
     * The phone number of the customer.
     */
    private String phone;

    /**
     * The name of the country of the customer.
     */
    private String countryName;

    /**
     * The name of the first level division of the customer.
     */
    private String divisionName;

    /**
     * Constructor method for the Customer class.
     * @param customerId ID of the customer.
     * @param divisionId ID of the division.
     * @param countryId ID of the country.
     * @param customerName Name of the customer.
     * @param address Address of the customer.
     * @param postalCode Postal code of the customer.
     * @param phone Phone number of the customer.
     * @param countryName Name of the country.
     * @param divisionName Name of the division.
     */
    public Customer(int customerId, int divisionId, int countryId, String customerName, String address, String postalCode, String phone, String countryName, String divisionName) {
        this.customerId = customerId;
        this.divisionId = divisionId;
        this.countryId = countryId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.countryName = countryName;
        this.divisionName = divisionName;
    }

    // Getters and Setters
    /**
     * Returns the ID of the customer.
     * @return customer ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the ID of the customer.
     * @param customerId ID of the customer.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /**
     * Returns the ID of the division.
     * @return division ID.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the ID of the division.
     * @param divisionId ID of the division.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Returns the ID of the country.
     * @return country ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the ID of the country.
     * @param countryId ID of the country.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Returns the name of the customer.
     * @return customer name.
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the name of the customer.
     * @param customerName name of the customer.
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Returns the address of the customer.
     * @return customer address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the customer.
     * @param address address of the customer.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns the postal code of the customer.
     * @return customer postal code.
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code of the customer.
     * @param postalCode postal code of the customer.
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Returns the phone number of the customer.
     * @return customer phone number.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the customer.
     * @param phone phone number of the customer.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns the country name of the customer.
     * @return customer's country name.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the country name of the customer.
     * @param countryName country name of the customer.
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Returns the division name of the customer.
     * @return customer's division name.
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Sets the division name of the customer.
     * @param divisionName division name of the customer.
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * Provides default syntax for customer information.
     * @return string of customer info
     * */
    @Override
    public String toString() {
        return ("[" + Integer.toString(customerId) + "] " + customerName);
    }
}
