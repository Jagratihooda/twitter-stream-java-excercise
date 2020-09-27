package com.ing.assignment.utility;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

	private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";
/*

	 * This method convert String date Time to LocalDateTime
	 * @param stringDateTime
	 * @return dateTime
*/
	public static LocalDateTime convertStringToLocalDateTime(String stringDateTime){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		return LocalDateTime.parse(stringDateTime, formatter);
	}
}
