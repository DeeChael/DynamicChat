package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.util.ColorUtils;
import net.deechael.dynamichat.util.ConfigUtils;
import net.deechael.dynamichat.util.Lang;
import net.deechael.dynamichat.util.StringUtils;
import net.deechael.useless.objs.DuObj;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;

public class ConsoleUserEntity extends UserEntity {

    public ConsoleUserEntity(CommandSender sender) {
        super(sender);
    }

    @Override
    public void chat(String message) {

    }

    @Override
    public String getDisplayName() {
        return "Console";
    }

    void chat0(CommandSender sender, String format, String message) {
        String formatText = DynamicChatPlaceholder.replaceSender(sender, ConfigUtils.getChatFormat()).replace("%message%", message);
        ComponentBuilder builder = new ComponentBuilder();
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat-cache-clicker message " + DynamicChatManager.addChatCache(new MessageEntity(this, message.replace("§", "&"))));
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
