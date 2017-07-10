package logia.quanlyso.service.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The Class DateFormatterUtil.
 * 
 * @author logia193
 */
public class DateFormatterUtil {

	/**
	 * From DDMMYYYY string to zoned date time.
	 *
	 * @param __stringTime the string time
	 * @return the zoned date time
	 */
	public static ZonedDateTime fromDDMMYYYYStringToZonedDateTime(String __stringTime) {
		DateTimeFormatter _dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate _localDate = LocalDate.parse(__stringTime, _dateFormatter);
		return _localDate.atStartOfDay(DateFormatterUtil.systemZoneId());
	}

	/**
	 * From YYYYMMDD string to zoned date time.
	 *
	 * @param __stringTime the string time
	 * @return the zoned date time
	 */
	public static ZonedDateTime fromYYYYMMDDStringToZonedDateTime(String __stringTime) {
		DateTimeFormatter _dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate _localDate = LocalDate.parse(__stringTime, _dateFormatter);
		return _localDate.atStartOfDay(DateFormatterUtil.systemZoneId());
	}

	/**
	 * From date time to string DDMMYYYY.
	 *
	 * @param __time the time
	 * @return the string
	 */
	public static String fromDateTimeToStringDDMMYYYY(ZonedDateTime __time) {
		return __time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
	}

	/**
	 * From date time to string YYYYMMDD.
	 *
	 * @param __time the time
	 * @return the string
	 */
	public static String fromDateTimeToStringYYYYMMDD(ZonedDateTime __time) {
		return __time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	/**
	 * System zone id.
	 *
	 * @return the zone id
	 */
	public static ZoneId systemZoneId() {
		return ZoneId.of("Asia/Ho_Chi_Minh");
	}
}
