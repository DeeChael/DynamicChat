package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Punishment;
import net.deechael.dynamichat.api.Time;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.util.*;
import net.deechael.useless.objs.DuObj;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class ConsoleBukkitUserEntity extends BukkitUserEntity {

    public ConsoleBukkitUserEntity(CommandSender sender) {
        super(sender);
    }

    @Override
    public void chat(String message) {

    }

    @Override
    public String getDisplayName() {
        return "Console";
    }

    @Override
    public void banIP() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void unbanIP() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void unban() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void unmute() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void ban() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void ban(Time time) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void ban(Date unbanDate) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void ban(String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void ban(Time time, String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void ban(Date unbanDate, String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void kick() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void kick(String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void mute() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void mute(Time time) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void mute(Date unmuteDate) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void mute(String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void mute(Time time, String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public void mute(Date unmuteDate, String reason) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public List<? extends Punishment> getBanHistory() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public List<? extends Punishment> getMuteHistory() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public List<? extends Punishment> getPunishHistory() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public boolean isBanned() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public boolean isMuted() {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public Punishment getBan(String id) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    @Override
    public Punishment getMute(String id) {
        if (!ExtensionUtils.enabledMuteAndBanExtension())
            throw new RuntimeException("Extension: Mute And Ban didn't be installed!");
        throw new RuntimeException("You can do this to a console sender");
    }

    void chat0(CommandSender sender, String format, String message, String msgId) {
        String formatText = DynamicChatPlaceholder.replaceSender(sender, ConfigUtils.getChatFormat()).replace("%message%", message);
        ComponentBuilder builder = new ComponentBuilder();
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat-cache-clicker message " + msgId);
        HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Lang.lang(sender, "message.hover")));
        DuObj<String[], String[]> du = StringUtils.split(formatText, Pattern.compile("§x(§([a-fA-F\\d])){6}"));
        String[] contents = du.getFirst();
        String[] spliters = du.getSecond();
        for (int i = 0; i < contents.length; i++) {
            if (i > 0) {
                ComponentBuilder contentBuilder = new ComponentBuilder();
                if (Pattern.matches(".*§[abcdefklmnorABCDEFKLMNOR0123456789].*", contents[i])) {
                    DuObj<String[], String[]> du2 = StringUtils.split(contents[i], Pattern.compile("§[abcdefklmnorABCDEFKLMNOR0123456789]"));
                    String[] contents2 = du2.getFirst();
                    String[] spliters2 = du2.getSecond();
                    for (int j = 0; j < contents2.length; j++) {
                        if (j > 0) {
                            BaseComponent component = new TextComponent(contents2[j]);
                            component.setColor(ColorUtils.iHateSpigotBungeecordAndMD5(spliters2[j - 1].substring(1)));
                            component.setClickEvent(clickEvent);
                            component.setHoverEvent(hoverEvent);
                            contentBuilder.append(component);
                        } else {
                            contentBuilder.append(new ComponentBuilder(contents2[j]).event(clickEvent).event(hoverEvent).create());
                        }
                        if (spliters2.length - 1 == j && j == contents2.length - 1) {
                            contentBuilder.append(new ComponentBuilder("").event(clickEvent).event(hoverEvent).color(ColorUtils.iHateSpigotBungeecordAndMD5(spliters2[j].substring(1))).create());
                        }
                    }
                } else {
                    contentBuilder.append(new ComponentBuilder(contents[i]).event(clickEvent).event(hoverEvent).create());
                }
                contentBuilder.color(ChatColor.of("#" + spliters[i - 1].replace("§", "").substring(1)));
                builder.append(contentBuilder.event(clickEvent).event(hoverEvent).create());
            } else {
                if (Pattern.matches(".*§[abcdefklmnorABCDEFKLMNOR0123456789].*", contents[i])) {
                    DuObj<String[], String[]> du2 = StringUtils.split(contents[i], Pattern.compile("§[abcdefklmnorABCDEFKLMNOR0123456789]"));
                    String[] contents2 = du2.getFirst();
                    String[] spliters2 = du2.getSecond();
                    for (int j = 0; j < contents2.length; j++) {
                        if (j > 0) {
                            BaseComponent component = new TextComponent(contents2[j]);
                            component.setColor(ColorUtils.iHateSpigotBungeecordAndMD5(spliters2[j - 1].substring(1)));
                            component.setClickEvent(clickEvent);
                            component.setHoverEvent(hoverEvent);
                            builder.append(component);
                        } else {
                            builder.append(new ComponentBuilder(contents2[j]).event(clickEvent).event(hoverEvent).create());
                        }
                        if (spliters2.length - 1 == j && j == contents2.length - 1) {
                            builder.append(new ComponentBuilder().event(clickEvent).event(hoverEvent).color(ColorUtils.iHateSpigotBungeecordAndMD5(spliters2[j].substring(1))).create());
                        }
                    }
                } else {
                    builder.append(new ComponentBuilder(contents[i]).event(clickEvent).event(hoverEvent).create());
                }
            }
            if (spliters.length - 1 == i && i == contents.length - 1) {
                builder.append(new ComponentBuilder("").event(clickEvent).event(hoverEvent).color(ChatColor.of("#" + spliters[i].replace("§", "").substring(1))).create());
            }
        }
        builder.event(clickEvent).event(hoverEvent);
        BaseComponent[] components = builder.create();
        sender.spigot().sendMessage(builder.create());
    }

}
