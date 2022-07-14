package net.deechael.dynamichat.entity;

import me.clip.placeholderapi.PlaceholderAPI;
import net.deechael.dynamichat.api.PlayerUser;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.util.ColorUtils;
import net.deechael.dynamichat.util.Lang;
import net.deechael.dynamichat.util.StringUtils;
import net.deechael.useless.objs.DuObj;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.regex.Pattern;

public class PlayerUserEntity extends UserEntity implements PlayerUser {

    private final Player player;

    public PlayerUserEntity(Player player) {
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

    void chat0(CommandSender sender, String format, String message) {
        String formatText = PlaceholderAPI.setPlaceholders(this.player, DynamicChatPlaceholder.replaceSender(this.player, format.replace("%message%", message)));
        ComponentBuilder builder = new ComponentBuilder();
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat-cache-clicker message " + DynamicChatManager.newChatCache(this, message.replace("§", "&")));
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
