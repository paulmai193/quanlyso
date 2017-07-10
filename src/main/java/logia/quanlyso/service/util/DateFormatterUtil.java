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
		LocalDate localDate = LocalDate.parse(__stringTime, _dateFormatter);
		return localDate.atStartOfDay(DateFormatterUtil.systemZoneId());
	}

	/**
	 * From YYYYMMDD string to zoned date time.
	 *
	 * @param __stringTime the string time
	 * @return the zoned date time
	 */
	public static ZonedDateTime fromYYYYMMDDStringToZonedDateTime(String __stringTime) {
		DateTimeFormatter _dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate localDate = LocalDate.parse(__stringTime, _dateFormatter);
		return localDate.atStartOfDay(DateFormatterUtil.systemZoneId());
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
