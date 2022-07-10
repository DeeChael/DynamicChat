package net.deechael.dynamichat.util;

import net.deechael.dynamichat.DyChatPlugin;
import net.deechael.dynamichat.feature.Filter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.color.ICC_ColorSpace;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ConfigUtils {

    private static FileConfiguration configuration;
    private static FileConfiguration languageConfiguration;

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
                configuration.set("multi-language.follow-client", true);
                configuration.set("multi-language.default", "en_US");
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
        File languageFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "languages"), configuration.getString("multi-language.default", "en_US") + ".yml");
        if (!languageFile.exists()) {
            try {
                languageFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        languageConfiguration = YamlConfiguration.loadConfiguration(languageFile);
        setDefault(languageConfiguration, "command.gotohelp", "&c(!) Please type \"/dynamic-chat help\" to get help!");
        setDefault(languageConfiguration, "command.main-help", Arrays.asList("&6&l==============================", "&e/dynamic-chat help - get help", "&e/dynamic-chat reload - reload configuration", "&6&l=============================="));
        setDefault(languageConfiguration, "command.main-reload-success", "&a(!) Reloaded configuration successfully");
        setDefault(languageConfiguration, "message.filter-cancel", "&a(!) There are some illegal words");
        setDefault(languageConfiguration, "message.filter-cancel-edit-button", "&e&l[Click to edit]");
        setDefault(languageConfiguration, "message.filter-cancel-edit-button-hover", "&b&lClick here to edit!");
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

    public static boolean filterEnable() {
        check();
        return configuration.getBoolean("filter.enable");
    }

    public static Filter.Mode filterMode() {
        check();
        return Filter.Mode.valueOf(configuration.getString("filter.mode", "replace").toUpperCase());
    }


    public static List<Filter.Checker> filters() {
        check();
        List<Filter.Checker> filters = new ArrayList<>();
        if (configuration.contains("filter.keywords")) {
            for (String keyword : configuration.getStringList("filter.keywords")) {
                if (keyword.startsWith("[py]")) {
                    filters.add(new Filter.Checker(Filter.CheckMode.PINYIN, keyword.substring(4)));
                } else if (keyword.startsWith("[chs]")) {
                    filters.add(new Filter.Checker(Filter.CheckMode.CHINESE, keyword.substring(5)));
                } else if (keyword.startsWith("[pyh]")) {
                    filters.add(new Filter.Checker(Filter.CheckMode.PINYIN_STARTSWITH, keyword.substring(5)));
                } else if (keyword.startsWith("[en]")) {
                    filters.add(new Filter.Checker(Filter.CheckMode.ENGLISH, keyword.substring(4)));
                } else {
                    filters.add(new Filter.Checker(Filter.CheckMode.ENGLISH, keyword));
                }
            }
        }
        return filters;
    }

    public static String getChatFormat() {
        check();
        return configuration.getString("message-format.chat");
    }

    public static String lang(String key) {
        if (!languageConfiguration.contains(key))
            return "Unknown message: " + key;
        if (languageConfiguration.isList(key)) {
            List<String> stringList = languageConfiguration.getStringList(key);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stringList.size(); i++) {
                if (i != stringList.size() - 1) {
                    builder.append(stringList.get(i));
                    builder.append("\n");
                }
            }
            return ColorUtils.processGradientColor(ColorUtils.processChatColor(builder.toString()));
        }
        return ColorUtils.processGradientColor(ColorUtils.processChatColor(languageConfiguration.getString(key, "Unknown message: " + key)));
    }

    private static void setDefault(FileConfiguration configuration, String key, Object value) {
        if (!configuration.contains(key))
            configuration.set(key, value);
    }

}
