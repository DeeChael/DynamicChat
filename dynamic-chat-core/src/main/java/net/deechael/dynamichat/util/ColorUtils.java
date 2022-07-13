package net.deechael.dynamichat.util;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ColorUtils {

    private static final java.util.List<Character> ALL_CODES = new ArrayList<>();
    private static final List<Character> HEX_CODES = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'A', 'B', 'C', 'D', 'E', 'F');

    static {
        for (char c : ChatColor.ALL_CODES.toCharArray()) {
            ALL_CODES.add(c);
        }
    }

    public static ChatColor iHateSpigotBungeecordAndMD5(String code) {
        System.out.println(code);
        switch (code) {
            case "0":
                return ChatColor.BLACK;
            case "1":
                return ChatColor.DARK_BLUE;
            case "2":
                return ChatColor.DARK_GREEN;
            case "3":
                return ChatColor.DARK_AQUA;
            case "4":
                return ChatColor.DARK_RED;
            case "5":
                return ChatColor.DARK_PURPLE;
            case "6":
                return ChatColor.GOLD;
            case "7":
                return ChatColor.GRAY;
            case "8":
                return ChatColor.DARK_GRAY;
            case "9":
                return ChatColor.BLUE;
            case "a":
                return ChatColor.GREEN;
            case "b":
                return ChatColor.AQUA;
            case "c":
                return ChatColor.RED;
            case "d":
                return ChatColor.LIGHT_PURPLE;
            case "e":
                return ChatColor.YELLOW;
            case "f":
                return ChatColor.WHITE;
            case "k":
                return ChatColor.MAGIC;
            case "l":
                return ChatColor.BOLD;
            case "m":
                return ChatColor.STRIKETHROUGH;
            case "n":
                return ChatColor.UNDERLINE;
            case "o":
                return ChatColor.ITALIC;
            case "r":
            default:
                return ChatColor.RESET;
        }
    }

    public static String processChatColor(String message) {
        for (char c : ChatColor.ALL_CODES.toCharArray()) {
            message = message.replace("&" + c, "§" + c);
        }
        if (Ref.getVersion() >= 13) {
            message = message.replace("&g", "§x§d§d§d§6§0§5");
        }
        return message;
    }

    public static String processGradientColor(String message) {
        if (Ref.getVersion() < 13) return message;
        if (!message.contains("&{[")) return message;
        String[] subbed = message.split("&\\{\\[");
        for (String s : subbed) {
            String got = s;
            if (got.isEmpty()) continue;
            if (!got.contains("]}")) continue;
            String[] got_split_block_big = got.split("]}");
            if (got_split_block_big.length > 1) {
                got = got.substring(0, got.length() - (got_split_block_big[got_split_block_big.length - 1].length() + 2));
            } else {
                got = got.substring(0, got.length() - 2);
            }
            int start_length = 1;
            int end_length = 1;
            Color from;
            Color to;
            if (got.split("]")[0].length() == 1) {
                start_length += 1;
                char c = got.split("]")[0].toCharArray()[0];
                if (c == 'g') {
                    from = ChatColor.of("#ddd605").getColor();
                } else {
                    if (!checkSingle(c)) continue;
                    from = ChatColor.getByChar(c).getColor();
                }
            } else if (got.split("]")[0].length() == 6) {
                start_length += 6;
                String hex = got.split("]")[0];
                if (!checkHex(hex)) continue;
                from = ChatColor.of("#" + hex).getColor();
            } else if (got.split("]")[0].length() == 7) {
                start_length += 7;
                String hex = got.split("]")[0];
                if (!checkHex(hex)) continue;
                from = ChatColor.of(hex).getColor();
            } else continue;
            String[] split_end = got.split("\\[");
            String end_text_split = split_end[split_end.length - 1];
            if (end_text_split.length() == 1) {
                end_length += 1;
                char c = end_text_split.toCharArray()[0];
                if (c == 'g') {
                    to = ChatColor.of("#ddd605").getColor();
                } else {
                    if (!checkSingle(c)) continue;
                    to = ChatColor.getByChar(c).getColor();
                }
            } else if (end_text_split.length() == 6) {
                end_length += 6;
                if (!checkHex(end_text_split)) continue;
                to = ChatColor.of("#" + end_text_split).getColor();
            } else if (end_text_split.length() == 7) {
                end_length += 7;
                if (!checkHex(end_text_split)) continue;
                to = ChatColor.of(end_text_split).getColor();
            } else continue;
            if (!(from != null && to != null)) continue;
            String content = got.substring(start_length);
            content = content.substring(0, content.length() - end_length);
            String textToBeReplaced = "&{[" + got.split("]")[0] + "]" + content + "[" + end_text_split + "]}";
            message = message.replace(textToBeReplaced, new GradientColor(from, to).applyColor(content) + "§r");
        }
        return message;
    }

    private static boolean checkSingle(char c) {
        return ALL_CODES.contains(c);
    }

    private static boolean checkHex(String hex) {
        if (hex.length() <= 5 || hex.length() >= 8) return false;
        if (hex.length() == 7 && !hex.startsWith("#")) return false;
        if (hex.length() == 7) hex = hex.substring(1);
        for (char c : hex.toCharArray()) {
            if (!HEX_CODES.contains(c)) return false;
        }
        return true;
    }

}
