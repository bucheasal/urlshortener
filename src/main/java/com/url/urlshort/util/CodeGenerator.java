package com.url.urlshort.util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

public class CodeGenerator {
    private static final String alphabet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]{3,30}$");
    private static final Random random = new SecureRandom();

    public static String randomCode(int length) {
        if (length < 3 || length > 64) {
            throw new IllegalArgumentException("길이는 3~64까지만");
        }
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    public static boolean validAlias(String alias) {
        return alias != null && pattern.matcher(alias.trim()).matches();
    }

    //스킴 없을때 http://
    public static String normalizeUrl(String raw) {
        String url = raw == null ? "" : raw.trim();
        if (!url.matches("(?i)^[a-z][a-z0-9+.-]*://.*$")) {
            url = "http://" + url;
        }
        return url;
    }
}
