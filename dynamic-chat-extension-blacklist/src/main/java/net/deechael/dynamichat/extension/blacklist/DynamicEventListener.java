package net.deechael.dynamichat.extension.blacklist;

import net.deechael.dynamichat.api.PlayerUser;
import net.deechael.dynamichat.api.User;
import net.deechael.dynamichat.event.CommandSayEvent;
import net.deechael.dynamichat.event.UserChatEvent;
import net.deechael.dynamichat.event.WhisperEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DynamicEventListener extends JavaPlugin {

    @EventHandler
    public void onChat(UserChatEvent event) {
        if (!(event.getUser() instanceof PlayerUser player))
            return;
        List<PlayerUser> users = new ArrayList<>();
        for (User user : event.getRecipients()) {
            if (user instanceof PlayerUser playerUser) {
                users.add(playerUser);
            }
        }
        String playerName = player.getName().toLowerCase();
        for (PlayerUser playerUser : users) {
            if (BlackListManager.getBlacked(playerUser.getUniqueId()).contains(playerName)) {
                event.getRecipients().remove(playerUser);
            }
        }
    }

    @EventHandler
    public void onWhisper(WhisperEvent event) {
        if (!(event.getSender() instanceof PlayerUser sender))
            return;
        if (!(event.getReceiver() instanceof PlayerUser receiver))
            return;
        if (BlackListManager.getBlacked(Objects.requireNonNull(Bukkit.getPlayer(receiver.getUniqueId()))).contains(sender.getName().toLowerCase())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSayCommand(CommandSayEvent event) {
        if (!(event.getUser() instanceof PlayerUser player))
            return;
        List<PlayerUser> users = new ArrayList<>();
        for (User user : event.getRecipients()) {
            if (user instanceof PlayerUser playerUser) {
                users.add(playerUser);
            }
        }
        String playerName = player.getName().toLowerCase();
        for (PlayerUser playerUser : users) {
            if (BlackListManager.getBlacked(playerUser.getUniqueId()).contains(playerName)) {
                event.getRecipients().remove(playerUser);
            }
        }
    }

}
