package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;

public interface CopyMessageButton extends MessageButton {

    String value(CommandSender clicker, User sender, String message);

    @Override
    default void click(CommandSender clicker, User sender, String message) {
    }

}
