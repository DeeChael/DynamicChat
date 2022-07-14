package net.deechael.dynamichat.extension.report;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    public static void checkConfig() {
        File configFile = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
        setDefault(configuration, "mention-online-admin", true);
        setDefault(configuration, "mention-online-reporter", true);
        setDefault(configuration, "report-delay", 5L);
        try {
            configuration.save(configFile);
        } catch (IOException ignored) {
        }
    }

    public static boolean mentionAdmin() {
        File configFile = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "config.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
        return configuration.getBoolean("mention-online-admin");
    }

    public static boolean mentionReporter() {
        File configFile = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "config.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
        return configuration.getBoolean("mention-online-reporter");
    }

    public static long delay() {
        File configFile = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "config.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
        return configuration.getLong("report-delay");
    }

    private static void setDefault(FileConfiguration configuration, String key, Object value) {
        if (!configuration.contains(key))
            configuration.set(key, value);
    }

}
