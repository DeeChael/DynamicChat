package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class ChatManager {

    private static ChatManager manager = null;

    static void setChatManager(ChatManager manager) {
        if (ChatManager.manager != null) {
            throw new RuntimeException("You can't set manager because it exists");
        }
        ChatManager.manager = manager;
    }

    public static ChatManager getManager() {
        return manager;
    }

    public abstract User getUser(CommandSender sender);

    public abstract PlayerUser getPlayerUser(Player player);

}
