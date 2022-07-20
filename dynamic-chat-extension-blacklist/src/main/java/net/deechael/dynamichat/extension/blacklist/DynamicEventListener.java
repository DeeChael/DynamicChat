package net.deechael.dynamichat.extension.blacklist;

import net.deechael.dynamichat.api.PlayerBukkitUser;
import net.deechael.dynamichat.api.BukkitUser;
import net.deechael.dynamichat.event.CommandSayEvent;
import net.deechael.dynamichat.event.UserChatEvent;
import net.deechael.dynamichat.event.WhisperEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DynamicEventListener implements Listener {

    @EventHandler
    public void onChat(UserChatEvent event) {
        if (event.getUser().getSender().hasPermission("dynamichat.blacklist.ignore"))
            return;
        if (!(event.getUser() instanceof PlayerBukkitUser player))
            return;
        List<PlayerBukkitUser> users = new ArrayList<>();
        for (BukkitUser bukkitUser : event.getRecipients()) {
            if (bukkitUser instanceof PlayerBukkitUser playerUser) {
                users.add(playerUser);
            }
        }
        String playerName = player.getName().toLowerCase();
        for (PlayerBukkitUser playerUser : users) {
            if (BlackListManager.getBlacked(playerUser.getUniqueId()).contains(playerName)) {
                event.getRecipients().remove(playerUser);
            }
        }
    }

    @EventHandler
    public void onWhisper(WhisperEvent event) {
        if (event.getSender().getSender().hasPermission("dynamichat.blacklist.ignore"))
            return;
        if (!(event.getSender() instanceof PlayerBukkitUser sender))
            return;
        if (!(event.getReceiver() instanceof PlayerBukkitUser receiver))
            return;
        if (BlackListManager.getBlacked(Objects.requireNonNull(Bukkit.getPlayer(receiver.getUniqueId()))).contains(sender.getName().toLowerCase())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSayCommand(CommandSayEvent event) {
        if (event.getUser().getSender().hasPermission("dynamichat.blacklist.ignore"))
            return;
        if (!(event.getUser() instanceof PlayerBukkitUser player))
            return;
        List<PlayerBukkitUser> users = new ArrayList<>();
        for (BukkitUser bukkitUser : event.getRecipients()) {
            if (bukkitUser instanceof PlayerBukkitUser playerUser) {
                users.add(playerUser);
            }
        }
        String playerName = player.getName().toLowerCase();
        for (PlayerBukkitUser playerUser : users) {
            if (BlackListManager.getBlacked(playerUser.getUniqueId()).contains(playerName)) {
                event.getRecipients().remove(playerUser);
            }
        }
    }

}
