package net.deechael.dynamichat.extension.report;

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
        File langFile = new File(new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "languages"), "en_us.yml");
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
        setDefault(configuration, "command.mustbeplayer", "&c&l(!) &r&cYou must be a player!");
        setDefault(configuration, "command.nopermission", "&c&l(!) &r&cYou don't have the permission!");
        setDefault(configuration, "command.report.toofast", "&c&l(!) &r&cYour report cooldown hasn't finished yet!");
        setDefault(configuration, "command.report.gotohelp", "&c&l(!) &r&cType \"/report help\" to get help");
        setDefault(configuration, "command.report.help", List.of("&6&l==============================",
                "&e/report help - get help",
                "&e/report report <player> - report a player",
                "&e/report reports - open report management gui",
                "&6&l=============================="));
        setDefault(configuration, "command.report.success", "&a&l(!) &r&aYou reported &b{0} &asuccessfully!");
        setDefault(configuration, "command.report.notonline", "&c&l(!) &r&cPlayer &r&f{0} &r&cis not online!");
        setDefault(configuration, "command.message.cannotlocate", "&c&l(!) &r&cCannot locate the message!");
        setDefault(configuration, "button.display", "&c&l[&r&cReport&r&c&l]");
        setDefault(configuration, "button.hover", "&b&lReport this player");
        setDefault(configuration, "message.mention.player", "&a&l(!) &r&aOne of your report has been approved!");
        setDefault(configuration, "message.mention.admin", "&a&l(!) &r&aA new report was appealed!");
        setDefault(configuration, "message.approve.approved", "&a&l(!) &r&aApproved the report successfully!");
        setDefault(configuration, "message.hasreported", "&c&l(!) &r&cYou have reported this message already!");
        setDefault(configuration, "message.notself", "&c&l(!) &r&cYou cannot report yourself!");
        setDefault(configuration, "gui.reason.tips", "Reason");
        setDefault(configuration, "gui.comment.tips", "Comment");
        setDefault(configuration, "gui.reason.button.report.display", "§a§lReport");
        setDefault(configuration, "gui.reason.button.report.lore", "§eClick here to report!");
        setDefault(configuration, "gui.reports.title", "&c>> DynamicChat | Reports | &c&l(&r&6&l{0}&r&c&l&c/&r&6&l{1}&r&c&l)");
        setDefault(configuration, "gui.admin.title", "&c>> DynamicChat | Admin | &c&l(&r&6&l{0}&r&c&l&c/&r&6&l{1}&r&c&l)");
        setDefault(configuration, "gui.approve.title", "&c&lApprove Report");
        setDefault(configuration, "gui.reports.report.title", "&c&lReports | {0}");
        setDefault(configuration, "gui.reports.message.title", "&c&lMessage | {0}");
        setDefault(configuration, "gui.reports.report.admin.title", "&c&lReports | &r&f{0}&r&c&l | {1}");
        setDefault(configuration, "gui.reports.report.item.suspect.display", "&c&lSuspect: &r&f{0}");
        setDefault(configuration, "gui.reports.report.item.suspect.lore", "&b&lLast Online:&r&a {0}");
        setDefault(configuration, "gui.reports.report.item.message.display", "&b&lMessage");
        setDefault(configuration, "gui.reports.report.item.message.lore", "&b&lClick to view context");
        setDefault(configuration, "gui.reports.report.item.reason.display", "&b&lReason");
        setDefault(configuration, "gui.reports.report.item.approved.lore", List.of("&b&lAdmin: &r&a{0}", "&b&lDate: &r&a{1}", "&b&lComment: &r{2}"));
        setDefault(configuration, "gui.reports.item.approved.prefix", "&a&l{0}");
        setDefault(configuration, "gui.reports.item.unapproved.prefix", "&c&l{0}");
        setDefault(configuration, "gui.reports.item.name.approved.text", "Approved");
        setDefault(configuration, "gui.reports.item.name.unapproved.text", "Unapproved");
        setDefault(configuration, "gui.reports.item.lore", List.of("&a&lSuspect: &r&b{0}",
                "&a&lApproved: &r&b{1}"));
        setDefault(configuration, "gui.reports.item.admin.lore", List.of("&a&lReporter: &r&b{0}",
                "&a&lSuspect: &r&b{1}",
                "&a&lApproved: &r&b{2}"));
        setDefault(configuration, "gui.reports.button.next", "&a&lNext Page");
        setDefault(configuration, "gui.reports.button.previous", "&c&lPrevious Page");
        setDefault(configuration, "gui.reports.button.back", "&c&lBack");
        setDefault(configuration, "gui.reports.admin.button.solve.display", "&a&lApprove");
        setDefault(configuration, "gui.reports.admin.button.solve.lore", "&b&lClick here to approve");
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
        if (BukkitChatManager.getManager().languageFollowClient()) {
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
        return lang(BukkitChatManager.getManager().getDefaultLanguage(), key);
    }

    private static String lang(String name, String key) {
        File langFile = new File(new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "languages"), name + ".yml");
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
