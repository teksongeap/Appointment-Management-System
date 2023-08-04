package project.utilities;
import java.time.*;

/**
 * Time Utility class for all your time-related needs. Converts ZonedDateTime to eastern time.
 * Checks if within business hours.
 *
 * @author Teksong Eap
 */
public final class TimeUtil {
    /** zone id for eastern time */
    private static final ZoneId EASTERN_TIME = ZoneId.of("America/New_York");
    /** start of business day as LocalTime */
    private static final LocalTime START_OF_BUSINESS_DAY = LocalTime.of(8, 0);  // 8:00 AM
    /** end of business day as LocalTime */
    private static final LocalTime END_OF_BUSINESS_DAY = LocalTime.of(22, 0);  // 10:00 PM

    /**
     * Private constructor
     */
    private TimeUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts to eastern time
     * @param dateTime time to convert
     * @return converted zoned date time
     */
    public static ZonedDateTime convertToEasternTime(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(EASTERN_TIME);
    }

    /**
     * Checks if within business hours
     * @param localDateTime time to check
     * @return true if within, false if not
     */
    public static boolean isWithinBusinessHours(LocalDateTime localDateTime) {
        ZonedDateTime easternTime = convertToEasternTime(localDateTime);
        LocalTime time = easternTime.toLocalTime();
        return !time.isBefore(START_OF_BUSINESS_DAY) && !time.isAfter(END_OF_BUSINESS_DAY);
    }
}
