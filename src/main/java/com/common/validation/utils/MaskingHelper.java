package com.common.validation.utils;

import java.util.List;
import java.util.regex.Pattern;

public class MaskingHelper {
    //MASK Profane words
    public static String maskProfanity(String input, String pattern) {
        input = input.replaceAll("\\b" + pattern + "\\b", "******");
        return input;
    }

    public static String maskAllProfanity(String input, List<String> wordList) {
        for (String word : wordList) {
            String regex = "\\b" + Pattern.quote(word) + "\\b";
            input = maskProfanity(input, word);
        }
        return input;
    }

    //MASK All
    public static String maskAll(String input) {
        return "***********";
    }
}
