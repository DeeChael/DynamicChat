package net.deechael.dynamichat.api;

import org.bukkit.entity.Player;

public interface PlayerBukkitUser extends BukkitUser, PlayerUser {

    @Override
    Player getSender();

}
