package net.deechael.dynamichat.extension.blacklist;

import net.deechael.dynamichat.api.ChatManager;
import net.deechael.dynamichat.api.MessageButton;
import net.deechael.dynamichat.api.User;
import net.deechael.dynamichat.extension.blacklist.commands.CommandBlackList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class DyChatBlackListExtensionPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Lang.checkLanguage();
        getCommandMap().register("dynamic-chat", new CommandBlackList());
        ChatManager.getManager().registerButton(1, new MessageButton() {
            @Override
            public String display(CommandSender clicker, User sender, String message) {
                Player player = (Player) clicker;
                return Lang.lang(player, "button.display");
            }

            @Override
            public void click(CommandSender clicker, User sender, String message) {
                Player player = (Player) clicker;
                if (player.getName().equalsIgnoreCase(sender.getName())) {
                    Lang.send(player, "command.blacklist.notself");
                    return;
                }
                BlackListManager.add(player, sender.getName().toLowerCase());
                Lang.send(player, "command.blacklist.add.success", sender.getName());
            }

            @Override
            public String hover(CommandSender clicker, User sender, String message) {
                Player player = (Player) clicker;
                return Lang.lang(player, "button.hover");
            }
        });
    }

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

}
