package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.User;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.util.ConfigUtils;
import org.bukkit.command.CommandSender;

public class ConsoleUserEntity extends UserEntity {

    public ConsoleUserEntity(CommandSender sender) {
        super(sender);
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
    public String getDisplayName() {
        return "Console";
    }

    void chat0(CommandSender sender, String format, String message) {
        sender.sendMessage(DynamicChatPlaceholder.replaceSender(sender, ConfigUtils.getChatFormat()).replace("%message%", message));
    }

}
