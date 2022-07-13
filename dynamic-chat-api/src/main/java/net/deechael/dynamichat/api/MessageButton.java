package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;

public interface MessageButton {

    String display(CommandSender clicker, User sender, String message);

    void click(CommandSender clicker, User sender, String message);

    String hover(CommandSender clicker, User sender, String message);

}
