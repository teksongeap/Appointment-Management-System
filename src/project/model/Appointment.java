package project.model;

import java.time.*;

/**
 * Represents an appointment in the application.
 * It includes information such as the appointment's ID, customer ID, user ID, contact ID,
 * title, description, location, type, and start and end times.
 *
 * @author Teksong Eap
 */
public class Appointment {
    /**
     * The appointment's ID.
     */
    private int appointmentId;

    /**
     * The customer's ID.
     */
    private int customerId;

    /**
     * The database user's ID.
     */
    private int userId;

    /**
     * The customer contact's ID.
     */
    private int contactId;

    /**
     * The appointment's title.
     */
    private String title;

    /**
     * The appointment's description.
     */
    private String description;

    /**
     * The appointment's location.
     */
    private String location;

    /**
     * The appointment's type.
     */
    private String type;

    /**
     * The appointment's start date and time in local timezone.
     */
    private LocalDateTime startDateTime;

    /**
     * The appointment's end date and time in local timezone.
     */
    private LocalDateTime endDateTime;

    /**
     * The appointment's start date in local timezone.
     */
    private LocalDate startDate;

    /**
     * The appointment's end date in local timezone.
     */
    private LocalDate endDate;

    /**
     * The appointment's start time in local timezone.
     */
    private LocalTime startTime;

    /**
     * The appointment's end time in local timezone.
     */
    private LocalTime endTime;

    /**
     * Initializes a new instance of the {@code Appointment} class.
     *
     * @param appointmentId    The ID of the appointment.
     * @param customerId       The ID of the customer for the appointment.
     * @param userId           The ID of the database user.
     * @param contactId        The contact ID for the customer.
     * @param title            The title of the appointment.
     * @param description      The description of the appointment.
     * @param location         The location of the appointment.
     * @param type             The type of the appointment.
     * @param startDateTime    The start date and time of the appointment.
     * @param endDateTime      The end date and time of the appointment.
     * @param startDate        The start date of the appointment.
     * @param endDate          The end date of the appointment.
     * @param startTime        The start time of the appointment.
     * @param endTime          The end time of the appointment.
     */
    public Appointment(int appointmentId, int customerId, int userId, int contactId, String title, String description,
                       String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime,
                       LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // getters and setters for each variable
    /**
     * Gets the appointment's ID.
     *
     * @return The appointment's ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the appointment's ID.
     *
     * @param appointmentId The appointment's ID.
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets the customer's ID.
     *
     * @return The customer's ID.
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer's ID.
     *
     * @param customerId The customer's ID.
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the database user's ID.
     *
     * @return The database user's ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the database user's ID.
     *
     * @param userId The database user's ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the contact's ID.
     *
     * @return The contact's ID.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the contact's ID.
     *
     * @param contactId The contact's ID.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets the appointment's title.
     *
     * @return The appointment's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the appointment's title.
     *
     * @param title The appointment's title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the appointment's description.
     *
     * @return The appointment's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the appointment's description.
     *
     * @param description The appointment's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the appointment's location.
     *
     * @return The appointment's location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the appointment's location.
     *
     * @param location The appointment's location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the appointment's type.
     *
     * @return The appointment's type.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the appointment's type.
     *
     * @param type The appointment's type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the appointment's local start date and time.
     *
     * @return The appointment's local start date and time.
     */
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Sets the appointment's local start date and time.
     *
     * @param startDateTime The appointment's local start date and time.
     */
    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * Gets the appointment's local end date and time.
     *
     * @return The appointment's local end date and time.
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Sets the appointment's local end date and time.
     *
     * @param endDateTime The appointment's local end date and time.
     */
    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * Gets the appointment's local start date.
     *
     * @return The appointment's local start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the appointment's local start date.
     *
     * @param startDate The appointment's local start date.
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the appointment's local end date.
     *
     * @return The appointment's local end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the appointment's local end date.
     *
     * @param endDate The appointment's local end date.
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the appointment's local start time.
     *
     * @return The appointment's local start time.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the appointment's local start time.
     *
     * @param startTime The appointment's local start time.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the appointment's local end time.
     *
     * @return The appointment's local end time.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the appointment's local end time.
     *
     * @param endTime The appointment's local end time.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Provides default syntax for appointment information.
     * @return string of customer info
     * */
    @Override
    public String toString() {
        return ("Appt: [" + appointmentId + "] | Customer: [" + customerId + "] " +
                "| Contact: [" + contactId + "] | Type: " + type + "| Start: " + startDateTime
                + " | End: " + endDateTime );
    }
}
