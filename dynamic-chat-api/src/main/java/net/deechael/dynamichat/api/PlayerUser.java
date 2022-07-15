package net.deechael.dynamichat.api;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface PlayerUser extends User {

    @Override
    Player getSender();

    UUID getUniqueId();

}
