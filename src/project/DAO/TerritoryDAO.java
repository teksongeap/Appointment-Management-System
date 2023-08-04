package project.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.model.Appointment;
import project.model.Country;
import project.model.Division;
import project.utilities.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Territory DAO handles the database interactions for country and division.
 *
 * @author Teksong Eap
 */
public class TerritoryDAO {

    /**
     * Retrieves all countries from the database.
     *
     * @return an ObservableList containing all countries in the database
     * @throws SQLException if a database access error occurs or this method is
     *                      called on a closed connection
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        String SQL = "SELECT COUNTRY, COUNTRY_ID FROM COUNTRIES";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String countryName = rs.getString("COUNTRY");
                int countryId = rs.getInt("COUNTRY_ID");
                Country country = new Country(countryId, countryName);
                allCountries.add(country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return allCountries;
    }

    /**
     * Retrieves all divisions associated with a given country ID from the database.
     *
     * @param selectedCountryId the ID of the country whose divisions are to be retrieved
     * @return an ObservableList containing all divisions for the selected country in the database
     * @throws SQLException if a database access error occurs or this method is
     *                      called on a closed connection
     */
    public static ObservableList<Division> getAllDivisionsByCountryId(int selectedCountryId) throws SQLException {
        ObservableList<Division> allDivisionsByCountryId = FXCollections.observableArrayList();
        String SQL = "SELECT * FROM FIRST_LEVEL_DIVISIONS WHERE COUNTRY_ID = ?";

        try (PreparedStatement ps = JDBC.getConnection().prepareStatement(SQL)) {
            ps.setInt(1, selectedCountryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int divisionId = rs.getInt("Division_ID");
                    String divisionName = rs.getString("Division");
                    Division division = new Division(divisionId, divisionName);
                    allDivisionsByCountryId.add(division);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return allDivisionsByCountryId;
    }
}
