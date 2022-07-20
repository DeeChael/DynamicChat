package net.deechael.dynamichat.extension.blacklist;

import net.deechael.dynamichat.api.*;
import net.deechael.dynamichat.extension.blacklist.commands.CommandBlackList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public final class DyChatBlackListExtensionPlugin extends JavaPlugin {

    public static DyChatBlackListExtensionPlugin getInstance() {
        return JavaPlugin.getPlugin(DyChatBlackListExtensionPlugin.class);
    }

    private static CommandMap getCommandMap() {
        try {
            return (CommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        Lang.checkLanguage();
        Bukkit.getPluginManager().registerEvents(new DynamicEventListener(), this);
        Bukkit.getPluginManager().addPermission(new Permission("dynamichat.blacklist.ignore", PermissionDefault.OP));
        getCommandMap().register("dynamic-chat", new CommandBlackList());
        BukkitChatManager.getManager().registerButton(1, new MessageButton() {
            @Override
            public String display(User clicker, Message message) {
                Player player = (Player) ((BukkitUser) clicker).getSender();
                return Lang.lang(player, "button.display");
            }

            @Override
            public void click(User clicker, Message message) {
                Player player = (Player) ((BukkitUser) clicker).getSender();
                if (player.getName().equalsIgnoreCase(message.getSender().getName())) {
                    Lang.send(player, "command.blacklist.notself");
                    return;
                }
                BlackListManager.add(player, message.getSender().getName().toLowerCase());
                Lang.send(player, "command.blacklist.add.success", message.getSender().getName());
            }

            @Override
            public String hover(User clicker, Message message) {
                Player player = (Player) ((BukkitUser) clicker).getSender();
                return Lang.lang(player, "button.hover");
            }
        });
    }

}
