// src/main/java/com/tss/util/Validator.java

package com.tss.util;

public class Validator {

    // Validate email format (basic)
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    // Validate phone number (10 digits only)
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.matches("^[0-9]{10}$");
    }

    // Validate username (alphanumeric, 3-20 chars)
    public static boolean isValidUsername(String username) {
        if (username == null) return false;
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    // Validate password (at least 6 characters)
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Validate account type
    public static boolean isValidAccountType(String type) {
        return "SAVINGS".equalsIgnoreCase(type) || "CURRENT".equalsIgnoreCase(type);
    }

    // Sanitize input to prevent XSS (basic)
    public static String sanitize(String input) {
        if (input == null) return null;
        return input.replaceAll("<", "<").replaceAll(">", ">");
    }
}