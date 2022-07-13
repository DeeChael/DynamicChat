package net.deechael.dynamichat.util;

import net.deechael.dynamichat.DyChatPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public final class PlayerUtils {

    private PlayerUtils() {
    }

    public static void reload() {
    }

    public static ChatColor color(Player player) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            ChatColor chatColor = new ChatColor();
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            String colorString = configuration.getString("chat-color", "f");
            if (!colorString.contains(",")) {
                Color color = singleColor(colorString);
                if (color != null) {
                    chatColor.addColor(color);
                }
            } else {
                String[] colors = colorString.split(",");
                for (String color : colors) {
                    Color c = singleColor(color);
                    if (c != null) {
                        chatColor.addColor(c);
                    }
                }
            }
            if (chatColor.getColors() > 0) {
                return chatColor;
            }
        }
        return null;
    }

    public static void setColor(Player player, List<String> colors) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
        StringBuilder colorString = new StringBuilder();
        for (int i = 0; i < colors.size(); i++) {
            if (i < colors.size() - 1) {
                colorString.append(",");
            }
        }
        configuration.set("chat-color", colorString.toString());
        try {
            configuration.save(playerFile);
        } catch (IOException ignored) {
        }
    }

    public static void reset(Player player) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            configuration.set("chat-color", null);
            try {
                configuration.save(playerFile);
            } catch (IOException ignored) {
            }
        }
    }

    private static Color singleColor(String colorString) {
        if (colorString.length() == 1) {
            return org.bukkit.ChatColor.getByChar(colorString).asBungee().getColor();
        } else if (colorString.length() == 6) {
            return net.md_5.bungee.api.ChatColor.of("#" + colorString).getColor();
        } else if (colorString.length() == 7) {
            return net.md_5.bungee.api.ChatColor.of(colorString).getColor();
        }
        return null;
    }

}
