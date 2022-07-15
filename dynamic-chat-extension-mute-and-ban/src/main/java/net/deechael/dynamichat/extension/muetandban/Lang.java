package net.deechael.dynamichat.extension.muetandban;

import net.deechael.dynamichat.api.ChatManager;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class Lang {

    public static void checkLanguage() {
        File langFile = new File(new File(DyChatMNBExtensionPlugin.getInstance().getDataFolder(), "languages"), "en_us.yml");
        if (!langFile.exists()) {
            File parent = langFile.getParentFile();
            if (parent != null) {
                if (!parent.exists()) {
                    parent.mkdirs();
                }
            }
            try {
                langFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(langFile);
        //setDefault(configuration, "command.mustbeplayer", "&c&l(!) &r&cYou must be a player!");
        //setDefault(configuration, "command.nopermission", "&c&l(!) &r&cYou don't have the permission!");
        //setDefault(configuration, "command.mute.gotohelp", "&c&l(!) &r&cType \"/mute help\" to get help");
        //setDefault(configuration, "command.mute.help", List.of("&6&l==============================",
        //        "&e/report help - get help",
        //        "&e/report report <player> - report a player",
        //        "&e/report reports - open report management gui",
        //        "&6&l=============================="));
        setDefault(configuration, "button.mute.display", "&6&l[&r&6Mute&r&6&l]");
        setDefault(configuration, "button.mute.hover", "&b&lMute this player");
        setDefault(configuration, "button.ban.display", "&4&l[&r&4Ban&r&4&l]");
        setDefault(configuration, "button.ban.hover", "&b&lBan this player");
        try {
            configuration.save(langFile);
        } catch (IOException ignored) {
        }
    }

    public static void sendConsole(CommandSender sender, String key, Object... params) {
        String message = lang(key);
        for (int i = 0; i < params.length; i++) {
            message = message.replace("{" + i + "}", params[i].toString());
        }
        sender.sendMessage(message);
    }

    public static void send(Player player, String key, Object... params) {
        player.sendMessage(lang(player, key, params));
    }

    public static String lang(Player player, String key, Object... params) {
        if (ChatManager.getManager().languageFollowClient()) {
            String message = lang(player.getLocale(), key);
            for (int i = 0; i < params.length; i++) {
                message = message.replace("{" + i + "}", params[i].toString());
            }
            return message;
        }
        String message = lang(key);
        for (int i = 0; i < params.length; i++) {
            message = message.replace("{" + i + "}", params[i].toString());
        }
        return message;
    }

    public static String lang(String key) {
        return lang(ChatManager.getManager().getDefaultLanguage(), key);
    }

    private static String lang(String name, String key) {
        File langFile = new File(new File(DyChatMNBExtensionPlugin.getInstance().getDataFolder(), "languages"), name + ".yml");
        if (!langFile.exists()) {
            if (Objects.equals(name, "en_us")) {
                return "Unknown message: " + key;
            }
            return lang("en_us", key);
        }
        FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
        if (!lang.contains(key)) {
            if (Objects.equals(name, "en_us")) {
                return "Unknown message: " + key;
            }
            return lang("en_us", key);
        }
        return ColorUtils.processGradientColor(ColorUtils.processChatColor(lang(lang, key)));
    }

    private static String lang(Configuration languageConfiguration, String key) {
        if (!languageConfiguration.contains(key))
            return "Unknown message: " + key;
        if (languageConfiguration.isList(key)) {
            List<String> stringList = languageConfiguration.getStringList(key);
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < stringList.size(); i++) {
                builder.append(stringList.get(i));
                if (i != stringList.size() - 1) {
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
