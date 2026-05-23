package com.asri.prototype;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidJordanPhone(String phone) {
        return phone != null && phone.matches("07[789]\\d{7}");
    }

    public static boolean isValidDate(String date) {
        try {
            LocalDate parsed = LocalDate.parse(date, DATE_FORMATTER);
            int year = parsed.getYear();
            return year >= 1900 && !parsed.isAfter(LocalDate.now());
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public static boolean isEndDateAfterOrEqualStart(String startDate, String endDate) {
        try {
            LocalDate start = parseDate(startDate);
            LocalDate end = parseDate(endDate);
            return !end.isBefore(start);
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isPositiveNumber(String value) {
        try {
            return Double.parseDouble(value.trim()) > 0;
        } catch (Exception ex) {
            return false;
        }
    }
}
