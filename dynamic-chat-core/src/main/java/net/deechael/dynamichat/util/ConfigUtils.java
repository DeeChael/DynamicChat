package net.deechael.dynamichat.util;

import net.deechael.dynamichat.DyChatPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public final class ConfigUtils {

    private static FileConfiguration configuration;

    public static void load() {
        File configFile = new File(DyChatPlugin.getInstance().getDataFolder(), "config.yml");
        File parent = configFile.getParentFile();
        if (parent != null) {
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                configuration = YamlConfiguration.loadConfiguration(configFile);
                configuration.set("mention-player", true);
                configuration.set("chat-color.enable", false);
                configuration.set("chat-color.gradient", false);
                configuration.set("chat-color.changeable", false);
                configuration.set("message-format.chat", "%player_displayname%: %message%");
                configuration.set("message-format.channel-message", "%dynamichat_channel_displayname%: %message%");
                configuration.set("message-format.say-command", "[%sender%] %message%");
                configuration.set("message-format.whisper.send", "You -> %receiver%: %message%");
                configuration.set("message-format.whisper.receive", "%sender% -> you: %message%");
                configuration.set("replace.enable", false);
                configuration.set("filter.enable", false);
                configuration.set("filter.mode", "replace");
            } catch (IOException ignored) {
            }
        }
    }

    public static void save() {
        try {
            configuration.save(new File(DyChatPlugin.getInstance().getDataFolder(), "config.yml"));
        } catch (IOException ignored) {
        }
    }

    private static void check() {
        if (configuration == null) {
            throw new RuntimeException("Configuration hasn't been loaded!");
        }
    }

    public static boolean chatColorEnable() {
        check();
        return configuration.getBoolean("chat-color.enable");
    }

    public static boolean chatColorGradient() {
        check();
        return configuration.getBoolean("chat-color.gradient");
    }

    public static boolean chatColorChangeable() {
        check();
        return configuration.getBoolean("chat-color.changeable");
    }

}
