package net.deechael.dynamichat.util;

import net.deechael.useless.objs.DuObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private StringUtils() {
    }

    public static String random64() {
        StringBuilder builder = new StringBuilder();
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".toCharArray();
        Random random = new Random();
        for (int i = 0; i < 64; i++) {
            builder.append(chars[random.nextInt(chars.length)]);
        }
        return builder.toString();
    }

    public static String join(String spliter, List<String> strings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            builder.append(strings.get(i));
            if (i < strings.size() - 1) {
                builder.append(spliter);
            }
        }
        return builder.toString();
    }

    public static String join(String spliter, String[] strings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            builder.append(strings[i]);
            if (i < strings.length - 1) {
                builder.append(spliter);
            }
        }
        return builder.toString();
    }

    public static String safePattern(String string) {
        String[] keywords = new String[]{"*", ".", "?", "+", "$", "^", "[", "]", "(", ")", "{", "}", "|", "\"", "/"};
        for (String keyword : keywords) {
            string = string.replace(keyword, "\\" + keyword);
        }
        return string;
    }

    public static String unsafePattern(String string) {
        String[] keywords = new String[]{"*", ".", "?", "+", "$", "^", "[", "]", "(", ")", "{", "}", "|", "\"", "/"};
        for (String keyword : keywords) {
            string = string.replace("\\" + keyword, keyword);
        }
        return string;
    }

    public static boolean containsIgnoreCase(String owner, String child) {
        return owner.toLowerCase().contains(child.toLowerCase());
    }

    public static String multiply(String string, int times) {
        if (times == 0) return "";
        if (times < 0) return multiply0(new StringBuilder(string).reverse().toString(), -times);
        return multiply0(string, times);
    }

    private static String multiply0(String string, int times) {
        return string + string.repeat(Math.max(0, (times - 1)));
    }

    public static DuObj<String[], String[]> split(String string, String regex) {
        return split(string, Pattern.compile(regex));
    }

    public static DuObj<String[], String[]> split(String string, Pattern pattern) {
        if (!Pattern.matches(".*" + pattern.pattern() + ".*", string))
            return new DuObj<>(new String[]{string}, new String[0]);
        Matcher matcher = pattern.matcher(string);
        List<String> spliters = new ArrayList<>();
        while (matcher.find())
            spliters.add(matcher.group());
        return new DuObj<>(pattern.split(string), spliters.toArray(new String[0]));
    }

    public static boolean startsWith(String string, Pattern pattern) {
        return Pattern.matches(pattern.pattern() + ".*", string);
    }

}
