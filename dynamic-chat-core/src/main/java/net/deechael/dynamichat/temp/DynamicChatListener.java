package net.deechael.dynamichat.temp;

import net.deechael.dynamichat.api.BukkitChatManager;
import net.deechael.dynamichat.api.ChatManager;
import net.deechael.dynamichat.entity.BanIPPunishmentEntity;
import net.deechael.dynamichat.entity.DynamicBukkitChatManager;
import net.deechael.dynamichat.event.UserChatEvent;
import net.deechael.dynamichat.object.BanIPPunishment;
import net.deechael.dynamichat.object.Time;
import net.deechael.dynamichat.util.Lang;
import net.deechael.dynamichat.util.MuteNBanManager;
import net.deechael.useless.objs.DuObj;
import net.deechael.useless.objs.FoObj;
import net.deechael.useless.objs.TriObj;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Date;

public class DynamicChatListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChat(UserChatEvent event) {
        if (event.getUser().getSender() instanceof Player player) {
            TriObj<Boolean, String, Date> obj = MuteNBanManager.isNowMuted(player);
            Date date = obj.getThird();
            if (date != null) {
                Time time = BukkitChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
                if (obj.getFirst()) {
                    event.setCancelled(true);
                    Lang.send(player, "extension.mute-and-ban.message.muted.onchat.temporary", time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMinutes(), time.getSeconds());
                }
            } else {
                if (obj.getFirst()) {
                    event.setCancelled(true);
                    Lang.send(player, "extension.mute-and-ban.message.muted.onchat.forever");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        BukkitChatManager.getManager().getBukkitPlayerUser(event.getPlayer()).chat(event.getMessage());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(event.getPlayer());
        if (obj.getFirst()) {
            Date date = obj.getThird();
            if (date != null) {
                Time time = BukkitChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
                event.getPlayer().kickPlayer(Lang.lang(event.getPlayer(), "extension.mute-and-ban.message.banned.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMinutes(), time.getSeconds(), obj.getSecond()));
            } else {
                event.getPlayer().kickPlayer(Lang.lang(event.getPlayer(), "extension.mute-and-ban.message.banned.forever", obj.getFourth(), obj.getSecond()));
            }
            event.setJoinMessage("");
        } else {
            DuObj<Boolean, BanIPPunishment> banIp = BukkitChatManager.getManager().getBanIPManager().getCurrent(event.getPlayer().getAddress().getHostString());
            if (banIp.getFirst()) {
                Date date = banIp.getSecond().getEndDate();
                if (date != null) {
                    Time time = BukkitChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
                    event.getPlayer().kickPlayer(Lang.lang(event.getPlayer(), "extension.mute-and-ban.message.banned-ip.temporary", time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMinutes(), time.getSeconds(), banIp.getSecond().getReason()));
                } else {
                    event.getPlayer().kickPlayer(Lang.lang(event.getPlayer(), "extension.mute-and-ban.message.banned-ip.forever", banIp.getSecond().getReason()));
                }
                event.setJoinMessage("");
            } else {
                BukkitChatManager.getManager().getBukkitPlayerUser(event.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        if (!DynamicBukkitChatManager.quit(event.getPlayer())) {
            if (MuteNBanManager.isNowBanned(event.getPlayer()).getFirst()) {
                event.setQuitMessage("");
            } else if (BukkitChatManager.getManager().getBanIPManager().isBanned(event.getPlayer().getAddress().getHostString()))
                event.setQuitMessage("");
        }
    }


}
