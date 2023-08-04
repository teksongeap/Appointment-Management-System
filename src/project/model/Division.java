package project.model;

/**
 * Represents a geographical division.
 *
 * @author Teksong Eap
 */
public class Division {
    private int divisionId;
    private String divisionName;

    /**
     * Creates a new Division with the given ID and name.
     *
     * @param divisionId the division's ID
     * @param divisionName the name of the division
     */
    public Division(int divisionId, String divisionName) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
    }

    /**
     * Returns the division's ID.
     * @return id
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the division's ID.
     * @param divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Returns the division's name.
     * @return  name
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Sets the division's name.
     * @param divisionName
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * Returns a string representation of the division, including its name.
     * @return string rep
     */
    @Override
    public String toString() {
        return divisionName;
    }
}

