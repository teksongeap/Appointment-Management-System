package project.DAO;
import project.controller.LoginController;
import project.model.TypeAndMonthReport;
import project.utilities.AlertMessage;
import project.utilities.JDBC;
import project.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * Handles the database interactions for Appointment objects.
 *
 * @author Teksong Eap
 */
public class AppointmentDAO {

    /** Date-time formatter */
    private static final DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Local time zone ID */
    private static final ZoneId localZoneID = ZoneId.systemDefault();

    /** UTC time zone ID */
    private static final ZoneId utcZoneID = ZoneId.of("UTC");

    /**
     * Retrieves an unused appointment ID from the database. This is the highest current ID plus one.
     *
     * @return an unused appointment ID
     * @throws SQLException if a database access error occurs
     */
    public static int getUnusedAppointmentId() throws SQLException {
        String SQL = "SELECT MAX(Appointment_ID) AS MaxAppointmentID FROM appointments";
        int maxAppointmentId = 0;

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {
            if(rs.next()) {
                maxAppointmentId = rs.getInt("MaxAppointmentID");
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return maxAppointmentId + 1;
    }

    /**
     * Adds a new appointment to the database.
     *
     * The method sets up a SQL PreparedStatement to execute the INSERT operation,
     * and sets the parameters of the PreparedStatement using the provided Appointment object's data.
     * The method then executes the SQL statement and checks the returned row count to determine whether the operation was successful.
     *
     * @param appointment the appointment to add to the database
     * @return true if the operation was successful (i.e., at least one row was affected); false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean addAppointmentToDB(Appointment appointment) throws SQLException {
        System.out.println(appointment);
        String SQL = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Contact_ID, Customer_ID, User_ID, Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int rowsAffected = 0;
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime()));
            ps.setInt(7, appointment.getContactId());
            ps.setInt(8, appointment.getCustomerId());
            ps.setInt(9, appointment.getUserId());
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(11, LoginController.getCurrentUser().getUsername());
            ps.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(13, LoginController.getCurrentUser().getUsername());
            rowsAffected = ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return rowsAffected > 0;
    }

    /**
     * Updates an existing appointment in the database.
     * The method sets up a SQL PreparedStatement to execute the UPDATE operation,
     * and sets the parameters of the PreparedStatement using the provided Appointment object's data.
     * The method then executes the SQL statement and checks the returned row count to determine whether the operation was successful.
     *
     * @param appointment the appointment with updated information
     * @return true if the operation was successful (i.e., at least one row was affected); false otherwise
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public static boolean updateAppointmentInDB(Appointment appointment) throws SQLException {
        String SQL = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Contact_ID = ?, Customer_ID = ?, User_ID = ?, Last_Update = ?, Last_Updated_By = ? WHERE Appointment_ID = ?";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime()));
            ps.setInt(7, appointment.getContactId());
            ps.setInt(8, appointment.getCustomerId());
            ps.setInt(9, appointment.getUserId());
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));  // setting Last_Update to now
            ps.setString(11, LoginController.getCurrentUser().getUsername());  // setting Last_Updated_By to current user
            ps.setInt(12, appointment.getAppointmentId());

            int rowsUpdated = ps.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Checks whether a new or updated appointment overlaps with existing appointments for the same customer.
     * The method sets up a SQL PreparedStatement to execute a SELECT operation that retrieves appointments for
     * the same customer that overlap with the provided newAppointment's start and end times.
     * If the ResultSet returned by the query is not empty, there is an overlapping appointment.
     *
     * @param newAppointment the new or updated appointment to check for overlap
     * @return true if there is an overlapping appointment; false otherwise
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public static boolean checkForOverlap(Appointment newAppointment) throws SQLException {
        String SQL = "SELECT * FROM appointments WHERE Appointment_ID != ? AND Customer_ID = ? AND ((Start < ? AND End > ?) OR (Start < ? AND End > ?) OR (Start >= ? AND End <= ?))";
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, newAppointment.getAppointmentId());
            ps.setInt(2, newAppointment.getCustomerId());
            ps.setTimestamp(3, Timestamp.valueOf(newAppointment.getStartDateTime()));
            ps.setTimestamp(4, Timestamp.valueOf(newAppointment.getStartDateTime()));
            ps.setTimestamp(5, Timestamp.valueOf(newAppointment.getEndDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(newAppointment.getEndDateTime()));
            ps.setTimestamp(7, Timestamp.valueOf(newAppointment.getStartDateTime()));
            ps.setTimestamp(8, Timestamp.valueOf(newAppointment.getEndDateTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // If the result set is not empty, it means there is an overlapping appointment
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        // If the result set is empty, there is no overlapping appointment
        return false;
    }

    /**
     * Retrieves all appointments from the database.
     *
     * @return an ObservableList containing all appointments in the database
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return getAppointmentsWithQuery("SELECT * FROM APPOINTMENTS");
    }

    /**
     * Retrieves all appointments occurring in the current month from the database.
     *
     * @return an ObservableList containing all appointments in the current month
     */
    public static ObservableList<Appointment> getAllAppointmentsThisMonth() {
        YearMonth currentYearMonth = YearMonth.now();
        LocalDate startDate = currentYearMonth.atDay(1);
        LocalDate endDate = currentYearMonth.atEndOfMonth();
        String selectAppointmentsThisMonthQuery = "SELECT * FROM APPOINTMENTS WHERE DATE(start) BETWEEN ? AND ? ORDER BY CAST(APPOINTMENT_ID AS unsigned)";
        return getAppointmentsWithQuery(selectAppointmentsThisMonthQuery, startDate.toString(), endDate.toString());
    }

    /**
     * Retrieves all appointments occurring in the current week from the database.
     *
     * @return an ObservableList containing all appointments in the current week
     */
    public static ObservableList<Appointment> getAllAppointmentsThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        String selectAppointmentsThisWeekQuery = "SELECT * FROM APPOINTMENTS WHERE DATE(start) BETWEEN ? AND ? ORDER BY CAST(APPOINTMENT_ID AS unsigned)";
        return getAppointmentsWithQuery(selectAppointmentsThisWeekQuery, startOfWeek.toString(), endOfWeek.toString());
    }

    /**
     * Retrieves all appointments for a specific contact from the database.
     *
     * @param selectedContactId the ID of the contact whose appointments are to be retrieved
     * @return an ObservableList containing all appointments for the selected contact
     */
    public static ObservableList<Appointment> getAppointmentsByContact(int selectedContactId) {
        String selectAppointmentsByContactQuery = "SELECT * FROM APPOINTMENTS WHERE Contact_ID = ?";
        return getAppointmentsWithQuery(selectAppointmentsByContactQuery, Integer.toString(selectedContactId));
    }

    /**
     * Retrieves appointments from the database using a specified query.
     * The method sets up a SQL PreparedStatement using the provided query and parameters,
     * then executes the query and uses the returned ResultSet to create Appointment objects and add them to an ObservableList.
     *
     * @param query the SQL query to execute
     * @param parameters the parameters to use in the query
     * @return an ObservableList containing the appointments resulting from the query
     */
    private static ObservableList<Appointment> getAppointmentsWithQuery(String query, String... parameters) {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) {
                ps.setString(i + 1, parameters[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointment newAppointment = createAppointmentFromResultSet(rs);
                    allAppointments.add(newAppointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allAppointments;
    }

    /**
     * Creates an Appointment object from a ResultSet.
     * This method extracts data from the ResultSet and uses it to create a new Appointment object,
     * which is then returned.
     *
     * @param rs the ResultSet to convert
     * @return an Appointment object with the data from the ResultSet
     * @throws SQLException if a database access error occurs
     */
    private static Appointment createAppointmentFromResultSet(ResultSet rs) throws SQLException {
        int appointmentID = rs.getInt(1);
        String appointmentTitle = rs.getString(2);
        String appointmentDescription = rs.getString(3);
        String appointmentLocation = rs.getString(4);
        String appointmentType = rs.getString(5);
        int customerID = rs.getInt(12);
        int userID = rs.getInt(13);
        int contactID = rs.getInt(14);
        LocalDateTime utcStartDT = LocalDateTime.parse(rs.getString(6), datetimeDTF);
        LocalDateTime utcEndDT = LocalDateTime.parse(rs.getString(7), datetimeDTF);
        LocalDateTime localStartDT = utcStartDT.atZone(utcZoneID).withZoneSameInstant(localZoneID).toLocalDateTime();
        LocalDateTime localEndDT = utcEndDT.atZone(utcZoneID).withZoneSameInstant(localZoneID).toLocalDateTime();
        LocalDate localStartDate = localStartDT.toLocalDate();
        LocalDate localEndDate = localEndDT.toLocalDate();
        LocalTime localStartTime = localStartDT.toLocalTime();
        LocalTime localEndTime = localEndDT.toLocalTime();
        return new Appointment(appointmentID, customerID, userID, contactID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, localStartDT, localEndDT, localStartDate, localEndDate, localStartTime, localEndTime);
    }

    /**
     * Retrieves a report from the database showing the number of appointments by type and month.
     * The method executes a SQL SELECT operation that groups appointments by type and month,
     * and uses the ResultSet to create TypeAndMonthReport objects and add them to an ObservableList.
     *
     * @return an ObservableList containing the report data
     */
    public static ObservableList<TypeAndMonthReport> getAppointmentCountByTypeAndMonth() {
        ObservableList<TypeAndMonthReport> reports = FXCollections.observableArrayList();

        String SQL = "SELECT MONTHNAME(Start) AS Month, Type, COUNT(*) AS Count " +
                "FROM Appointments " +
                "GROUP BY Month, Type";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String month = rs.getString("month");
                    String type = rs.getString("type");
                    int count = rs.getInt("count");
                    TypeAndMonthReport report = new TypeAndMonthReport(type, month, count);
                    reports.add(report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reports;
    }

    /**
     * Retrieves the upcoming appointment for a user from the database.
     * The method executes a SQL SELECT operation that retrieves appointments for the specified user
     * that start within the next 15 minutes. If such an appointment exists,
     * the ResultSet is used to create an Appointment object, which is then returned.
     *
     * @param userId the ID of the user whose upcoming appointment is to be retrieved
     * @return the upcoming Appointment object for the user, or null if no upcoming appointment exists
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public static Appointment getUpcomingAppointment(int userId) throws SQLException {
        String SQL = "SELECT * FROM Appointments WHERE user_id = ? AND Start BETWEEN ? AND ?";

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime in15Minutes = now.plusMinutes(15);

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(now));
            ps.setTimestamp(3, Timestamp.valueOf(in15Minutes));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int appointmentId = rs.getInt("Appointment_ID");
                    int customerId = rs.getInt("Customer_ID");
                    int contactId = rs.getInt("Contact_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                    LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                    LocalDate startDate = startDateTime.toLocalDate();
                    LocalDate endDate = endDateTime.toLocalDate();
                    LocalTime startTime = startDateTime.toLocalTime();
                    LocalTime endTime = endDateTime.toLocalTime();

                    return new Appointment(appointmentId, customerId, userId, contactId, title, description,
                            location, type, startDateTime, endDateTime, startDate, endDate, startTime, endTime);
                }
            }
        }
        return null;
    }


    /**
     * Deletes an appointment from the database.
     * The method sets up a SQL PreparedStatement to execute a DELETE operation,
     * and sets the ID of the appointment to delete as the parameter of the PreparedStatement.
     * The method then executes the SQL statement and checks the returned row count to determine whether the operation was successful.
     *
     * @param selectedAppointmentId the ID of the appointment to delete
     * @return true if the operation was successful (i.e., at least one row was affected); false otherwise
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public static boolean deleteAppointmentFromDB(int selectedAppointmentId) throws SQLException {
        String SQL = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
        ps.setInt(1, selectedAppointmentId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    }

}
