package com.common.validation.utils;

public class MaskingHelper {
    //MASK Profane words
    public static String maskProfanity(String input, String pattern) {
        input = input.replaceAll(pattern, "******");
        return input;
    }

    //MASK All
    public static String maskAll(String input) {
        return "***********";
    }
}
