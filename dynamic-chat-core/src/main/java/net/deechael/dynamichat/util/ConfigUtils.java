package net.deechael.dynamichat.util;

import net.deechael.dynamichat.DyChatPlugin;
import net.deechael.dynamichat.feature.Filter;
import net.deechael.useless.objs.FiObj;
import net.deechael.useless.objs.TriObj;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
            } catch (IOException ignored) {
            }
        }
        configuration = YamlConfiguration.loadConfiguration(configFile);
        setDefault(configuration, "proxy-mode", false);
        setDefault(configuration, "mention-player", false);
        setDefault(configuration, "whisper-sound", false);
        setDefault(configuration, "channel.enable", false);
        setDefault(configuration, "channel.global-name", "Global");
        setDefault(configuration, "fold-message.enable", false);
        setDefault(configuration, "fold-message.format", " - %message%");
        setDefault(configuration, "chat-color.enable", false);
        setDefault(configuration, "chat-color.gradient", false);
        setDefault(configuration, "chat-color.changeable", false);
        setDefault(configuration, "multi-language.follow-client", true);
        setDefault(configuration, "multi-language.default", "en_us");
        setDefault(configuration, "message-format.chat", "%player_displayname%: %message%");
        setDefault(configuration, "message-format.channel-message", "[%dynamichat_currentChannelDisplay%] %message%");
        setDefault(configuration, "message-format.say-command", "[%sender%] %message%");
        setDefault(configuration, "message-format.whisper.send", "You -> %receiver%: %message%");
        setDefault(configuration, "message-format.whisper.receive", "%sender% -> you: %message%");
        setDefault(configuration, "replace.enable", false);
        setDefault(configuration, "filter.enable", false);
        setDefault(configuration, "filter.mode", "replace");
        File languageFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "languages"), configuration.getString("multi-language.default", "en_us") + ".yml");
        File languageParent = languageFile.getParentFile();
        if (languageParent != null) {
            if (!languageParent.exists()) {
                languageParent.mkdirs();
            }
        }
        if (!languageFile.exists()) {
            try {
                languageFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        languageConfiguration = YamlConfiguration.loadConfiguration(languageFile);
        setDefault(languageConfiguration, "command.gotohelp", "&c&l(!) &r&cPlease type \"/dynamic-chat help\" to get help!");
        setDefault(languageConfiguration, "command.extensionrequired", "&c&l(!) &r&cCannot find the required extension!");
        setDefault(languageConfiguration, "command.mustbeplayer", "&c&l(!) &r&cYou must be a player to use this command!");
        setDefault(languageConfiguration, "command.main-help", Arrays.asList("&6&l==============================", "&e/dynamic-chat help - get help", "&e/dynamic-chat reload - reload configuration", "&6&l=============================="));
        setDefault(languageConfiguration, "command.main-reload-success", "&a&l(!) &r&aReloaded configuration successfully!");
        setDefault(languageConfiguration, "command.channel-gotohelp", "&c&l(!) &rPlease type \"/dynamic-chat help\" to get help!");
        setDefault(languageConfiguration, "command.channel-help", Arrays.asList("&6&l==============================", "&e/channel help - get help", "&e/channel switch <channel> - switch channel", "&6&l=============================="));
        setDefault(languageConfiguration, "command.channel-notexists", "&c&l(!) &r&cThe channel not exists!");
        setDefault(languageConfiguration, "command.channel-switch-success", "&a&l(!) &r&aYou switch channel to &b{0}&a!");
        setDefault(languageConfiguration, "command.channel-notavailable", "&c&l(!) &r&cYou have no access to &f{0}&c!");
        setDefault(languageConfiguration, "command.chatcolor-gotohelp", "&c&l(!) &rPlease type \"/chat-color help\" to get help!");
        setDefault(languageConfiguration, "command.chatcolor-help", Arrays.asList("&6&l==============================", "&e/chat-color help - get help", "&e/chat-color set color <color> - set the color", "&e/chat-color set gradient <from> <to> - set the gradient color", "&e/chat-color reset - reset the chat color", "&6&l=============================="));
        setDefault(languageConfiguration, "command.chatcolor-set-success", "&a&l(!) &r&aYou set the chat color successfully!");
        setDefault(languageConfiguration, "command.chatcolor-set-unknowncolor", "&c&l(!) &r&cYou type a wrong color format!");
        setDefault(languageConfiguration, "command.chatcolor-reset-success", "&a&l(!) &r&aYou reset the chat color successfully!");
        setDefault(languageConfiguration, "command.message.cannotlocate", "&c&l(!) &r&cCannot locate the message!");
        setDefault(languageConfiguration, "command.message.unknownbutton", "&c&l(!) &r&cCannot invoke the button!");
        setDefault(languageConfiguration, "message.filter-cancel", "&c&l(!) &r&cThere are some illegal words!");
        setDefault(languageConfiguration, "message.filter-cancel-edit-button", "&e&l[Click to edit]");
        setDefault(languageConfiguration, "message.filter-cancel-edit-button-hover", "&b&lClick here to edit");
        setDefault(languageConfiguration, "message.hover", "&b&lClick here to show options");
        setDefault(languageConfiguration, "message.button.copy.display", "&a&l[&r&aCopy Message&a&l]");
        setDefault(languageConfiguration, "message.button.copy.hover", "&b&lClick here to copy the message");
        //setDefault(languageConfiguration, "message.button.copy.message", "&a&l(!) &r&cYou copied the message successfully!");
        setDefault(languageConfiguration, "gui.main.title", "&c&lDynamicChat Chat Management");
        setDefault(languageConfiguration, "extension.mute-and-ban.default.no-reason", "&fNo reason");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.kick.success", "&a&l(!) &r&aYou kicked &r&b{0}&r&a successfully!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.kick.notself", "&c&l(!) &r&cYou cannot kick yourself!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.ban.success", "&a&l(!) &r&aYou banned &r&b{0}&r&a successfully!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.ban.time-format", "&c&l(!) &r&cWrong time format!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.ban.notself", "&c&l(!) &r&cYou cannot ban yourself!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.ban.failed", "&c&l(!) &r&cPlayer &r&f{0}&r&c has been banned already!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.unban.success", "&a&l(!) &r&aYou unbanned &r&b{0}&r&a successfully!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.unban.failed", "&c&l(!) &r&cPlayer &r&f{0}&r&c hasn't been banned yet!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.ban-ip.success", "&a&l(!) &r&aYou banned &r&b{0}&r&a successfully!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.ban-ip.time-format", "&c&l(!) &r&cWrong time format!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.ban-ip.notself", "&c&l(!) &r&cYou cannot ban your IP!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.ban-ip.failed", "&c&l(!) &r&cIP &r&f{0}&r&c has been banned already!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.unban-ip.success", "&a&l(!) &r&aYou unbanned &r&b{0}&r&a successfully!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.unban-ip.failed", "&c&l(!) &r&cIP &r&f{0}&r&c hasn't been banned yet!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.mute.success", "&a&l(!) &r&aYou muted &r&b{0}&r&a successfully!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.mute.time-format", "&c&l(!) &r&cWrong time format!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.mute.notself", "&c&l(!) &r&cYou cannot mute yourself!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.mute.failed", "&c&l(!) &r&cPlayer &r&f{0}&r&c has been muted already!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.unmute.success", "&a&l(!) &r&aYou unmuted &r&b{0}&r&a successfully!");
        setDefault(languageConfiguration, "extension.mute-and-ban.command.unmute.failed", "&c&l(!) &r&cPlayer &r&f{0}&r&c hasn't been muted yet!");
        setDefault(languageConfiguration, "extension.mute-and-ban.message.defaults.reason.kick", "&c&l(!) &r&cYou are kicked!");
        setDefault(languageConfiguration, "extension.mute-and-ban.message.defaults.reason.ban", "&c&l(!) &r&cYou are banned!");
        setDefault(languageConfiguration, "extension.mute-and-ban.message.defaults.reason.mute", "&c&l(!) &r&cYou are muted!");
        setDefault(languageConfiguration, "extension.mute-and-ban.message.kicked", List.of("&c&l(!) &r&cYou are kicked!", "&r&cReason: &r&f{0}"));
        setDefault(languageConfiguration, "extension.mute-and-ban.message.banned.forever", List.of("&c&l(!) &r&cYou are banned &r&fFOREVER&r&c!", "&cBanned ID: &r&f{0}", "&r&cReason: &r&f{1}"));
        setDefault(languageConfiguration, "extension.mute-and-ban.message.banned.temporary", List.of("&c&l(!) &r&cYou are banned!", "&r&cUnbanned after &r&f{1}&r&c years &r&f{2}&r&c months &r&f{3}&r&c weeks &r&f{4}&r&c days &r&f{5}&r&c hours &r&f{6}&r&c minutes &r&f{7}&r&c seconds!", "&r&cReason: &r&f{8}"));
        setDefault(languageConfiguration, "extension.mute-and-ban.message.banned-ip.forever", List.of("&c&l(!) &r&cYour IP is banned &r&fFOREVER&r&c!", "&r&cReason: &r&f{0}"));
        setDefault(languageConfiguration, "extension.mute-and-ban.message.banned-ip.temporary", List.of("&c&l(!) &r&cYour IP is banned!", "&r&cUnbanned after &r&f{0}&r&c years &r&f{1}&r&c months &r&f{2}&r&c weeks &r&f{3}&r&c days &r&f{4}&r&c hours &r&f{5}&r&c minutes &r&f{6}&r&c seconds!", "&r&cReason: &r&f{7}"));
        setDefault(languageConfiguration, "extension.mute-and-ban.message.muted.onchat.temporary", List.of("&c&l(!) &r&cYour message didn't be sent because you are muted!", "&r&cUnmuted after &r&f{0}&r&c years &r&f{1}&r&c months &r&f{2}&r&c weeks &r&f{3}&r&c days &r&f{4}&r&c hours &r&f{5}&r&c minutes &r&f{6}&r&c seconds!"));
        setDefault(languageConfiguration, "extension.mute-and-ban.message.muted.onchat.forever", "&c&l(!) &r&cYour message didn't be sent because you are muted &r&fFOREVER&r&c!");
        setDefault(languageConfiguration, "extension.mute-and-ban.message.muted.forever", List.of("&c&l(!) &r&cYou are muted &r&fFOREVER&r&c!", "&cMuted ID: &r&f{0}", "&r&cReason: &r&f{1}"));
        setDefault(languageConfiguration, "extension.mute-and-ban.message.muted.temporary", List.of("&c&l(!) &r&cYou are muted!", "&cMuted ID: &r&f{0}", "&r&cUnmuted after &r&f{1}&r&c years &r&f{2}&r&c months &r&f{3}&r&c weeks &r&f{4}&r&c days &r&f{5}&r&c hours &r&f{6}&r&c minutes &r&f{7}&r&c seconds!", "&r&cReason: &r&f{8}"));
        setDefault(languageConfiguration, "extension.mute-and-ban.message.unmuted", "&a&l(!) &r&aYou are unmuted!");
        setDefault(languageConfiguration, "extension.mute-and-ban.button.mute.display", "&6&l[&r&6Mute&r&6&l]");
        setDefault(languageConfiguration, "extension.mute-and-ban.button.mute.hover", "&b&lMute this player");
        setDefault(languageConfiguration, "extension.mute-and-ban.button.kick.display", "&d&l[&r&dKick&r&d&l]");
        setDefault(languageConfiguration, "extension.mute-and-ban.button.kick.hover", "&b&lKick this player");
        setDefault(languageConfiguration, "extension.mute-and-ban.button.ban.display", "&4&l[&r&4Ban&r&4&l]");
        setDefault(languageConfiguration, "extension.mute-and-ban.button.ban.hover", "&b&lBan this player");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.reason.title", "&c&lReason");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.reason.tips", "Reason");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.reason.button", "&a&lAPPLY");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.message.notlower0", "&c&lTime cannot be lower than 0 seconds");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.title", "&c&lTime");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.item.time", "&c&lTime: &r&f{0}&r&c years &r&f{1}&r&c months &r&f{2}&r&c weeks &r&f{3}&r&c days &r&f{4}&r&c hours &r&f{5}&r&c minutes &r&f{6}&r&c seconds");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.item.time-forever", "&c&lTime: &r&fFOREVER");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.plus.second", "&a&l+1 Second");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.plus.minute", "&a&l+1 Minute");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.plus.hour", "&a&l+1 Hour");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.plus.day", "&a&l+1 Day");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.plus.week", "&a&l+1 Week");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.plus.month", "&a&l+1 Month");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.plus.year", "&a&l+1 Year");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.minus.second", "&c&l-1 Second");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.minus.minute", "&c&l-1 Minute");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.minus.hour", "&c&l-1 Hour");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.minus.day", "&c&l-1 Day");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.minus.week", "&c&l-1 Week");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.minus.month", "&c&l-1 Month");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.minus.year", "&c&l-1 Year");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.apply", "&a&lContinue");
        setDefault(languageConfiguration, "extension.mute-and-ban.gui.time.button.reset", "&c&lReset");
        save();
    }

    public static void save() {
        try {
            configuration.save(new File(DyChatPlugin.getInstance().getDataFolder(), "config.yml"));
            languageConfiguration.save(new File(new File(DyChatPlugin.getInstance().getDataFolder(), "languages"), configuration.getString("multi-language.default", "en_us") + ".yml"));
        } catch (IOException ignored) {
        }
    }

    private static void check() {
        if (configuration == null) {
            throw new RuntimeException("Configuration hasn't been loaded!");
        }
    }

    public static boolean isProxyMode() {
        check();
        return configuration.getBoolean("proxy-mode", false);
    }

    public static boolean chatColorEnable() {
        check();
        return configuration.getBoolean("chat-color.enable");
    }

    public static boolean chatColorGradient() {
        check();
        return configuration.getBoolean("chat-color.gradient");
    }

    public static boolean foldMessageEnable() {
        check();
        return configuration.getBoolean("fold-message.enable");
    }

    public static String foldMessageFormat() {
        check();
        return configuration.getString("fold-message.format");
    }


    public static boolean mentionPlayer() {
        check();
        return configuration.getBoolean("mention-player");
    }


    public static boolean whisperSound() {
        check();
        return configuration.getBoolean("whisper-sound");
    }

    public static boolean languageFollowClient() {
        check();
        return configuration.getBoolean("multi-language.follow-client");
    }

    public static String globalChannelDisplayName() {
        check();
        return configuration.getString("channel.global-name");
    }

    public static boolean chatColorChangeable() {
        check();
        return configuration.getBoolean("chat-color.changeable");
    }

    public static boolean filterEnable() {
        check();
        return configuration.getBoolean("filter.enable");
    }

    public static boolean replaceEnable() {
        check();
        return configuration.getBoolean("replace.enable");
    }

    public static boolean channelEnable() {
        check();
        return configuration.getBoolean("channel.enable");
    }

    public static Filter.Mode filterMode() {
        check();
        return Filter.Mode.valueOf(configuration.getString("filter.mode", "replace").toUpperCase());
    }

    public static List<TriObj<String, String, Integer>> permissionFormat() {
        check();
        return permissionFormat(configuration.getConfigurationSection("message-format.permissions"));
    }

    private static List<TriObj<String, String, Integer>> permissionFormat(ConfigurationSection section) {
        List<TriObj<String, String, Integer>> formats = new ArrayList<>();
        if (section != null) {
            for (String key : section.getKeys(false)) {
                if (!section.contains(key + ".permission"))
                    continue;
                if (!section.contains(key + ".format"))
                    continue;
                if (!section.isString(key + ".permission"))
                    continue;
                if (!section.isString(key + ".format"))
                    continue;
                int priority = 0;
                formats.add(new TriObj<>(section.getString(key + ".permission"), section.getString(key + ".format"), priority));
            }
        }
        return formats;
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

    public static Map<String, String> replaces() {
        Map<String, String> replaces = new HashMap<>();
        ConfigurationSection section = configuration.getConfigurationSection("replace.replaces");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                if (!section.contains(key + ".key"))
                    continue;
                if (!section.contains(key + ".value"))
                    continue;
                if (!section.isString(key + ".key"))
                    continue;
                if (!section.isString(key + ".value"))
                    continue;
                replaces.put(section.getString(key + ".key"), section.getString(key + ".value"));
            }
        }
        return replaces;
    }

    public static List<FiObj<String, String, Boolean, String, List<TriObj<String, String, Integer>>>> channels() {
        List<FiObj<String, String, Boolean, String, List<TriObj<String, String, Integer>>>> channels = new ArrayList<>();
        ConfigurationSection section = configuration.getConfigurationSection("channel.channels");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                if (!section.contains(key + ".name") || !section.isString(key + ".name"))
                    continue;
                if (Objects.equals(section.getString(key + ".name"), "global"))
                    continue;
                String displayName = null;
                boolean availableForAll = true;
                String format = null;
                List<TriObj<String, String, Integer>> permissionFormats = permissionFormat(section.getConfigurationSection(key + ".permissions"));
                if (section.contains(key + ".display") && section.isString(key + ".display"))
                    displayName = section.getString(key + ".display");
                // if (section.contains(key + ".all-available") && section.isBoolean(key + ".all-available"))
                //     availableForAll = section.getBoolean(key + ".all-available");
                if (section.contains(key + ".format") && section.isString(key + ".format"))
                    format = section.getString(key + ".format");
                channels.add(new FiObj<>(section.getString(key + ".name"), displayName, availableForAll, format, permissionFormats));
            }
        }
        return channels;
    }

    public static String getChatFormat() {
        check();
        return configuration.getString("message-format.chat");
    }

    public static String getWhisperSend() {
        check();
        return configuration.getString("message-format.whisper.send");
    }

    public static String getWhisperReceive() {
        check();
        return configuration.getString("message-format.whisper.receive");
    }

    public static String getSayFormat() {
        check();
        return configuration.getString("message-format.say-command");
    }

    public static String channelMessageFormat() {
        check();
        return configuration.getString("message-format.channel-message");
    }

    public static String lang() {
        check();
        return configuration.getString("multi-language.default");
    }

    public static String lang(String key) {
        return lang(languageConfiguration, key);
    }

    public static String lang(String name, String key) {
        File langFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "languages"), name + ".yml");
        if (!langFile.exists()) {
            return lang(key);
        }
        FileConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
        if (!lang.contains(key)) {
            return lang(key);
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
