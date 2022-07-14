package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;

public interface MessageButton {

    String display(CommandSender clicker, Message message);

    void click(CommandSender clicker, Message message);

    String hover(CommandSender clicker, Message message);

}
