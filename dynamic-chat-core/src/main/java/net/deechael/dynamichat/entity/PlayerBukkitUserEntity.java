package net.deechael.dynamichat.entity;

import me.clip.placeholderapi.PlaceholderAPI;
import net.deechael.dynamichat.api.BukkitChatManager;
import net.deechael.dynamichat.api.PlayerBukkitUser;
import net.deechael.dynamichat.api.Punishment;
import net.deechael.dynamichat.api.Time;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.util.*;
import net.deechael.useless.objs.DuObj;
import net.deechael.useless.objs.FoObj;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class PlayerBukkitUserEntity extends BukkitUserEntity implements PlayerBukkitUser {

    private final Player player;

    public PlayerBukkitUserEntity(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public String getDisplayName() {
        return player.getDisplayName();
    }

    @Override
    public Player getSender() {
        return player;
    }

    @Override
    public void banIP() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        if (BukkitChatManager.getManager().getBanIPManager().isBannedWithPlayer(this))
            throw new RuntimeException("The user's ip has been banned already!");
        BukkitChatManager.getManager().getBanIPManager().banIPWithPlayer(this, "PLUGIN", Lang.lang(this.player, "extension.mute-and-ban.default.no-reason"), new Date(), null);
    }

    @Override
    public void unbanIP() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        if (!BukkitChatManager.getManager().getBanIPManager().isBannedWithPlayer(this))
            throw new RuntimeException("The user's ip hasn't been banned!");
        BukkitChatManager.getManager().getBanIPManager().unbanIPWithPlayer(this.getName());
    }

    @Override
    public void unban() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(player);
        if (obj.getFirst())
            throw new RuntimeException("The player hasn't been banned!");
        MuteNBanManager.unbanned(player, obj.getFourth());
    }

    @Override
    public void unmute() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(player);
        if (obj.getFirst())
            throw new RuntimeException("The player hasn't been muted!");
        MuteNBanManager.unmuted(player, obj.getFourth());
    }

    @Override
    public void ban() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        this.ban((Date) null, Lang.lang(this.player, "extension.mute-and-ban.default.no-reason"));
    }

    @Override
    public void ban(Time time) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        this.ban(time, Lang.lang(this.player, "extension.mute-and-ban.default.no-reason"));
    }

    @Override
    public void ban(Date unbanDate) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        this.ban(unbanDate, Lang.lang(this.player, "extension.mute-and-ban.default.no-reason"));
    }

    @Override
    public void ban(String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        this.ban((Date) null, reason);
    }

    @Override
    public void ban(Time time, String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        Date date = null;
        if (time != null)
            date = time.after(new Date());
        this.ban(date, reason);
    }

    @Override
    public void ban(Date unbanDate, String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        if (MuteNBanManager.isNowBanned(player).getFirst())
            throw new RuntimeException("The player has been banned!");
        Punishment punishment = MuteNBanManager.addMuted("PLUGIN", player, unbanDate, reason);

        if (unbanDate != null) {
            Time time = BukkitChatManager.getManager().createTime((unbanDate.getTime() - new Date().getTime()) / 1000L);
            player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned.temporary", punishment.getId(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMinutes(), time.getSeconds(), punishment.getReason()));
        } else {
            player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned.forever", punishment.getId(), punishment.getReason()));
        }
    }

    @Override
    public void kick() {
        this.kick(Lang.lang(this.player, "extension.mute-and-ban.default.no-reason"));
    }

    @Override
    public void kick(String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        this.player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.kicked", reason));
    }

    @Override
    public void mute() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        this.mute((Date) null, Lang.lang(this.player, "extension.mute-and-ban.default.no-reason"));
    }

    @Override
    public void mute(Time time) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        this.mute(time, Lang.lang(this.player, "extension.mute-and-ban.default.no-reason"));
    }

    @Override
    public void mute(Date unmuteDate) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        this.mute(unmuteDate, Lang.lang(this.player, "extension.mute-and-ban.default.no-reason"));
    }

    @Override
    public void mute(String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        this.mute((Date) null, reason);
    }

    @Override
    public void mute(Time time, String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        Date date = null;
        if (time != null)
            date = time.after(new Date());
        this.mute(date, reason);
    }

    @Override
    public void mute(Date unmuteDate, String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        if (MuteNBanManager.isNowMuted(player).getFirst())
            throw new RuntimeException("The player has been muted!");
        Punishment punishment = MuteNBanManager.addMuted("PLUGIN", player, unmuteDate, reason);

        if (unmuteDate != null) {
            Time time = BukkitChatManager.getManager().createTime((unmuteDate.getTime() - new Date().getTime()) / 1000L);
            player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned.temporary", punishment.getId(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMinutes(), time.getSeconds(), punishment.getReason()));
        } else {
            player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned.forever", punishment.getId(), punishment.getReason()));
        }
    }

    @Override
    public List<? extends Punishment> getBanHistory() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        return MuteNBanManager.getBannedHistory(player);
    }

    @Override
    public List<? extends Punishment> getMuteHistory() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        return MuteNBanManager.getMutedHistory(player);
    }

    @Override
    public List<? extends Punishment> getPunishHistory() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        List<Punishment> punishments = new ArrayList<>();
        punishments.addAll(getBanHistory());
        punishments.addAll(getMuteHistory());
        return punishments;
    }

    @Override
    public boolean isBanned() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        return MuteNBanManager.isNowBanned(player).getFirst();
    }

    @Override
    public boolean isMuted() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        return MuteNBanManager.isNowMuted(player).getFirst();
    }

    @Override
    public Punishment getBan(String id) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        return MuteNBanManager.getBanById(player, id);
    }

    @Override
    public Punishment getMute(String id) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        return MuteNBanManager.getMuteById(player, id);
    }

    void chat0(CommandSender sender, String format, String message, String msgId) {
        String formatText = PlaceholderAPI.setPlaceholders(this.player, DynamicChatPlaceholder.replaceSender(this.player, format.replace("%message%", message)));
        ComponentBuilder builder = new ComponentBuilder();
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat-cache-clicker message " + msgId);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Lang.lang(sender, "message.hover")));
        DuObj<String[], String[]> du = StringUtils.split(formatText, Pattern.compile("§x(§([a-fA-F\\d])){6}"));
        String[] contents = du.getFirst();
        String[] spliters = du.getSecond();
        ChatColor lastColor = ChatColor.RESET;
        for (int i = 0; i < contents.length; i++) {
            if (i > 0) {
                ComponentBuilder contentBuilder = new ComponentBuilder();
                lastColor = ChatColor.of("#" + spliters[i - 1].replace("§", "").substring(1));
                contentBuilder.color(lastColor);
                if (Pattern.matches(".*§[abcdefklmnorABCDEFKLMNOR0123456789].*", contents[i])) {
                    DuObj<String[], String[]> du2 = StringUtils.split(contents[i], Pattern.compile("§[abcdefklmnorABCDEFKLMNOR0123456789]"));
                    String[] contents2 = du2.getFirst();
                    String[] spliters2 = du2.getSecond();
                    for (int j = 0; j < contents2.length; j++) {
                        if (j > 0) {
                            BaseComponent component = new TextComponent(contents2[j]);
                            lastColor = ColorUtils.iHateSpigotBungeecordAndMD5(spliters2[j - 1].substring(1));
                            component.setColor(lastColor);
                            component.setClickEvent(clickEvent);
                            component.setHoverEvent(hoverEvent);
                            contentBuilder.append(component);
                        } else {
                            contentBuilder.append(new ComponentBuilder(contents2[j]).color(lastColor).event(clickEvent).event(hoverEvent).create());
                        }
                        if (spliters2.length - 1 == j && j == contents2.length - 1) {
                            lastColor = ColorUtils.iHateSpigotBungeecordAndMD5(spliters2[j].substring(1));
                            contentBuilder.append(new ComponentBuilder("").event(clickEvent).event(hoverEvent).color(lastColor).create());
                        }
                    }
                } else {
                    contentBuilder.append(new ComponentBuilder(contents[i]).color(lastColor).event(clickEvent).event(hoverEvent).create());
                }
                builder.append(contentBuilder.event(clickEvent).event(hoverEvent).create());
            } else {
                if (Pattern.matches(".*§[abcdefklmnorABCDEFKLMNOR0123456789].*", contents[i])) {
                    DuObj<String[], String[]> du2 = StringUtils.split(contents[i], Pattern.compile("§[abcdefklmnorABCDEFKLMNOR0123456789]"));
                    String[] contents2 = du2.getFirst();
                    String[] spliters2 = du2.getSecond();
                    for (int j = 0; j < contents2.length; j++) {
                        if (j > 0) {
                            BaseComponent component = new TextComponent(contents2[j]);
                            lastColor = ColorUtils.iHateSpigotBungeecordAndMD5(spliters2[j - 1].substring(1));
                            component.setColor(lastColor);
                            component.setClickEvent(clickEvent);
                            component.setHoverEvent(hoverEvent);
                            builder.append(component);
                        } else {
                            builder.append(new ComponentBuilder(contents2[j]).color(lastColor).event(clickEvent).event(hoverEvent).create());
                        }
                        if (spliters2.length - 1 == j && j == contents2.length - 1) {
                            lastColor = ColorUtils.iHateSpigotBungeecordAndMD5(spliters2[j].substring(1));
                            builder.append(new ComponentBuilder().event(clickEvent).event(hoverEvent).color(lastColor).create());
                        }
                    }
                } else {
                    builder.append(new ComponentBuilder(contents[i]).color(lastColor).event(clickEvent).event(hoverEvent).create());
                }
            }
            if (spliters.length - 1 == i && i == contents.length - 1) {
                lastColor = ChatColor.of("#" + spliters[i].replace("§", "").substring(1));
                builder.append(new ComponentBuilder("").event(clickEvent).event(hoverEvent).color(lastColor).create());
            }
        }
        builder.event(clickEvent).event(hoverEvent);
        BaseComponent[] components = builder.create();
        sender.spigot().sendMessage(components);
    }


}
