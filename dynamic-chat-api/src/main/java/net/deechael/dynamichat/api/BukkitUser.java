package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;

/**
 * User for bukkit
 */
public interface BukkitUser extends User {

    /**
     * Get bukkit command sender
     *
     * @return bukkit command sender
     */
    CommandSender getSender();

}
