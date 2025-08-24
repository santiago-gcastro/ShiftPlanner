/**
 * 
 */
package es.main.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


/**
 * 
 */
public class CommonUtils {
	private CommonUtils() {}
	public static String[] split(String line, String delim) {
		return line.split(delim);
	}
	public static Date parseDate(String dateAsString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return simpleDateFormat.parse(dateAsString);
		} catch (ParseException e) {
			//me la como con patatas
		}
		return null;
	}
	public static String formatDate(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return simpleDateFormat.format(date);
	}
	public static String generateBlankLine(Integer size) {
		String[] blankLine = new String[size];
		Arrays.fill(blankLine, "");
		return String.join(CommonUtils.CSV_DELIM, Arrays.asList(blankLine));
	}
	public static final String CSV_DELIM = ";";
}
