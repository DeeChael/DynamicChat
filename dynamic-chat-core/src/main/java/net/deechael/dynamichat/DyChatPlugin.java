package net.deechael.dynamichat;

import net.deechael.dynamichat.api.*;
import net.deechael.dynamichat.command.EzArgument;
import net.deechael.dynamichat.command.EzCommand;
import net.deechael.dynamichat.command.EzCommandManager;
import net.deechael.dynamichat.command.argument.ArgumentChat;
import net.deechael.dynamichat.command.argument.ArgumentOfflinePlayer;
import net.deechael.dynamichat.command.argument.ArgumentPlayer;
import net.deechael.dynamichat.command.argument.BaseArguments;
import net.deechael.dynamichat.entity.ChannelEntity;
import net.deechael.dynamichat.entity.DynamicChatManager;
import net.deechael.dynamichat.entity.TimeEntity;
import net.deechael.dynamichat.entity.UserEntity;
import net.deechael.dynamichat.exception.TimeParseException;
import net.deechael.dynamichat.gui.AnvilGUI;
import net.deechael.dynamichat.gui.AnvilOutputImage;
import net.deechael.dynamichat.gui.Image;
import net.deechael.dynamichat.object.Time;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.temp.DynamicChatListener;
import net.deechael.dynamichat.temp.NMSCommandKiller;
import net.deechael.dynamichat.util.*;
import net.deechael.useless.objs.FoObj;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DyChatPlugin extends JavaPlugin {

    public static DyChatPlugin getInstance() {
        return JavaPlugin.getPlugin(DyChatPlugin.class);
    }

    @Override
    public void onLoad() {
        ConfigUtils.load();
        ChatManager.setManager(new DynamicChatManager());
        ChatManager.getManager().getUser(Bukkit.getConsoleSender());
        DynamicChatManager.reload();
    }

    @Override
    public void onEnable() {
        new AnvilGUI(this).drop();
        new DynamicChatPlaceholder().register();
        Bukkit.getPluginManager().registerEvents(EzCommandManager.INSTANCE, this);
        Bukkit.getPluginManager().registerEvents(new DynamicChatListener(), this);
        ChatManager.getManager().registerButton(0, new CopyMessageButton() {
            @Override
            public String value(CommandSender clicker, Message message) {
                return message.getContent();
            }

            @Override
            public String display(CommandSender clicker, Message message) {
                return Lang.lang(clicker, "message.button.copy.display");
            }

            @Override
            public String hover(CommandSender clicker, Message message) {
                return Lang.lang(clicker, "message.button.copy.hover");
            }

        });
        ChatManager.getManager().registerButton(3, new MessageButton() {
            @Override
            public String display(CommandSender clicker, Message message) {
                if (ExtensionUtils.enabledMuteAndBanExtension()) {
                    if (clicker.hasPermission("dynamichat.mnb.mute")) {
                        Player player = (Player) clicker;
                        return Lang.lang(player, "extension.mute-and-ban.button.mute.display");
                    }
                }
                return null;
            }

            @Override
            public void click(CommandSender clicker, Message message) {
                if (clicker instanceof Player player) {
                    if (message.getSender().getSender() instanceof Player bePunished) {
                        if (clicker.hasPermission("dynamichat.mnb.mute")) {
                            if (player.getUniqueId() == bePunished.getUniqueId()) {
                                Lang.send(clicker, "extension.mute-and-ban.command.mute.notself");
                            } else {
                                if (MuteNBanManager.isNowMuted(bePunished).getFirst()) {
                                    Lang.send(clicker, "extension.mute-and-ban.command.mute.failed", player.getName());
                                } else {
                                    MNBGUIUtils.openSetTime(player, bePunished, 0, (punish, reason, timeLong) -> {
                                        if (timeLong == 0) {
                                            MuteNBanManager.addMuted(clicker.getName(), punish, null, reason);
                                            if (punish.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(punish);
                                                Lang.send(punish, "extension.mute-and-ban.message.muted.forever", obj.getFourth(), obj.getSecond());
                                            }
                                        } else {
                                            Time time = new TimeEntity(timeLong);
                                            MuteNBanManager.addMuted(clicker.getName(), punish, time.after(new Date()), reason);
                                            if (punish.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(punish);
                                                Lang.send(punish, "extension.mute-and-ban.message.muted.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond());
                                            }
                                        }
                                        Lang.send(clicker, "extension.mute-and-ban.command.mute.success", punish.getName());
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public String hover(CommandSender clicker, Message message) {
                Player player = (Player) clicker;
                return Lang.lang(player, "extension.mute-and-ban.button.mute.hover");
            }
        });
        ChatManager.getManager().registerButton(4, new MessageButton() {
            @Override
            public String display(CommandSender clicker, Message message) {
                if (ExtensionUtils.enabledMuteAndBanExtension()) {
                    if (clicker.hasPermission("dynamichat.mnb.kick")) {
                        Player player = (Player) clicker;
                        return Lang.lang(player, "extension.mute-and-ban.button.kick.display");
                    }
                }
                return null;
            }

            @Override
            public void click(CommandSender clicker, Message message) {
                if (clicker instanceof Player player) {
                    if (message.getSender().getSender() instanceof Player bePunished) {
                        if (clicker.hasPermission("dynamichat.mnb.kick")) {
                            if (player.getUniqueId() == bePunished.getUniqueId()) {
                                Lang.send(clicker, "extension.mute-and-ban.command.kick.notself");
                            } else {
                                if (MuteNBanManager.isNowBanned(bePunished).getFirst()) {
                                    Lang.send(clicker, "extension.mute-and-ban.command.ban.failed", player.getName());
                                } else {
                                    MNBGUIUtils.openReason(player, bePunished, 0, (punish, reason, timeLong) -> {
                                        message.getSender().kick(reason);
                                        Lang.send(clicker, "extension.mute-and-ban.command.kick.success", punish.getName());
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public String hover(CommandSender clicker, Message message) {
                Player player = (Player) clicker;
                return Lang.lang(player, "extension.mute-and-ban.button.kick.hover");
            }
        });
        ChatManager.getManager().registerButton(5, new MessageButton() {
            @Override
            public String display(CommandSender clicker, Message message) {
                if (ExtensionUtils.enabledMuteAndBanExtension()) {
                    if (clicker.hasPermission("dynamichat.mnb.ban")) {
                        Player player = (Player) clicker;
                        return Lang.lang(player, "extension.mute-and-ban.button.ban.display");
                    }
                }
                return null;
            }

            @Override
            public void click(CommandSender clicker, Message message) {
                if (clicker instanceof Player player) {
                    if (message.getSender().getSender() instanceof Player bePunished) {
                        if (clicker.hasPermission("dynamichat.mnb.ban")) {
                            if (player.getUniqueId() == bePunished.getUniqueId()) {
                                Lang.send(clicker, "extension.mute-and-ban.command.ban.notself");
                            } else {
                                if (MuteNBanManager.isNowBanned(bePunished).getFirst()) {
                                    Lang.send(clicker, "extension.mute-and-ban.command.ban.failed", player.getName());
                                } else {
                                    MNBGUIUtils.openSetTime(player, bePunished, 0, (punish, reason, timeLong) -> {
                                        if (timeLong == 0) {
                                            MuteNBanManager.addBanned(clicker.getName(), punish, null, reason);
                                            if (punish.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(punish);
                                                punish.kickPlayer(Lang.lang(punish, "extension.mute-and-ban.message.banned.forever", obj.getFourth(), obj.getSecond()));
                                            }
                                        } else {
                                            Time time = new TimeEntity(timeLong);
                                            MuteNBanManager.addBanned(clicker.getName(), punish, time.after(new Date()), reason);
                                            if (punish.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(punish);
                                                punish.kickPlayer(Lang.lang(punish, "extension.mute-and-ban.message.banned.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond()));
                                            }
                                        }
                                        Lang.send(clicker, "extension.mute-and-ban.command.ban.success", punish.getName());
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public String hover(CommandSender clicker, Message message) {
                Player player = (Player) clicker;
                return Lang.lang(player, "extension.mute-and-ban.button.ban.hover");
            }
        });
        EzCommand mainCommand = new EzCommand("dynamic-chat", "dynamichat.command.dynamic-chat", PermissionDefault.OP).executes((sender, helper) -> {
            Lang.send(sender, "command.gotohelp");
            /*if (sender instanceof Player) {
                try {
                    AnvilGUI gui = new AnvilGUI(DyChatPlugin.this, "§c§lDynamicChat");
                    //gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, (Clicker) (viewer, type, action) -> viewer.sendMessage("Type: " + type.name() + ", Action: " + action.name()));
                    gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, new Storage() {
                        @Override
                        public boolean isAllow(Player viewer, ItemStack cursorItem) {
                            return cursorItem.getType().isFuel();
                        }
                    });
                    gui.open((Player) sender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            */
            return 1;
        }).then(new EzCommand("help").executes((sender, helper) -> {
            Lang.send(sender, "command.main-help");
            return 1;
        })).then(new EzCommand("reload").executes((sender, helper) -> {
            ConfigUtils.load();
            DynamicChatManager.reload();
            Lang.send(sender, "command.main-reload-success");
            return 1;
        })).then(new EzCommand("test").requires(sender -> sender.getName().equalsIgnoreCase("DeeChael")).executes(((sender, helper) -> {
            try {
                AnvilGUI gui = new AnvilGUI(DyChatPlugin.this, "§c§lDynamicChat");
                //gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, (Clicker) (viewer, type, action) -> viewer.sendMessage("Type: " + type.name() + ", Action: " + action.name()));
                gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, (Image) (viewer, inventory) -> new ItemStack(Material.PAPER));
                gui.setItem(AnvilGUI.AnvilSlot.OUTPUT, (AnvilOutputImage) (viewer, inventory, result) -> {
                    ItemStack itemStack = new ItemStack(Material.DIAMOND);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        itemMeta.setDisplayName("§a§lAPPLY");
                        if (result != null) {
                            itemMeta.setLore(List.of("§r§aYou are renaming to §b" + result));
                        } else {
                            itemMeta.setLore(List.of("§r§aYou are renaming to §b"));
                        }
                        itemStack.setItemMeta(itemMeta);
                    }
                    return itemStack;
                });
                gui.open((Player) sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        })));
        mainCommand.addAliases("dynamicchat", "dynamichat", "dychat", "dchat", "dc", "dy-chat", "dee-chat", "deechat", "deechaelchat");
        EzCommandManager.register("dyanmichat", mainCommand);
        EzCommandManager.register("dynamichat", new EzCommand("channel", "dynamichat.command.channel", PermissionDefault.TRUE)
                .executes((sender, helper) -> {
                    Lang.send(sender, "command.channel-gotohelp");
                    return 1;
                })
                .then(new EzCommand("help").executes((sender, helper) -> {
                    Lang.send(sender, "command.channel-help");
                    return 1;
                }))
                .then(new EzCommand("switch").executes((sender, helper) -> {
                                    Lang.send(sender, "command.channel-gotohelp");
                                    return 1;
                                }).then(new EzArgument(BaseArguments.string(), "channel")
                                        .suggest((sender, suggestionsBuilder) -> ChatManager.getManager().getUser(sender).getAvailable().stream().map(Channel::getName).forEach(suggestionsBuilder::suggest))
                                        .executes((sender, helper) -> {
                                            String channelName = helper.getAsString("channel");
                                            Channel channel = ChatManager.getManager().getChannel(channelName);
                                            if (channel == null) {
                                                Lang.send(sender, "command.channel-notexists");
                                            } else {
                                                UserEntity user = (UserEntity) ChatManager.getManager().getUser(sender);
                                                if (!user.getAvailable().contains((ChannelEntity) channel)) {
                                                    Lang.send(sender, "command.channel-notavailable", channel.getDisplayName());
                                                } else {
                                                    user.moveTo(channel);
                                                    Lang.send(sender, "command.channel-switch-success", channel.getDisplayName());
                                                }
                                            }
                                            return 1;
                                        }))
                                .then(new EzCommand("global").executes(((sender, helper) -> {
                                    UserEntity user = (UserEntity) ChatManager.getManager().getUser(sender);
                                    user.moveTo(ChatManager.getManager().getGlobal());
                                    Lang.send(sender, "command.channel-switch-success", ConfigUtils.globalChannelDisplayName());
                                    return 1;
                                })))
                )
                .then(new EzCommand("send").executes((sender, helper) -> {
                    Lang.send(sender, "command.channel-gotohelp");
                    return 1;
                }).then(new EzArgument(BaseArguments.string(), "channel")
                        .suggest((sender, suggestionsBuilder) -> ChatManager.getManager().getUser(sender).getAvailable().stream().map(Channel::getName).forEach(suggestionsBuilder::suggest))
                        .executes((sender, helper) -> {
                            String channelName = helper.getAsString("channel");
                            Channel channel = ChatManager.getManager().getChannel(channelName);
                            if (channel == null) {
                                Lang.send(sender, "command.channel-notexists");
                            } else {
                                UserEntity user = (UserEntity) ChatManager.getManager().getUser(sender);
                                if (!user.getAvailable().contains((ChannelEntity) channel)) {
                                    Lang.send(sender, "command.channel-notavailable", channel.getDisplayName());
                                } else {
                                    Channel current = user.getCurrent();
                                    user.moveTo(channel);
                                    user.chat("");
                                    user.moveTo(current);
                                    Lang.send(sender, "command.channel-switch-success", channel.getDisplayName());
                                }
                            }
                            return 1;
                        }).then(new EzArgument(ArgumentChat.argumentType(), "message").executes((sender, helper) -> {
                            String channelName = helper.getAsString("channel");
                            Channel channel = ChatManager.getManager().getChannel(channelName);
                            if (channel == null) {
                                Lang.send(sender, "command.channel-notexists");
                            } else {
                                UserEntity user = (UserEntity) ChatManager.getManager().getUser(sender);
                                if (!user.getAvailable().contains((ChannelEntity) channel)) {
                                    Lang.send(sender, "command.channel-notavailable", channel.getDisplayName());
                                } else {
                                    Channel current = user.getCurrent();
                                    user.moveTo(channel);
                                    user.chat(helper.getAsChatMessage("message"));
                                    user.moveTo(current);
                                }
                            }
                            return 1;
                        }))))
        );
        EzCommandManager.register("dynamichat", new EzCommand("chat-color", "dynamichat.command.chat-color", PermissionDefault.TRUE)
                .requires(object -> ConfigUtils.chatColorChangeable())
                .executes((sender, helper) -> {
                    Lang.send(sender, "command.chatcolor-gotohelp");
                    return 1;
                })
                .then(new EzCommand("help").executes((sender, helper) -> {
                    Lang.send(sender, "command.chatcolor-help");
                    return 1;
                }))
                .then(new EzCommand("reset").executes(((sender, helper) -> {
                    if (sender instanceof Player player) {
                        PlayerUtils.reset(player);
                        Lang.send(sender, "command.chatcolor-reset-success");
                    } else {
                        Lang.send(sender, "command.mustbeplayer");
                    }
                    return 1;
                })))
                .then(new EzCommand("set").executes((sender, helper) -> {
                            Lang.send(sender, "command.chatcolor-gotohelp");
                            return 1;
                        }).then(new EzCommand("color").then(new EzArgument(BaseArguments.string(), "color").executes((sender, helper) -> {
                            if (sender instanceof Player player) {
                                String colorString = helper.getAsString("color");
                                if (colorString.length() == 1) {
                                    if (!ChatColor.ALL_CODES.contains(colorString)) {
                                        Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                    } else {
                                        PlayerUtils.setColor(player, List.of(colorString));
                                        Lang.send(sender, "command.chatcolor-set-success");
                                    }
                                } else if (colorString.length() == 6) {
                                    if (!Pattern.matches("[a-fA-F0-9]{6}", colorString)) {
                                        Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                    } else {
                                        PlayerUtils.setColor(player, List.of(colorString));
                                        Lang.send(sender, "command.chatcolor-set-success");
                                    }
                                } else if (colorString.length() == 7) {
                                    if (!Pattern.matches("#([a-fA-F0-9]{6})", colorString)) {
                                        Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                    } else {
                                        PlayerUtils.setColor(player, List.of(colorString.substring(1)));
                                        Lang.send(sender, "command.chatcolor-set-success");
                                    }
                                } else {
                                    Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                }
                            } else {
                                Lang.send(sender, "command.mustbeplayer");
                            }
                            return 1;
                        })))
                        .then(new EzCommand("gradient").then(new EzArgument(BaseArguments.string(), "from").then(new EzArgument(BaseArguments.string(), "to").executes((sender, helper) -> {
                            if (sender instanceof Player player) {
                                String from = helper.getAsString("from");
                                String to = helper.getAsString("to");
                                if (from.length() == 1) {
                                    if (!ChatColor.ALL_CODES.contains(from)) {
                                        Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                    } else {
                                        if (to.length() == 1) {
                                            if (!ChatColor.ALL_CODES.contains(to)) {
                                                Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                            } else {
                                                PlayerUtils.setColor(player, List.of(from, to));
                                                Lang.send(sender, "command.chatcolor-set-success");
                                            }
                                        } else if (to.length() == 6) {
                                            if (!Pattern.matches("[a-fA-F\\d]{6}", to)) {
                                                Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                            } else {
                                                PlayerUtils.setColor(player, List.of(from, to));
                                                Lang.send(sender, "command.chatcolor-set-success");
                                            }
                                        } else if (to.length() == 7) {
                                            if (!Pattern.matches("#([a-fA-F\\d]{6})", to)) {
                                                Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                            } else {
                                                PlayerUtils.setColor(player, List.of(from, to.substring(1)));
                                                Lang.send(sender, "command.chatcolor-set-success");
                                            }
                                        } else {
                                            Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                        }
                                    }
                                } else if (from.length() == 6) {
                                    if (!Pattern.matches("[a-fA-F\\d]{6}", from)) {
                                        Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                    } else {
                                        net.deechael.dynamichat.util.ChatColor color = new net.deechael.dynamichat.util.ChatColor();
                                        if (to.length() == 1) {
                                            if (!ChatColor.ALL_CODES.contains(to)) {
                                                Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                            } else {
                                                PlayerUtils.setColor(player, List.of(from, to));
                                                Lang.send(sender, "command.chatcolor-set-success");
                                            }
                                        } else if (to.length() == 6) {
                                            if (!Pattern.matches("[a-fA-F\\d]{6}", to)) {
                                                Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                            } else {
                                                PlayerUtils.setColor(player, List.of(from, to));
                                                Lang.send(sender, "command.chatcolor-set-success");
                                            }
                                        } else if (to.length() == 7) {
                                            if (!Pattern.matches("#([a-fA-F\\d]{6})", to)) {
                                                Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                            } else {
                                                PlayerUtils.setColor(player, List.of(from, to.substring(1)));
                                                Lang.send(sender, "command.chatcolor-set-success");
                                            }
                                        } else {
                                            Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                        }
                                    }
                                } else if (from.length() == 7) {
                                    if (!Pattern.matches("#([a-fA-F\\d]{6})", from)) {
                                        Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                    } else {
                                        net.deechael.dynamichat.util.ChatColor color = new net.deechael.dynamichat.util.ChatColor();
                                        if (to.length() == 1) {
                                            if (!ChatColor.ALL_CODES.contains(to)) {
                                                Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                            } else {
                                                PlayerUtils.setColor(player, List.of(from.substring(1), to));
                                                Lang.send(sender, "command.chatcolor-set-success");
                                            }
                                        } else if (to.length() == 6) {
                                            if (!Pattern.matches("[a-fA-F\\d]{6}", to)) {
                                                Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                            } else {
                                                PlayerUtils.setColor(player, List.of(from.substring(1), to));
                                                Lang.send(sender, "command.chatcolor-set-success");
                                            }
                                        } else if (to.length() == 7) {
                                            if (!Pattern.matches("#([a-fA-F\\d]{6})", to)) {
                                                Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                            } else {
                                                PlayerUtils.setColor(player, List.of(from.substring(1), to.substring(1)));
                                                Lang.send(sender, "command.chatcolor-set-success");
                                            }
                                        } else {
                                            Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                        }
                                    }
                                } else {
                                    Lang.send(sender, "command.chatcolor-set-unknowncolor");
                                }
                            } else {
                                Lang.send(sender, "command.mustbeplayer");
                            }
                            return 1;
                        })))))
        );
        EzCommandManager.register("dynamichat-cache", new EzCommand("chat-cache-clicker")
                .then(new EzCommand("message").then(new EzArgument(BaseArguments.string(), "cache_id")
                        .executes(((sender, helper) -> {
                            String cache_id = helper.getAsString("cache_id");
                            Message msg = DynamicChatManager.getChatCache(cache_id);
                            if (msg == null) {
                                Lang.send(sender, "command.message.cannotlocate");
                            } else {
                                ComponentBuilder componentBuilder = new ComponentBuilder();
                                Map<Integer, ? extends MessageButton> buttons = ChatManager.getManager().getButtons();
                                int maxIndex = ChatManager.getManager().getButtonMaxIndex();
                                if (maxIndex != -1) {
                                    for (int i = 0; i < maxIndex + 1; i++) {
                                        if (!buttons.containsKey(i))
                                            continue;
                                        MessageButton button = buttons.get(i);
                                        ComponentBuilder buttonBuilder;
                                        String display = button.display(sender, msg);
                                        if (display == null)
                                            continue;
                                        if (!display.isBlank() && !display.isEmpty()) {
                                            buttonBuilder = new ComponentBuilder(display);
                                        } else {
                                            buttonBuilder = new ComponentBuilder("Button" + (i + 1));
                                        }
                                        String hover = button.hover(sender, msg);
                                        if (hover != null) {
                                            buttonBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
                                        }
                                        if (button instanceof CopyMessageButton copyButton) {
                                            buttonBuilder.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyButton.value(sender, msg)));
                                        } else {
                                            buttonBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat-cache-clicker click " + cache_id + " " + i));
                                        }
                                        componentBuilder.append(buttonBuilder.create());
                                        if (i < buttons.size() - 1) {
                                            componentBuilder.append(" ");
                                        }
                                    }
                                    sender.spigot().sendMessage(componentBuilder.create());
                                }
                            }
                            return 1;
                        }))))
                .then(new EzCommand("click").then(new EzArgument(BaseArguments.string(), "cache_id")
                        .then(new EzArgument(BaseArguments.integer(), "index")
                                .executes(((sender, helper) -> {
                                    String cache_id = helper.getAsString("cache_id");
                                    int index = helper.getAsInteger("index");
                                    Message msg = DynamicChatManager.getChatCache(cache_id);
                                    if (msg == null) {
                                        Lang.send(sender, "command.message.cannotlocate");
                                    } else {
                                        try {
                                            ChatManager.getManager().getButtons().get(index).click(sender, msg);
                                        } catch (IndexOutOfBoundsException e) {
                                            Lang.send(sender, "command.message.unknownbutton");
                                        }
                                    }
                                    return 1;
                                }))
                        )
                ))
        );


        // Extension Command
        EzCommandManager.register("dynamichat", new EzCommand("kick", "dynamichat.mnb.kick", PermissionDefault.OP)
                .then(new EzArgument(ArgumentPlayer.argumentType(), "target")
                        .then(new EzArgument(ArgumentChat.argumentType(), "reason")
                                .executes((sender, helper) -> {
                                    List<Player> players = helper.getAsPlayers("target");
                                    String reason = helper.getAsChatMessage("reason");
                                    boolean isPlayer = sender instanceof Player;
                                    Player senderPlayer = isPlayer ? (Player) sender : null;
                                    for (Player player : players) {
                                        if (isPlayer) {
                                            if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                Lang.send(sender, "extension.mute-and-ban.command.kick.notself");
                                                continue;
                                            }
                                        }
                                        ChatManager.getManager().getUser(player).kick(reason);
                                        Lang.send(sender, "extension.mute-and-ban.command.kick.success", player.getName());
                                    }
                                    return 1;
                                })
                        )
                        .executes((sender, helper) -> {
                            List<Player> players = helper.getAsPlayers("target");
                            boolean isPlayer = sender instanceof Player;
                            Player senderPlayer = isPlayer ? (Player) sender : null;
                            for (Player player : players) {
                                if (isPlayer) {
                                    if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                        Lang.send(sender, "extension.mute-and-ban.command.kick.notself");
                                        continue;
                                    }
                                }
                                ChatManager.getManager().getUser(player).kick();
                                Lang.send(sender, "extension.mute-and-ban.command.kick.success", player.getName());
                            }
                            return 1;
                        })
                )
        );
        EzCommandManager.register("dynamichat", new EzCommand("ban", "dynamichat.mnb.ban", PermissionDefault.OP)
                .then(new EzArgument(ArgumentOfflinePlayer.argumentType(), "target")
                        .then(new EzArgument(BaseArguments.string(), "time")
                                .then(new EzArgument(ArgumentChat.argumentType(), "reason")
                                        .executes((sender, helper) -> {
                                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                                Lang.send(sender, "command.extensionrequired");
                                                return 1;
                                            }
                                            List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                                            String reason = helper.getAsChatMessage("reason");
                                            try {
                                                Time time = TimeEntity.of(helper.getAsString("time"));
                                                boolean isPlayer = sender instanceof Player;
                                                Player senderPlayer = isPlayer ? (Player) sender : null;
                                                for (OfflinePlayer player : players) {
                                                    if (isPlayer) {
                                                        if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                            Lang.send(sender, "extension.mute-and-ban.command.ban.notself");
                                                            continue;
                                                        }
                                                    }
                                                    if (MuteNBanManager.isNowBanned(player).getFirst()) {
                                                        Lang.send(sender, "extension.mute-and-ban.command.ban.failed", player.getName());
                                                    } else {
                                                        MuteNBanManager.addBanned(sender.getName(), player, time.after(new Date()), reason);
                                                        if (player.isOnline()) {
                                                            FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(player);
                                                            ((Player) player).kickPlayer(Lang.lang(((Player) player), "extension.mute-and-ban.message.banned.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond()));
                                                        }
                                                        Lang.send(sender, "extension.mute-and-ban.command.ban.success", player.getName());
                                                    }
                                                }
                                            } catch (TimeParseException e) {
                                                Lang.send(sender, "extension.mute-and-ban.command.ban.time-format");
                                            }
                                            return 1;
                                        }))
                                .executes((sender, helper) -> {
                                    if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                        Lang.send(sender, "command.extensionrequired");
                                        return 1;
                                    }
                                    List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                                    try {
                                        Time time = TimeEntity.of(helper.getAsString("time"));
                                        boolean isPlayer = sender instanceof Player;
                                        Player senderPlayer = isPlayer ? (Player) sender : null;
                                        for (OfflinePlayer player : players) {
                                            if (isPlayer) {
                                                if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                    Lang.send(sender, "extension.mute-and-ban.command.ban.notself");
                                                    continue;
                                                }
                                            }
                                            if (MuteNBanManager.isNowBanned(player).getFirst()) {
                                                Lang.send(sender, "extension.mute-and-ban.command.ban.failed", player.getName());
                                            } else {
                                                MuteNBanManager.addBanned(sender.getName(), player, time.after(new Date()), Lang.lang(sender, "extension.mute-and-ban.default.no-reason"));
                                                if (player.isOnline()) {
                                                    FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(player);
                                                    ((Player) player).kickPlayer(Lang.lang(((Player) player), "extension.mute-and-ban.message.banned.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond()));
                                                }
                                                Lang.send(sender, "extension.mute-and-ban.command.ban.success", player.getName());
                                            }
                                        }
                                    } catch (TimeParseException e) {
                                        Lang.send(sender, "extension.mute-and-ban.command.ban.time-format");
                                    }
                                    return 1;
                                }))
                        .then(new EzCommand("forever")
                                .then(new EzArgument(ArgumentChat.argumentType(), "reason")
                                        .executes((sender, helper) -> {
                                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                                Lang.send(sender, "command.extensionrequired");
                                                return 1;
                                            }
                                            List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                                            String reason = helper.getAsChatMessage("reason");
                                            boolean isPlayer = sender instanceof Player;
                                            Player senderPlayer = isPlayer ? (Player) sender : null;
                                            for (OfflinePlayer player : players) {
                                                if (isPlayer) {
                                                    if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                        Lang.send(sender, "extension.mute-and-ban.command.ban.notself");
                                                        continue;
                                                    }
                                                }
                                                if (MuteNBanManager.isNowBanned(player).getFirst()) {
                                                    Lang.send(sender, "extension.mute-and-ban.command.ban.failed", player.getName());
                                                } else {
                                                    MuteNBanManager.addBanned(sender.getName(), player, null, reason);
                                                    if (player.isOnline()) {
                                                        FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(player);
                                                        Date date = obj.getThird();
                                                        if (date != null) {
                                                            Time time = ChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
                                                            ((Player) player).kickPlayer(Lang.lang(((Player) player), "extension.mute-and-ban.message.banned.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond()));
                                                        } else {
                                                            ((Player) player).kickPlayer(Lang.lang(((Player) player), "extension.mute-and-ban.message.banned.forever", obj.getFourth(), obj.getSecond()));
                                                        }
                                                    }
                                                    Lang.send(sender, "extension.mute-and-ban.command.ban.success", player.getName());
                                                }
                                            }
                                            return 1;
                                        }))
                                .executes((sender, helper) -> {
                                    if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                        Lang.send(sender, "command.extensionrequired");
                                        return 1;
                                    }
                                    List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                                    boolean isPlayer = sender instanceof Player;
                                    Player senderPlayer = isPlayer ? (Player) sender : null;
                                    for (OfflinePlayer player : players) {
                                        if (isPlayer) {
                                            if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                Lang.send(sender, "extension.mute-and-ban.command.ban.notself");
                                                continue;
                                            }
                                        }
                                        if (MuteNBanManager.isNowBanned(player).getFirst()) {
                                            Lang.send(sender, "extension.mute-and-ban.command.ban.failed", player.getName());
                                        } else {
                                            MuteNBanManager.addBanned(sender.getName(), player, null, Lang.lang(sender, "extension.mute-and-ban.default.no-reason"));
                                            if (player.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(player);
                                                Date date = obj.getThird();
                                                if (date != null) {
                                                    Time time = ChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
                                                    ((Player) player).kickPlayer(Lang.lang(((Player) player), "extension.mute-and-ban.message.banned.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond()));
                                                } else {
                                                    ((Player) player).kickPlayer(Lang.lang(((Player) player), "extension.mute-and-ban.message.banned.forever", obj.getFourth(), obj.getSecond()));
                                                }
                                            }
                                            Lang.send(sender, "extension.mute-and-ban.command.ban.success", player.getName());
                                        }
                                    }
                                    return 1;
                                }))
                        .executes((sender, helper) -> {
                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                Lang.send(sender, "command.extensionrequired");
                                return 1;
                            }
                            List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                            boolean isPlayer = sender instanceof Player;
                            Player senderPlayer = isPlayer ? (Player) sender : null;
                            for (OfflinePlayer player : players) {
                                if (isPlayer) {
                                    if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                        Lang.send(sender, "extension.mute-and-ban.command.ban.notself");
                                        continue;
                                    }
                                }
                                if (MuteNBanManager.isNowBanned(player).getFirst()) {
                                    Lang.send(sender, "extension.mute-and-ban.command.ban.failed", player.getName());
                                } else {
                                    MuteNBanManager.addBanned(sender.getName(), player, null, Lang.lang(sender, "extension.mute-and-ban.default.no-reason"));
                                    if (player.isOnline()) {
                                        FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(player);
                                        Date date = obj.getThird();
                                        if (date != null) {
                                            Time time = ChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
                                            ((Player) player).kickPlayer(Lang.lang(((Player) player), "extension.mute-and-ban.message.banned.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond()));
                                        } else {
                                            ((Player) player).kickPlayer(Lang.lang(((Player) player), "extension.mute-and-ban.message.banned.forever", obj.getFourth(), obj.getSecond()));
                                        }
                                    }
                                    Lang.send(sender, "extension.mute-and-ban.command.ban.success", player.getName());
                                }
                            }
                            return 1;
                        })
                )
        );
        EzCommandManager.register("dynamichat", new EzCommand("unban", "dynamichat.mnb.unban", PermissionDefault.OP)
                .then(new EzArgument(ArgumentOfflinePlayer.argumentType(), "target")
                        .executes((sender, helper) -> {
                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                Lang.send(sender, "command.extensionrequired");
                                return 1;
                            }
                            List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                            for (OfflinePlayer player : players) {
                                FoObj<Boolean, String, Date, String> bannedInformation = MuteNBanManager.isNowBanned(player);
                                if (!bannedInformation.getFirst()) {
                                    Lang.send(sender, "extension.mute-and-ban.command.unban.failed", player.getName());
                                } else {
                                    MuteNBanManager.unbanned(player, bannedInformation.getFourth());
                                    Lang.send(sender, "extension.mute-and-ban.command.unban.success", player.getName());
                                }
                            }
                            return 1;
                        })
                )
        );
        EzCommandManager.register("dynamichat", new EzCommand("mute", "dynamichat.mnb.mute", PermissionDefault.OP)
                .then(new EzArgument(ArgumentOfflinePlayer.argumentType(), "target")
                        .then(new EzArgument(BaseArguments.string(), "time")
                                .then(new EzArgument(ArgumentChat.argumentType(), "reason")
                                        .executes((sender, helper) -> {
                                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                                Lang.send(sender, "command.extensionrequired");
                                                return 1;
                                            }
                                            List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                                            String reason = helper.getAsChatMessage("reason");
                                            try {
                                                Time time = TimeEntity.of(helper.getAsString("time"));
                                                boolean isPlayer = sender instanceof Player;
                                                Player senderPlayer = isPlayer ? (Player) sender : null;
                                                for (OfflinePlayer player : players) {
                                                    if (isPlayer) {
                                                        if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                            Lang.send(sender, "extension.mute-and-ban.command.mute.notself");
                                                            continue;
                                                        }
                                                    }
                                                    if (MuteNBanManager.isNowMuted(player).getFirst()) {
                                                        Lang.send(sender, "extension.mute-and-ban.command.mute.failed", player.getName());
                                                    } else {
                                                        MuteNBanManager.addMuted(sender.getName(), player, time.after(new Date()), reason);
                                                        if (player.isOnline()) {
                                                            FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(player);
                                                            Lang.send(((Player) player), "extension.mute-and-ban.message.muted.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond());
                                                        }
                                                        Lang.send(sender, "extension.mute-and-ban.command.mute.success", player.getName());
                                                    }
                                                }
                                            } catch (TimeParseException e) {
                                                Lang.send(sender, "extension.mute-and-ban.command.mute.time-format");
                                            }
                                            return 1;
                                        }))
                                .executes((sender, helper) -> {
                                    if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                        Lang.send(sender, "command.extensionrequired");
                                        return 1;
                                    }
                                    List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                                    try {
                                        Time time = TimeEntity.of(helper.getAsString("time"));
                                        boolean isPlayer = sender instanceof Player;
                                        Player senderPlayer = isPlayer ? (Player) sender : null;
                                        for (OfflinePlayer player : players) {
                                            if (isPlayer) {
                                                if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                    Lang.send(sender, "extension.mute-and-ban.command.mute.notself");
                                                    continue;
                                                }
                                            }
                                            if (MuteNBanManager.isNowMuted(player).getFirst()) {
                                                Lang.send(sender, "extension.mute-and-ban.command.mute.failed", player.getName());
                                            } else {
                                                MuteNBanManager.addMuted(sender.getName(), player, time.after(new Date()), Lang.lang(sender, "extension.mute-and-ban.default.no-reason"));
                                                if (player.isOnline()) {
                                                    FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(player);
                                                    Lang.send(((Player) player), "extension.mute-and-ban.message.muted.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond());
                                                }
                                                Lang.send(sender, "extension.mute-and-ban.command.mute.success", player.getName());
                                            }
                                        }
                                    } catch (TimeParseException e) {
                                        Lang.send(sender, "extension.mute-and-ban.command.mute.time-format");
                                    }
                                    return 1;
                                }))
                        .then(new EzCommand("forever")
                                .then(new EzArgument(ArgumentChat.argumentType(), "reason")
                                        .executes((sender, helper) -> {
                                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                                Lang.send(sender, "command.extensionrequired");
                                                return 1;
                                            }
                                            List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                                            String reason = helper.getAsChatMessage("reason");
                                            boolean isPlayer = sender instanceof Player;
                                            Player senderPlayer = isPlayer ? (Player) sender : null;
                                            for (OfflinePlayer player : players) {
                                                if (isPlayer) {
                                                    if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                        Lang.send(sender, "extension.mute-and-ban.command.mute.notself");
                                                        continue;
                                                    }
                                                }
                                                if (MuteNBanManager.isNowMuted(player).getFirst()) {
                                                    Lang.send(sender, "extension.mute-and-ban.command.mute.failed", player.getName());
                                                } else {
                                                    MuteNBanManager.addMuted(sender.getName(), player, null, reason);
                                                    if (player.isOnline()) {
                                                        FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(player);
                                                        Date date = obj.getThird();
                                                        if (date != null) {
                                                            Time time = ChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
                                                            Lang.send(((Player) player), "extension.mute-and-ban.message.muted.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond());
                                                        } else {
                                                            Lang.send(((Player) player), "extension.mute-and-ban.message.muted.forever", obj.getFourth(), obj.getSecond());
                                                        }
                                                    }
                                                    Lang.send(sender, "extension.mute-and-ban.command.mute.success", player.getName());
                                                }
                                            }
                                            return 1;
                                        }))
                                .executes((sender, helper) -> {
                                    if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                        Lang.send(sender, "command.extensionrequired");
                                        return 1;
                                    }
                                    List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                                    boolean isPlayer = sender instanceof Player;
                                    Player senderPlayer = isPlayer ? (Player) sender : null;
                                    for (OfflinePlayer player : players) {
                                        if (isPlayer) {
                                            if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                Lang.send(sender, "extension.mute-and-ban.command.mute.notself");
                                                continue;
                                            }
                                        }
                                        if (MuteNBanManager.isNowMuted(player).getFirst()) {
                                            Lang.send(sender, "extension.mute-and-ban.command.mute.failed", player.getName());
                                        } else {
                                            MuteNBanManager.addMuted(sender.getName(), player, null, Lang.lang(sender, "extension.mute-and-ban.default.no-reason"));
                                            if (player.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(player);
                                                Date date = obj.getThird();
                                                if (date != null) {
                                                    Time time = ChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
                                                    Lang.send(((Player) player), "extension.mute-and-ban.message.muted.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond());
                                                } else {
                                                    Lang.send(((Player) player), "extension.mute-and-ban.message.muted.forever", obj.getFourth(), obj.getSecond());
                                                }
                                            }
                                            Lang.send(sender, "extension.mute-and-ban.command.mute.success", player.getName());
                                        }
                                    }
                                    return 1;
                                }))
                        .executes((sender, helper) -> {
                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                Lang.send(sender, "command.extensionrequired");
                                return 1;
                            }
                            List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                            boolean isPlayer = sender instanceof Player;
                            Player senderPlayer = isPlayer ? (Player) sender : null;
                            for (OfflinePlayer player : players) {
                                if (isPlayer) {
                                    if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                        Lang.send(sender, "extension.mute-and-ban.command.mute.notself");
                                        continue;
                                    }
                                }
                                if (MuteNBanManager.isNowBanned(player).getFirst()) {
                                    Lang.send(sender, "extension.mute-and-ban.command.mute.failed", player.getName());
                                } else {
                                    MuteNBanManager.addBanned(sender.getName(), player, null, Lang.lang(sender, "extension.mute-and-ban.default.no-reason"));
                                    if (player.isOnline()) {
                                        FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(player);
                                        Date date = obj.getThird();
                                        if (date != null) {
                                            Time time = ChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
                                            Lang.send(((Player) player), "extension.mute-and-ban.message.muted.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond());
                                        } else {
                                            Lang.send(((Player) player), "extension.mute-and-ban.message.muted.forever", obj.getFourth(), obj.getSecond());
                                        }
                                    }
                                    Lang.send(sender, "extension.mute-and-ban.command.mute.success", player.getName());
                                }
                            }
                            return 1;
                        })
                )
        );
        EzCommandManager.register("dynamichat", new EzCommand("unmute", "dynamichat.mnb.unmute", PermissionDefault.OP)
                .then(new EzArgument(ArgumentOfflinePlayer.argumentType(), "target")
                        .executes((sender, helper) -> {
                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                Lang.send(sender, "command.extensionrequired");
                                return 1;
                            }
                            List<OfflinePlayer> players = helper.getAsOfflinePlayer("target");
                            for (OfflinePlayer player : players) {
                                FoObj<Boolean, String, Date, String> mutedInformation = MuteNBanManager.isNowMuted(player);
                                if (!mutedInformation.getFirst()) {
                                    Lang.send(sender, "extension.mute-and-ban.command.unmute.failed", player.getName());
                                } else {
                                    MuteNBanManager.unmuted(player, mutedInformation.getFourth());
                                    Lang.send(sender, "extension.mute-and-ban.command.unmute.success", player.getName());
                                    if (player.isOnline()) {
                                        Lang.send(((Player) player), "extension.mute-and-ban.message.unmuted");
                                    }
                                }
                            }
                            return 1;
                        })
                )
        );
        NMSCommandKiller.kill(this);
    }

    @Override
    public void onDisable() {
    }

}
