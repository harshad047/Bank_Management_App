// src/main/java/com/tss/util/PasswordUtil.java

package com.tss.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtil {

    // Hash password using SHA-256 (optional – for future use)
    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Check if password matches hash (not used now, but ready for upgrade)
    public static boolean verify(String password, String hashed) {
        return hash(password).equals(hashed);
    }
}