package uk.gov.di.test.utils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {
    private static final String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPERCASE = CHAR_LOWERCASE.toUpperCase();
    private static final String CHAR_DIGIT = "0123456789";
    private static final String CHAR_SPECIAL = "!@#$%^";
    private static final SecureRandom random = new SecureRandom();

    public String generatePassword(int length) {

        // Password will contain at least 1 uppercase, 1 lowercase, 1 digit, 1 special character
        String password =
                generateRandomString(CHAR_UPPERCASE, 1)
                        + generateRandomString(CHAR_LOWERCASE, 1)
                        + generateRandomString(CHAR_DIGIT, 1)
                        + generateRandomString(CHAR_SPECIAL, 1)
                        + generateRandomString(CHAR_LOWERCASE, length - 4);

        return shuffleString(password);
    }

    private String generateRandomString(String input, int size) {

        if (size < 1) {
            throw new IllegalArgumentException("Invalid size.");
        }

        StringBuilder result = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            int index = random.nextInt(input.length());
            result.append(input.charAt(index));
        }
        return result.toString();
    }

    private String shuffleString(String input) {
        List<String> result = Arrays.asList(input.split(""));
        Collections.shuffle(result);
        return String.join("", result);
    }
}
