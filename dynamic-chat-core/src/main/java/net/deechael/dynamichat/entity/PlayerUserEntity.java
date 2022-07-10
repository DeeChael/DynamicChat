package net.deechael.dynamichat.entity;

import me.clip.placeholderapi.PlaceholderAPI;
import net.deechael.dynamichat.api.PlayerUser;
import net.deechael.dynamichat.api.User;
import net.deechael.dynamichat.util.ConfigUtils;
import org.bukkit.command.CommandSender;
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

    @Override
    public void whisper(User another, String message) {

    }

    @Override
    public void say(String message) {

    }

    @Override
    public void chat(String message) {

    }

    @Override
    public String getDisplyName() {
        return player.getDisplayName();
    }

    @Override
    public Player getSender() {
        return player;
    }

    void chat0(String message) {
        getCurrent().broadcast0(PlaceholderAPI.setPlaceholders(this.player, ConfigUtils.getChatFormat()).replace("%message%", message));
    }


}
