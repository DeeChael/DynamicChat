package net.deechael.dynamichat.temp;

import net.deechael.dynamichat.api.ChatManager;
import net.deechael.dynamichat.entity.DynamicChatManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DynamicChatListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        ChatManager.getManager().getPlayerUser(event.getPlayer()).chat(event.getMessage());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ChatManager.getManager().getPlayerUser(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        DynamicChatManager.quit(event.getPlayer());
    }


}
