package net.deechael.dynamichat.api;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ChatManager {

    private static ChatManager manager = null;

    public static void setManager(ChatManager manager) {
        if (ChatManager.manager != null) {
            throw new RuntimeException("You can't set manager because it exists");
        }
        ChatManager.manager = manager;
    }

    public static ChatManager getManager() {
        return manager;
    }

    public abstract User getUser(CommandSender sender);

    public abstract PlayerUser getPlayerUser(Player player);

    public abstract Channel getChannel(String name);

    public abstract List<? extends Channel> getChannels();

    public abstract List<? extends TemporaryChannel> getTemporaryChannels();

    public abstract Channel getGlobal();

    public abstract TemporaryChannel createTemporaryChannel(@Nullable String displayName, @Nullable String format);

}
