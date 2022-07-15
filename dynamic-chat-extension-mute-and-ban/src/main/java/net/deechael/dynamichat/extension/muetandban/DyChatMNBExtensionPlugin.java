package net.deechael.dynamichat.extension.muetandban;

import net.deechael.dynamichat.api.ChatManager;
import net.deechael.dynamichat.api.Message;
import net.deechael.dynamichat.api.MessageButton;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

public final class DyChatMNBExtensionPlugin extends JavaPlugin {

    public static DyChatMNBExtensionPlugin getInstance() {
        return JavaPlugin.getPlugin(DyChatMNBExtensionPlugin.class);
    }

    @Override
    public void onEnable() {
        Config.checkConfig();
        Lang.checkLanguage();
        Bukkit.getPluginManager().addPermission(new Permission("dynamichat.mnb.mute", PermissionDefault.OP));
        Bukkit.getPluginManager().addPermission(new Permission("dynamichat.mnb.ban", PermissionDefault.OP));
        ChatManager.getManager().registerButton(3, new MessageButton() {
            @Override
            public String display(CommandSender clicker, Message message) {
                if (clicker.hasPermission("dynamichat.mnb.mute")) {
                    Player player = (Player) clicker;
                    return Lang.lang(player, "button.mute.display");
                }
                return null;
            }

            @Override
            public void click(CommandSender clicker, Message message) {
                if (clicker.hasPermission("dynamichat.mnb.mute")) {

                }
            }

            @Override
            public String hover(CommandSender clicker, Message message) {
                Player player = (Player) clicker;
                return Lang.lang(player, "button.mute.hover");
            }
        });
        ChatManager.getManager().registerButton(4, new MessageButton() {
            @Override
            public String display(CommandSender clicker, Message message) {
                if (clicker.hasPermission("dynamichat.mnb.ban")) {
                    Player player = (Player) clicker;
                    return Lang.lang(player, "button.ban.display");
                }
                return null;
            }

            @Override
            public void click(CommandSender clicker, Message message) {
                if (clicker.hasPermission("dynamichat.mnb.ban")) {

                }
            }

            @Override
            public String hover(CommandSender clicker, Message message) {
                Player player = (Player) clicker;
                return Lang.lang(player, "button.ban.hover");
            }
        });
    }

}
