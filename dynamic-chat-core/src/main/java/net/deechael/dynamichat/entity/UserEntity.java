package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.User;
import org.bukkit.command.CommandSender;

import java.util.List;

public class UserEntity implements User {

    private final CommandSender sender;

    public UserEntity(CommandSender sender) {
        this.sender = sender;
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
    public Channel getCurrent() {
        return null;
    }

    @Override
    public List<Channel> getAvailable() {
        return null;
    }

}
