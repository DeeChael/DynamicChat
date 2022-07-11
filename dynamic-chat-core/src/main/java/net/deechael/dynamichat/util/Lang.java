package net.deechael.dynamichat.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Lang {

    public static void send(CommandSender sender, String key, String... params) {
        if (sender instanceof Player player) {
            if (ConfigUtils.languageFollowClient()) {
                String message = ConfigUtils.lang(player.getLocale(), key);
                for (int i = 0; i < params.length; i++) {
                    message = message.replace("{" + i + "}", params[i]);
                }
                player.sendMessage(message);
                return;
            }
        }
        String message = ConfigUtils.lang(key);
        for (int i = 0; i < params.length; i++) {
            message = message.replace("{" + i + "}", params[i]);
        }
        sender.sendMessage(message);
    }

    public static String lang(CommandSender sender, String key, String... params) {
        if (sender instanceof Player player) {
            if (ConfigUtils.languageFollowClient()) {
                String message = ConfigUtils.lang(player.getLocale(), key);
                for (int i = 0; i < params.length; i++) {
                    message = message.replace("{" + i + "}", params[i]);
                }
                return message;
            }
        }
        String message = ConfigUtils.lang(key);
        for (int i = 0; i < params.length; i++) {
            message = message.replace("{" + i + "}", params[i]);
        }
        return message;
    }

}
