package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;

public interface CopyMessageButton extends MessageButton {

    String value(CommandSender clicker, Message message);

    @Override
    default void click(CommandSender clicker, Message message) {
    }

}
