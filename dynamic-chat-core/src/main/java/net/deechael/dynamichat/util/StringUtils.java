package net.deechael.dynamichat.util;

public final class StringUtils {

    private StringUtils() {
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
        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < (times - 1); i++) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

}
