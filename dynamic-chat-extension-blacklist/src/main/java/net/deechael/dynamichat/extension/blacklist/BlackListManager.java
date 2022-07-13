package net.deechael.dynamichat.extension.blacklist;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BlackListManager {

    public static void add(Player player, String blacker) {
        File playerFile = new File(new File(DyChatBlackListExtensionPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
        List<String> black = new ArrayList<>();
        black.add(blacker);
        if (configuration.contains("black") && configuration.isList("black")) {
            black.addAll(configuration.getStringList("black"));
        }
        configuration.set("black", black);
        try {
            configuration.save(playerFile);
        } catch (IOException ignored) {
        }
    }

    public static void remove(Player player, String blacker) {
        File playerFile = new File(new File(DyChatBlackListExtensionPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
        List<String> black = new ArrayList<>();
        if (configuration.contains("black") && configuration.isList("black")) {
            black.addAll(configuration.getStringList("black"));
        }
        black.remove(blacker);
        configuration.set("black", black);
        try {
            configuration.save(playerFile);
        } catch (IOException ignored) {
        }
    }

    public static List<String> getBlacked(Player player) {
        File playerFile = new File(new File(DyChatBlackListExtensionPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("black") && configuration.isList("black")) {
                return new ArrayList<>(configuration.getStringList("black"));
            }
        }
        return new ArrayList<>();
    }

    public static List<String> getBlacked(UUID player) {
        File playerFile = new File(new File(DyChatBlackListExtensionPlugin.getInstance().getDataFolder(), "players"), player + ".yml");
        if (playerFile.exists()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("black") && configuration.isList("black")) {
                return new ArrayList<>(configuration.getStringList("black"));
            }
        }
        return new ArrayList<>();
    }

}
