package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Chat manager for bukkit
 */
public abstract class BukkitChatManager implements ChatManager {

    private static BukkitChatManager manager = null;

    /**
     * Get the instance of BukkitChatManager
     *
     * @return the instance
     */
    public static BukkitChatManager getManager() {
        return manager;
    }

    /**
     * Don't touch this method!
     */
    public static void setManager(BukkitChatManager manager) {
        if (BukkitChatManager.manager != null) {
            throw new RuntimeException("You can't set manager because it exists");
        }
        BukkitChatManager.manager = manager;
    }

    /**
     * Get the user instance by sender
     *
     * @param sender sender
     * @return user
     */
    public abstract BukkitUser getBukkitUser(CommandSender sender);

    /**
     * Get the user instance by player
     *
     * @param player player
     * @return user
     */
    public abstract PlayerBukkitUser getBukkitPlayerUser(Player player);

}
