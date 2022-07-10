package net.deechael.dynamichat.temp;

import net.deechael.dynamichat.api.ChatManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class DynamicChatListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        ChatManager.getManager().getPlayerUser(event.getPlayer()).chat(event.getMessage());
    }

}
