package com.common.commonUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class TokenHelper {
    public static String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
    public static String removeBearer(String token) {
        if (token.contains("Bearer")) {
            return token.substring(7);
        } else
            return token;
    }
}
