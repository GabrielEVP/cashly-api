package com.cashly.cashly_api.shared.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.function.Supplier;

/**
 * Utility class for common controller operations and validations
 */
public class ControllerUtils {

    private ControllerUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Validates that a user ID is not null or empty
     * @param userId the user ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUserId(String userId) {
        return userId != null && !userId.trim().isEmpty();
    }

    /**
     * Validates that a string parameter is not null
     * @param parameter the parameter to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidParameter(String parameter) {
        return parameter != null;
    }

    /**
     * Safely parses a YearMonth from a string
     * @param monthParam the month parameter as string
     * @return the parsed YearMonth or null if invalid
     */
    public static YearMonth parseYearMonth(String monthParam) {
        try {
            return YearMonth.parse(monthParam);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Safely parses a LocalDate from a string
     * @param dateParam the date parameter as string
     * @return the parsed LocalDate or null if invalid
     */
    public static LocalDate parseLocalDate(String dateParam) {
        try {
            return LocalDate.parse(dateParam);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Safely parses an integer from a string
     * @param intParam the integer parameter as string
     * @return the parsed integer or null if invalid
     */
    public static Integer parseInteger(String intParam) {
        try {
            return Integer.parseInt(intParam);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Executes a service operation and returns the result
     * Exceptions are handled by GlobalExceptionHandler
     * @param serviceCall the service method to execute
     * @param <T> the response type
     * @return ResponseEntity with the result
     */
    public static <T> ResponseEntity<T> executeServiceCall(Supplier<T> serviceCall) {
        T result = serviceCall.get();
        return ResponseEntity.ok(result);
    }
}