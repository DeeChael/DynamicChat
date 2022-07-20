package net.deechael.dynamichat.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.deechael.dynamichat.DyChatPlugin;
import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.BukkitChatManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicChatPlaceholder extends PlaceholderExpansion {

    public static String replaceSender(CommandSender sender, String message) {
        message = message.replace("%sender%", sender.getName());
        if (sender instanceof Player) {
            message = message.replace("%senderDisplay%", ((Player) sender).getDisplayName());
            message = PlaceholderAPI.setPlaceholders((Player) sender, message);
        } else {
            message = message.replace("%senderDisplay%", sender.getName());
        }
        return message;
    }

    public static String replaceReceiver(CommandSender sender, String message) {
        message = message.replace("%receiver%", sender.getName());
        if (sender instanceof Player) {
            message = message.replace("%receiverDisplay%", ((Player) sender).getDisplayName());
            message = PlaceholderAPI.setPlaceholders((Player) sender, message);
        } else {
            message = message.replace("%receiverDisplay%", sender.getName());
        }
        return message;
    }

    public static String replaceChannel(Channel channel, String message) {
        return message.replace("%dynamichat_currentChannel%", channel.getName()).replace("%dynamichat_currentChannelDisplay%", channel.getDisplayName());
    }

    @Override
    @Nullable
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        return switch (identifier) {
            case "currentChannel" -> BukkitChatManager.getManager().getBukkitPlayerUser(player).getCurrent().getName();
            case "currentChannelDisplay" ->
                    BukkitChatManager.getManager().getBukkitPlayerUser(player).getCurrent().getDisplayName();
            default -> null;
        };
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "dynamichat";
    }

    @Override
    @NotNull
    public String getAuthor() {
        return "DeeChael";
    }

    @Override
    @NotNull
    public String getVersion() {
        return "1.00.0";
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return DyChatPlugin.getInstance().getName();
    }

}
