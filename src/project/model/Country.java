package project.model;

/**
 * Represents a Country with a unique identifier and name.
 *
 * @author Teksong Eap
 */
public class Country {
    /**
     * Country ID
     */
    private int countryId;

    /**
     * Country Name
     */
    private String countryName;

    /**
     * Creates a new Country with the given ID and name.
     *
     * @param countryId the unique identifier of the country
     * @param countryName the name of the country
     */
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /**
     * Returns the ID of the Country.
     * @return id
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the ID of the Country.
     * @param countryId country id
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Returns the name of the Country.
     * @return name
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the name of the Country.
     * @param countryName
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Returns the name of the Country.
     * @return string rep
     */
    @Override
    public String toString() {
        return countryName;
    }
}

