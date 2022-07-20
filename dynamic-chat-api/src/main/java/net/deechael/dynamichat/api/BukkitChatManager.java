package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BukkitChatManager implements ChatManager {

    private static BukkitChatManager manager = null;

    public static BukkitChatManager getManager() {
        return manager;
    }

    public static void setManager(BukkitChatManager manager) {
        if (BukkitChatManager.manager != null) {
            throw new RuntimeException("You can't set manager because it exists");
        }
        BukkitChatManager.manager = manager;
    }

    public abstract BukkitUser getBukkitUser(CommandSender sender);

    public abstract PlayerBukkitUser getBukkitPlayerUser(Player player);

}
