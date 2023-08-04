package project.model;

/**
 * Represents a monthly report with appointment types and counts.
 *
 * @author Teksong Eap
 */
public class TypeAndMonthReport {
    private String month;
    private String type;
    private int count;

    /**
     * Creates a new Report with the given month, type, and count.
     *
     * @param type the type of the appointments
     * @param month the month of the appointments
     * @param count the number of appointments of the given type in the given month
     */
    public TypeAndMonthReport( String type, String month, int count) {
        this.type = type;
        this.month = month;
        this.count = count;
    }

    /**
     * Returns the month of the appointments in the report.
     * @return month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the month of the appointments in the report.
     * @param month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Returns the type of the appointments in the report.
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the appointments in the report.
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the count of the appointments in the report.
     * @return count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the count of the appointments in the report.
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Returns a string representation of the report, including its month, type, and count.
     * @return string rep
     */
    @Override
    public String toString() {
        return "Report: " + month + " " + type + " " + count;
    }
}
