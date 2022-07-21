package net.deechael.dynamichat.api;

import org.bukkit.entity.Player;

/**
 * PlayerUser for Bukkit
 */
public interface PlayerBukkitUser extends BukkitUser, PlayerUser {

    /**
     * Get bukkit player
     *
     * @return bukkit player
     */
    @Override
    Player getSender();

}
