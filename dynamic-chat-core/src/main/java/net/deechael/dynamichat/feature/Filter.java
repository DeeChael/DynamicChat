package net.deechael.dynamichat.feature;

import net.deechael.chschar.ChineseCharacterLibrary;
import net.deechael.chschar.Pronunciation;
import net.deechael.dynamichat.util.ConfigUtils;
import net.deechael.dynamichat.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Filter {

    public static String set(String message) {
        for (Checker checker : ConfigUtils.filters()) {
            message = checker.set(message);
        }
        return message;
    }

    public static boolean suit(String message) {
        for (Checker checker : ConfigUtils.filters()) {
            if (checker.suit(message)) {
                return true;
            }
        }
        return false;
    }

    public enum Mode {
        REPLACE, CANCEL
    }

    public enum CheckMode {

        PINYIN, ENGLISH, CHINESE, PINYIN_STARTSWITH

    }

    public static class Checker {

        private final CheckMode mode;
        private final String keyword;

        public Checker(CheckMode mode, String keyword) {
            this.mode = mode;
            this.keyword = keyword;
        }

        public String set(String message) {
            if (mode.equals(CheckMode.PINYIN)) {
                List<String> divided = new ArrayList<>();
                if (keyword.contains(","))
                    divided.addAll(Arrays.asList(keyword.split(",")));
                else
                    divided.add(keyword);
                if (message.length() >= divided.size()) {
                    for (int i = 0; i < message.length(); i++) {
                        char[] checking = message.substring(i, i + divided.size()).toCharArray();
                        boolean shouldContinue = false;
                        for (char c : checking) {
                            if (!ChineseCharacterLibrary.has(c)) {
                                shouldContinue = true;
                                break;
                            }
                        }
                        if (shouldContinue) {
                            if (i + divided.size() >= message.length()) {
                                break;
                            }
                            continue;
                        }
                        for (int j = 0; j < checking.length; j++) {
                            boolean has = false;
                            for (Pronunciation pronunciation : ChineseCharacterLibrary.get(checking[j]).getPronunciations()) {
                                if (pronunciation.getPhoneticizationStringWithV().equalsIgnoreCase(divided.get(j))
                                        || pronunciation.getPhoneticizationString().equalsIgnoreCase(divided.get(j))) {
                                    has = true;
                                    break;
                                }
                            }
                            if (!has) {
                                shouldContinue = true;
                                break;
                            }
                        }
                        if (shouldContinue) {
                            if (i + divided.size() >= message.length()) {
                                break;
                            }
                            continue;
                        }
                        if (i + divided.size() >= message.length()) {
                            message = message.substring(0, i) + StringUtils.multiply("*", divided.size());
                            break;
                        }
                        message = message.substring(0, i) + StringUtils.multiply("*", divided.size()) + message.substring(i + divided.size());
                    }
                }
            } else if (mode.equals(CheckMode.PINYIN_STARTSWITH)) {
                List<String> divided = new ArrayList<>();
                if (keyword.contains(","))
                    divided.addAll(Arrays.asList(keyword.split(",")));
                else
                    divided.add(keyword);
                if (message.length() >= divided.size()) {
                    for (int i = 0; i < message.length(); i++) {
                        char[] checking = message.substring(i, i + divided.size()).toCharArray();
                        boolean shouldContinue = false;
                        for (char c : checking) {
                            if (!ChineseCharacterLibrary.has(c)) {
                                shouldContinue = true;
                                break;
                            }
                        }
                        if (shouldContinue) {
                            if (i + divided.size() >= message.length()) {
                                break;
                            }
                            continue;
                        }
                        for (int j = 0; j < checking.length; j++) {
                            boolean has = false;
                            for (Pronunciation pronunciation : ChineseCharacterLibrary.get(checking[j]).getPronunciations()) {
                                if (pronunciation.getPhoneticizationStringWithV().startsWith(divided.get(j))
                                        || pronunciation.getPhoneticizationString().startsWith(divided.get(j))) {
                                    has = true;
                                    break;
                                }
                            }
                            if (!has) {
                                shouldContinue = true;
                                break;
                            }
                        }
                        if (shouldContinue) {
                            if (i + divided.size() >= message.length()) {
                                break;
                            }
                            continue;
                        }
                        if (i + divided.size() >= message.length()) {
                            message = message.substring(0, i) + StringUtils.multiply("*", divided.size());
                            break;
                        }
                        message = message.substring(0, i) + StringUtils.multiply("*", divided.size()) + message.substring(i + divided.size());
                    }
                }
            } else if (mode.equals(CheckMode.CHINESE)) {
                message = message.replace(keyword, StringUtils.multiply("*", keyword.length()));
            } else {
                if (StringUtils.containsIgnoreCase(message, keyword)) {
                    String safedKeyword = StringUtils.safePattern(keyword);
                    Pattern two_spaces = Pattern.compile(".* (?i)" + safedKeyword + " .*");
                    Pattern start_spaces = Pattern.compile(".* (?i)" + safedKeyword + ".*");
                    Pattern end_spaces = Pattern.compile(".*(?i)" + safedKeyword + " .*");
                    if (two_spaces.matcher(message).matches()) {
                        message = message.replaceAll(" (?i)" + safedKeyword + " ", " " + StringUtils.multiply("*", keyword.length()) + " ");
                    }
                    if (start_spaces.matcher(message).matches()) {
                        message = message.replaceAll(" (?i)" + safedKeyword, " " + StringUtils.multiply("*", keyword.length()));
                    }
                    if (end_spaces.matcher(message).matches()) {
                        message = message.replaceAll("(?i)" + safedKeyword + " ", StringUtils.multiply("*", keyword.length()) + " ");
                    }
                    if (keyword.equalsIgnoreCase(message)) {
                        message = StringUtils.multiply("*", message.length());
                    }
                }
            }
            return message;
        }

        public boolean suit(String message) {
            if (mode == CheckMode.PINYIN) {
                List<String> divided = new ArrayList<>();
                if (keyword.contains(","))
                    divided.addAll(Arrays.asList(keyword.split(",")));
                else
                    divided.add(keyword);
                if (message.length() >= divided.size()) {
                    for (int i = 0; i < message.length(); i++) {
                        char[] checking = message.substring(i, i + divided.size()).toCharArray();
                        boolean shouldContinue = false;
                        for (char c : checking) {
                            if (!ChineseCharacterLibrary.has(c)) {
                                shouldContinue = true;
                                break;
                            }
                        }
                        if (shouldContinue) {
                            if (i + divided.size() >= message.length()) {
                                break;
                            }
                            continue;
                        }
                        for (int j = 0; j < checking.length; j++) {
                            boolean has = false;
                            for (Pronunciation pronunciation : ChineseCharacterLibrary.get(checking[j]).getPronunciations()) {
                                if (pronunciation.getPhoneticizationStringWithV().equalsIgnoreCase(divided.get(j))
                                        || pronunciation.getPhoneticizationString().equalsIgnoreCase(divided.get(j))) {
                                    has = true;
                                    break;
                                }
                            }
                            if (!has) {
                                shouldContinue = true;
                                break;
                            }
                        }
                        if (shouldContinue) {
                            if (i + divided.size() >= message.length()) {
                                break;
                            }
                            continue;
                        }
                        if (i + divided.size() >= message.length()) {
                            break;
                        }
                        return true;
                    }
                }
            } else if (mode.equals(CheckMode.PINYIN_STARTSWITH)) {
                List<String> divided = new ArrayList<>();
                if (keyword.contains(","))
                    divided.addAll(Arrays.asList(keyword.split(",")));
                else
                    divided.add(keyword);
                if (message.length() >= divided.size()) {
                    for (int i = 0; i < message.length(); i++) {
                        char[] checking = message.substring(i, i + divided.size()).toCharArray();
                        boolean shouldContinue = false;
                        for (char c : checking) {
                            if (!ChineseCharacterLibrary.has(c)) {
                                shouldContinue = true;
                                break;
                            }
                        }
                        if (shouldContinue) {
                            if (i + divided.size() >= message.length()) {
                                break;
                            }
                            continue;
                        }
                        for (int j = 0; j < checking.length; j++) {
                            boolean has = false;
                            for (Pronunciation pronunciation : ChineseCharacterLibrary.get(checking[j]).getPronunciations()) {
                                if (pronunciation.getPhoneticizationStringWithV().startsWith(divided.get(j))
                                        || pronunciation.getPhoneticizationString().startsWith(divided.get(j))) {
                                    has = true;
                                    break;
                                }
                            }
                            if (!has) {
                                shouldContinue = true;
                                break;
                            }
                        }
                        if (shouldContinue) {
                            if (i + divided.size() >= message.length()) {
                                break;
                            }
                            continue;
                        }
                        if (i + divided.size() >= message.length()) {
                            break;
                        }
                        return true;
                    }
                }
            } else if (mode.equals(CheckMode.CHINESE)) {
                return StringUtils.containsIgnoreCase(message, keyword);
            } else {
                String safedKeyword = StringUtils.safePattern(keyword);
                Pattern two_spaces = Pattern.compile(".* (?i)" + safedKeyword + " .*");
                Pattern start_spaces = Pattern.compile(".* (?i)" + safedKeyword + ".*");
                Pattern end_spaces = Pattern.compile(".*(?i)" + safedKeyword + " .*");
                return two_spaces.matcher(message).matches() || start_spaces.matcher(message).matches() || end_spaces.matcher(message).matches() || message.equalsIgnoreCase(keyword);
            }
            return false;
        }

    }

}
