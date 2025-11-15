package br.com.ldr.devshe;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.ldr.devshe.exceptions.ServiceError;

public class UtilData {
	
	private UtilData() {
        throw new ServiceError("Classe utilitaria");
    }
	
    /**
     * @return yyyy-MM-dd'T'HH:mm:ss
     */
	public static SimpleDateFormat isoDateTime() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	}
	
    /**
     * @return yyyy-MM-dd
     */
	public static SimpleDateFormat isoDate() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
    /**
     * @return dd/MM/yyyy
     */
	public static SimpleDateFormat formatoDataBr() {
        return new SimpleDateFormat("dd/MM/yyyy");
    }


	private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	private static final String DEFAULT_DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

	// -------------------------------------------------------------
	// LocalDate <-> String
	// -------------------------------------------------------------
	public static LocalDate stringToLocalDate(String data) {
		return stringToLocalDate(data, DEFAULT_DATE_FORMAT);
	}

	public static LocalDate stringToLocalDate(String data, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDate.parse(data, formatter);
	}

	public static String localDateToString(LocalDate date) {
		return localDateToString(date, DEFAULT_DATE_FORMAT);
	}

	public static String localDateToString(LocalDate date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return date.format(formatter);
	}

	// -------------------------------------------------------------
	// LocalDateTime <-> String
	// -------------------------------------------------------------
	public static LocalDateTime stringToLocalDateTime(String dataHora) {
		return stringToLocalDateTime(dataHora, DEFAULT_DATETIME_FORMAT);
	}

	public static LocalDateTime stringToLocalDateTime(String dataHora, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(dataHora, formatter);
	}

	public static String localDateTimeToString(LocalDateTime dateTime) {
		return localDateTimeToString(dateTime, DEFAULT_DATETIME_FORMAT);
	}

	public static String localDateTimeToString(LocalDateTime dateTime, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return dateTime.format(formatter);
	}
}
