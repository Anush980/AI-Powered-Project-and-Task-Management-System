package com.anush.aiproject.shared.util;

public final class StringUtils {

private StringUtils() {
    // private constructor to prevent instantiation
}
public static String trim(String str) {
    return str == null ? null : str.trim();
}

}
