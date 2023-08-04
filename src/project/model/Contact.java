package project.model;

/**
 * Represents a Contact with a unique identifier and name.
 *
 * @author Teksong Eap
 */
public class Contact {
    /** contact ID */
    private int contactId;

    /** contact name */
    private String contactName;

    /**
     * Creates a new Contact with the given ID and name.
     *
     * @param contactId the unique identifier of the contact
     * @param contactName the name of the contact
     */
    public Contact(int contactId, String contactName) {
        this.contactId = contactId;
        this.contactName = contactName;
    }

    /**
     * Returns the ID of the Contact.
     * @return int
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the ID of the Contact.
     * @param contactId
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Returns the name of the Contact.
     * @return contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the name of the Contact.
     * @param contactName
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Returns a string representation of the Contact, including its ID and name.
     * @return string rep
     */
    @Override
    public String toString() {
        return "[" + contactId + "] " + contactName;
    }
}

