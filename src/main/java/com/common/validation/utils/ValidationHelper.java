package com.common.validation.utils;

import java.util.List;
import java.util.regex.Pattern;

public class ValidationHelper {
    //Contains at least one special characters
    public static boolean validateContainsSpecialCharacter(String input) {
        return input.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");
    }

    //Check if email is a Valid Email
    public static boolean validateValidEmail(String input) {
        return input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    //Check if input is within the length
    public static boolean validateLength(String input, int min, int max) {
        return input.length() >= min && input.length() <= max;
    }

    //Check SQL injection
    public static String validateNoSQLInjection(String input) {
        // Patterns for SQL injection detection
        String[] sqlInjectionPatterns = {
            "--", ";", "/*", "*/", "@@", "@",
            "char", "nchar", "varchar", "nvarchar",
            "alter", "begin", "cast", "create", "cursor", "declare", "delete",
            "drop", "end", "exec", "execute", "fetch", "insert", "kill",
            "select", "sys", "sysobjects", "syscolumns", "table", "update"
        };

        for (String pattern : sqlInjectionPatterns) {
            if (input.toLowerCase().contains(pattern)) {
                return pattern;
            }
        }
        return "";
    }

    //CHECK Profane words
    public static boolean validateProfanity(String input, List<String> profanityWords) {
        if (input == null) {
            return true; // No input to validate
        }
        String lowercaseInput = input.toLowerCase();
        for (String word : profanityWords) {
            String regex = "\\b" + Pattern.quote(word.toLowerCase()) + "\\b"; // Use word boundaries to match whole words only
            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(lowercaseInput).find()) {
                return false;
            }
        }
        return true;
    }
}