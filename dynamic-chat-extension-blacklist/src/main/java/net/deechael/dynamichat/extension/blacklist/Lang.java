package net.deechael.dynamichat.extension.blacklist;

import net.deechael.dynamichat.api.BukkitChatManager;
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
        File langFile = new File(new File(DyChatBlackListExtensionPlugin.getInstance().getDataFolder(), "languages"), "en_us.yml");
        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(langFile);
        setDefault(configuration, "command.mustbeplayer", "&c&l(!) &r&cYou must be a player");
        setDefault(configuration, "command.blacklist.gotohelp", "&c&l(!) &r&cType \"/blacklist help\" to get help");
        setDefault(configuration, "command.blacklist.help", List.of("&6&l==============================",
                "&e/blacklist help - get help",
                "&e/blacklist add <player> - add a player to your blacklist",
                "&e/blacklist remove <player> - remove a player from your blacklist",
                "&6&l=============================="));
        setDefault(configuration, "command.blacklist.add.success", "&a&l(!) &r&aYou added &b{0} &ato your blacklist successfully!");
        setDefault(configuration, "command.blacklist.add.alreadyin", "&c&l(!) &r&cYou have added &b{0} &ato your blacklist already!");
        setDefault(configuration, "command.blacklist.remove.success", "&a&l(!) &r&aYou removed &b{0} &afrom your blacklist successfully!");
        setDefault(configuration, "command.blacklist.remove.notin", "&c&l(!) &r&cYou didn't add &b{0} &cto your blacklist!");
        setDefault(configuration, "command.blacklist.notself", "&c&l(!) &r&cYou cannot add yourself to your blacklist");
        setDefault(configuration, "command.blacklist.selfnot", "&c&l(!) &r&cYou cannot be in your blacklist");
        setDefault(configuration, "command.blacklist.notint", "&c&l(!) &r&cPage number must be a integer!");
        setDefault(configuration, "button.display", "&b&l[&r&bAdd to Blacklist&r&b&l]");
        setDefault(configuration, "button.hover", "&b&lAdd this player to your blacklist");
        try {
            configuration.save(langFile);
        } catch (IOException ignored) {
        }
    }

    public static void sendConsole(CommandSender sender, String key, String... params) {
        String message = lang(key);
        for (int i = 0; i < params.length; i++) {
            message = message.replace("{" + i + "}", params[i]);
        }
        sender.sendMessage(message);
    }

    public static void send(Player player, String key, String... params) {
        player.sendMessage(lang(player, key, params));
    }

    public static String lang(Player player, String key, String... params) {
        if (BukkitChatManager.getManager().languageFollowClient()) {
            String message = lang(player.getLocale(), key);
            for (int i = 0; i < params.length; i++) {
                message = message.replace("{" + i + "}", params[i]);
            }
            return message;
        }
        String message = lang(key);
        for (int i = 0; i < params.length; i++) {
            message = message.replace("{" + i + "}", params[i]);
        }
        return message;
    }

    public static String lang(String key) {
        return lang(BukkitChatManager.getManager().getDefaultLanguage(), key);
    }

    private static String lang(String name, String key) {
        File langFile = new File(new File(DyChatBlackListExtensionPlugin.getInstance().getDataFolder(), "languages"), name + ".yml");
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
