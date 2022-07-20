package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;

public interface BukkitUser extends User {

    CommandSender getSender();

}
