// src/main/java/com/tss/util/PasswordUtil.java

package com.tss.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordUtil {

    private static final int SALT_LENGTH_BYTES = 16;
    private static final int HASH_LENGTH_BITS = 256;
    private static final int DEFAULT_ITERATIONS = 10000;

    public static String hash(String password) {
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = pbkdf2(password.toCharArray(), salt, DEFAULT_ITERATIONS, HASH_LENGTH_BITS);
        String saltB64 = Base64.getEncoder().encodeToString(salt);
        String hashB64 = Base64.getEncoder().encodeToString(hash);
        return "PBKDF2$" + DEFAULT_ITERATIONS + "$" + saltB64 + "$" + hashB64;
    }

    public static boolean verify(String password, String stored) {
        if (stored == null) return false;
        if (stored.startsWith("PBKDF2$")) {
            String[] parts = stored.split("\\$");
            if (parts.length != 4) return false;
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);
            byte[] actualHash = pbkdf2(password.toCharArray(), salt, iterations, expectedHash.length * 8);
            if (actualHash.length != expectedHash.length) return false;
            int diff = 0;
            for (int i = 0; i < actualHash.length; i++) diff |= actualHash[i] ^ expectedHash[i];
            return diff == 0;
        }
        // Fallback for legacy plaintext storage
        return password.equals(stored);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLengthBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }
}