package net.deechael.dynamichat.util;

import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChatColor {

    private final List<Color> colors = new ArrayList<>();

    public ChatColor() {
    }

    public void addColor(Color color) {
        this.colors.add(color);
    }

    public String apply(String message) {
        if (colors.size() == 1) {
            return net.md_5.bungee.api.ChatColor.of(colors.get(0)).toString() + message + "§r";
        } else if (colors.size() >= 2) {
            return new GradientColor(colors.get(0), colors.get(1)).applyColor(message) + "§r";
        }
        return message;
    }

    public int getColors() {
        return colors.size();
    }

    public List<Color> colors() {
        return new ArrayList<>(this.colors);
    }

    public String applyWithMention(String message, Player player) {
        if (colors.size() == 1) {
            String[] split = message.split("@" + player.getName() + " ");
            String color = net.md_5.bungee.api.ChatColor.of(colors.get(0)).toString();
            List<String> strings = new ArrayList<>();
            for (String str : split) {
                strings.add(color + str + "§r");
            }
            String msg = StringUtils.join("@" + player.getName() + " ", strings);
            if (msg.endsWith("@" + player.getName() + "§r")) {
                msg = msg.substring(0, msg.length() - 3 - player.getName().length()) + "§r@" + player.getName();
            }
            return msg;
        } else if (colors.size() >= 2) {
            String[] split = message.split("@" + player.getName() + " ");
            int length = 0;
            for (String str : split) {
                length += str.length();
            }
            Color[] colors = GradientColor.gradient(this.colors.get(0), this.colors.get(1), length);
            int j = 0;
            for (int i = 0; i < split.length; i++) {
                char[] chars = split[i].toCharArray();
                StringBuilder builder = new StringBuilder();
                for (char c : chars) {
                    builder.append(net.md_5.bungee.api.ChatColor.of(colors[j]).toString()).append(c).append("§r");
                    j += 1;
                }
                split[i] = builder.toString();
            }
            String msg = StringUtils.join("@" + player.getName() + " ", split);
            if (msg.endsWith("@" + player.getName() + "§r")) {
                msg = msg.substring(0, msg.length() - 3 - player.getName().length()) + "§r@" + player.getName();
            }
            return msg;
        }
        return message;
    }

    public String applyWithMentionAll(String message) {
        if (colors.size() == 1) {
            String[] split = message.split("@Everyone ");
            String color = net.md_5.bungee.api.ChatColor.of(colors.get(0)).toString();
            List<String> strings = new ArrayList<>();
            for (String str : split) {
                strings.add(color + str + "§r");
            }
            String msg = StringUtils.join("@Everyone ", strings);
            if (msg.endsWith("@Everyone§r")) {
                msg = msg.substring(0, msg.length() - 11) + "§r@Everyone";
            }
            return msg;
        } else if (colors.size() >= 2) {
            String[] split = message.split("@Everyone ");
            int length = 0;
            for (String str : split) {
                length += str.length();
            }
            Color[] colors = GradientColor.gradient(this.colors.get(0), this.colors.get(1), length);
            int j = 0;
            for (int i = 0; i < split.length; i++) {
                char[] chars = split[i].toCharArray();
                StringBuilder builder = new StringBuilder();
                for (char c : chars) {
                    builder.append(net.md_5.bungee.api.ChatColor.of(colors[j]).toString()).append(c).append("§r");
                    j += 1;
                }
                split[i] = builder.toString();
            }
            String msg = StringUtils.join("@Everyone ", split);
            if (msg.endsWith("@Everyone§r")) {
                msg = msg.substring(0, msg.length() - 11) + "§r@Everyone";
            }
            return msg;
        }
        return message;
    }

    public String applyWithMentionPlayerAndAll(String message, Player player) {
        if (colors.size() == 1) {
            /*String[] split = message.split("@" + player.getName());
            String color = net.md_5.bungee.api.ChatColor.of(colors.get(0)).toString();
            List<String> strings = new ArrayList<>();
            for (String str : split) {
                strings.add(color + str + "§r");
            }
            String msg = StringUtils.join("@" + player.getName() + " ", strings);
            if (msg.endsWith("@" + player.getName() + "§r")) {
                msg = msg.substring(0, msg.length() - 3 - player.getName().length()) + "§r@" + player.getName();
            }
            */
            String color = net.md_5.bungee.api.ChatColor.of(colors.get(0)).toString();
            String msg = color + message.replace("@Everyone ", "§r@Everyone " + color).replace("@" + player.getName() + " ", "§r@" + player.getName() + " " + color);
            if (msg.endsWith("@" + player.getName() + "§r")) {
                msg = msg.substring(0, msg.length() - 3 - player.getName().length()) + "§r@" + player.getName();
            } else if (msg.endsWith("@Everyone§r")) {
                msg = msg.substring(0, msg.length() - 11) + "§r@Everyone";
            }
            return msg;
        } else if (colors.size() >= 2) {
            String[] split = message.split("@" + player.getName() + " ");
            String[][] splits = new String[split.length][0];
            int length = 0;
            for (int i = 0; i < split.length; i++) {
                String str = split[i];
                String[] split2 = str.split("@Everyone ");
                splits[i] = split2;
                for (String str2 : split2) {
                    length += str2.length();
                }
            }
            Color[] colors = GradientColor.gradient(this.colors.get(0), this.colors.get(1), length);
            int j = 0;
            StringBuilder holyBuilder = new StringBuilder();
            for (int i = 0; i < splits.length; i++) {
                String[] spilit2 = splits[i];
                StringBuilder builder = new StringBuilder();
                for (int o = 0; o < spilit2.length; o++) {
                    StringBuilder charBuilder = new StringBuilder();
                    char[] chars = spilit2[o].toCharArray();
                    for (char c : chars) {
                        charBuilder.append(net.md_5.bungee.api.ChatColor.of(colors[j]).toString()).append(c).append("§r");
                        j += 1;
                    }
                    builder.append(charBuilder);
                    if (o < spilit2.length - 1) {
                        builder.append("@Everyone ");
                    }
                }

                holyBuilder.append(builder);
                if (i < splits.length - 1) {
                    holyBuilder.append("@").append(player.getName()).append(" ");
                }
            }
            String msg = holyBuilder.toString();
            if (msg.endsWith("@" + player.getName() + "§r")) {
                msg = msg.substring(0, msg.length() - 3 - player.getName().length()) + "§r@" + player.getName();
            } else if (msg.endsWith("@Everyone§r")) {
                msg = msg.substring(0, msg.length() - 11) + "§r@Everyone";
            }
            return msg;
        }
        return message;
    }

}
