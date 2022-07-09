package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.PlayerUser;
import net.deechael.dynamichat.api.User;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerUserEntity extends UserEntity implements PlayerUser {

    private final Player player;

    public PlayerUserEntity(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

}
