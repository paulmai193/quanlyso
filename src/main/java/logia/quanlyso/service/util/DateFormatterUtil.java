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
	 * From DDMMYYY string to zoned date time.
	 *
	 * @param __stringTime the string time
	 * @return the zoned date time
	 */
	public static ZonedDateTime fromDDMMYYYStringToZonedDateTime(String __stringTime) {
		DateTimeFormatter _dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate localDate = LocalDate.parse(__stringTime, _dateFormatter);
		return localDate.atStartOfDay(ZoneId.systemDefault());
	}
}
