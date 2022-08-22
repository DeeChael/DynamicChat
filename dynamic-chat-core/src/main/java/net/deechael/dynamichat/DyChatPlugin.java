package net.deechael.dynamichat;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.deechael.dynamichat.api.*;
import net.deechael.dynamichat.command.EzArgument;
import net.deechael.dynamichat.command.EzCommand;
import net.deechael.dynamichat.command.EzCommandManager;
import net.deechael.dynamichat.command.argument.ArgumentChat;
import net.deechael.dynamichat.command.argument.ArgumentOfflinePlayer;
import net.deechael.dynamichat.command.argument.ArgumentPlayer;
import net.deechael.dynamichat.command.argument.BaseArguments;
import net.deechael.dynamichat.entity.*;
import net.deechael.dynamichat.exception.TimeParseException;
import net.deechael.dynamichat.gui.AnvilGUI;
import net.deechael.dynamichat.gui.AnvilOutputImage;
import net.deechael.dynamichat.gui.Image;
import net.deechael.dynamichat.api.BanIPPunishment;
import net.deechael.dynamichat.api.Time;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.proxy.packets.PacketRequestSettings;
import net.deechael.dynamichat.temp.DynamicChatListener;
import net.deechael.dynamichat.temp.NMSCommandKiller;
import net.deechael.dynamichat.temp.ProxyConnection;
import net.deechael.dynamichat.temp.RecallSolver;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class DyChatPlugin extends JavaPlugin {

    public static DyChatPlugin getInstance() {
        return JavaPlugin.getPlugin(DyChatPlugin.class);
    }

    @Override
    public void onLoad() {
        ConfigUtils.load();
        BukkitChatManager.setManager(new DynamicBukkitChatManager());
        BukkitChatManager.getManager().getBukkitUser(Bukkit.getConsoleSender());
        if (ConfigUtils.isProxyMode())
            return;
        DynamicBukkitChatManager.reload();
        DynamicBanIPManager.reload();
    }

    @Override
    public void onEnable() {
        new AnvilGUI(this).drop();
        new DynamicChatPlaceholder().register();
        Bukkit.getPluginManager().registerEvents(EzCommandManager.INSTANCE, this);
        Bukkit.getPluginManager().registerEvents(new DynamicChatListener(), this);
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        manager.addPacketListener(new RecallSolver());
        if (ConfigUtils.isProxyMode()) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "dynamichat");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "dynamichat", new ProxyConnection());
        }
        ProxyConnection.sendPacket(new PacketRequestSettings());
        BukkitChatManager.getManager().registerButton(0, new CopyMessageButton() {
            @Override
            public String value(User clicker, Message message) {
                return message.getContent();
            }

            @Override
            public String display(User clicker, Message message) {
                return Lang.lang(((BukkitUser) clicker).getSender(), "message.button.copy.display");
            }

            @Override
            public String hover(User clicker, Message message) {
                return Lang.lang(((BukkitUser) clicker).getSender(), "message.button.copy.hover");
            }

        });
        BukkitChatManager.getManager().registerButton(3, new MessageButton() {
            @Override
            public String display(User clicker, Message message) {
                if (ExtensionUtils.enabledMuteAndBanExtension()) {
                    if (((BukkitUser) clicker).getSender().hasPermission("dynamichat.mnb.mute")) {
                        Player player = (Player) ((BukkitUser) clicker).getSender();
                        return Lang.lang(player, "extension.mute-and-ban.button.mute.display");
                    }
                }
                return null;
            }

            @Override
            public void click(User clicker, Message message) {
                if (((BukkitUser) clicker).getSender() instanceof Player player) {
                    if (((BukkitUser) message.getSender()).getSender() instanceof Player bePunished) {
                        if (player.hasPermission("dynamichat.mnb.mute")) {
                            if (player.getUniqueId() == bePunished.getUniqueId()) {
                                Lang.send(player, "extension.mute-and-ban.command.mute.notself");
                            } else {
                                if (MuteNBanManager.isNowMuted(bePunished).getFirst()) {
                                    Lang.send(player, "extension.mute-and-ban.command.mute.failed", player.getName());
                                } else {
                                    MNBGUIUtils.openSetTime(player, bePunished, 0, (punish, reason, timeLong) -> {
                                        if (timeLong == 0) {
                                            MuteNBanManager.addMuted(player.getName(), punish, null, reason);
                                            if (punish.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(punish);
                                                Lang.send(punish, "extension.mute-and-ban.message.muted.forever", obj.getFourth(), obj.getSecond());
                                            }
                                        } else {
                                            Time time = new TimeEntity(timeLong);
                                            MuteNBanManager.addMuted(player.getName(), punish, time.after(new Date()), reason);
                                            if (punish.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowMuted(punish);
                                                Lang.send(punish, "extension.mute-and-ban.message.muted.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond());
                                            }
                                        }
                                        Lang.send(player, "extension.mute-and-ban.command.mute.success", punish.getName());
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public String hover(User clicker, Message message) {
                Player player = (Player) ((BukkitUser) clicker).getSender();
                return Lang.lang(player, "extension.mute-and-ban.button.mute.hover");
            }
        });
        BukkitChatManager.getManager().registerButton(4, new MessageButton() {
            @Override
            public String display(User clicker, Message message) {
                if (ExtensionUtils.enabledMuteAndBanExtension()) {
                    if (((BukkitUser) clicker).getSender().hasPermission("dynamichat.mnb.kick")) {
                        Player player = (Player) ((BukkitUser) clicker).getSender();
                        return Lang.lang(player, "extension.mute-and-ban.button.kick.display");
                    }
                }
                return null;
            }

            @Override
            public void click(User clicker, Message message) {
                if (((BukkitUser) clicker).getSender() instanceof Player player) {
                    if (((BukkitUser) message.getSender()).getSender() instanceof Player bePunished) {
                        if (player.hasPermission("dynamichat.mnb.kick")) {
                            if (player.getUniqueId() == bePunished.getUniqueId()) {
                                Lang.send(player, "extension.mute-and-ban.command.kick.notself");
                            } else {
                                if (MuteNBanManager.isNowBanned(bePunished).getFirst()) {
                                    Lang.send(player, "extension.mute-and-ban.command.ban.failed", player.getName());
                                } else {
                                    MNBGUIUtils.openReason(player, bePunished, 0, (punish, reason, timeLong) -> {
                                        message.getSender().kick(reason);
                                        Lang.send(player, "extension.mute-and-ban.command.kick.success", punish.getName());
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public String hover(User clicker, Message message) {
                Player player = (Player) ((BukkitUser) clicker).getSender();
                return Lang.lang(player, "extension.mute-and-ban.button.kick.hover");
            }
        });
        BukkitChatManager.getManager().registerButton(5, new MessageButton() {
            @Override
            public String display(User clicker, Message message) {
                if (ExtensionUtils.enabledMuteAndBanExtension()) {
                    if (((BukkitUser) clicker).getSender().hasPermission("dynamichat.mnb.ban")) {
                        Player player = (Player) ((BukkitUser) clicker).getSender();
                        return Lang.lang(player, "extension.mute-and-ban.button.ban.display");
                    }
                }
                return null;
            }

            @Override
            public void click(User clicker, Message message) {
                if (((BukkitUser) clicker).getSender() instanceof Player player) {
                    if (((BukkitUser) message.getSender()).getSender() instanceof Player bePunished) {
                        if (player.hasPermission("dynamichat.mnb.ban")) {
                            if (player.getUniqueId() == bePunished.getUniqueId()) {
                                Lang.send(player, "extension.mute-and-ban.command.ban.notself");
                            } else {
                                if (MuteNBanManager.isNowBanned(bePunished).getFirst()) {
                                    Lang.send(player, "extension.mute-and-ban.command.ban.failed", player.getName());
                                } else {
                                    MNBGUIUtils.openSetTime(player, bePunished, 0, (punish, reason, timeLong) -> {
                                        if (timeLong == 0) {
                                            MuteNBanManager.addBanned(player.getName(), punish, null, reason);
                                            if (punish.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(punish);
                                                punish.kickPlayer(Lang.lang(punish, "extension.mute-and-ban.message.banned.forever", obj.getFourth(), obj.getSecond()));
                                            }
                                        } else {
                                            Time time = new TimeEntity(timeLong);
                                            MuteNBanManager.addBanned(player.getName(), punish, time.after(new Date()), reason);
                                            if (punish.isOnline()) {
                                                FoObj<Boolean, String, Date, String> obj = MuteNBanManager.isNowBanned(punish);
                                                punish.kickPlayer(Lang.lang(punish, "extension.mute-and-ban.message.banned.temporary", obj.getFourth(), time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), obj.getSecond()));
                                            }
                                        }
                                        Lang.send(player, "extension.mute-and-ban.command.ban.success", punish.getName());
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public String hover(User clicker, Message message) {
                Player player = (Player) ((BukkitUser) clicker).getSender();
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
            DynamicBukkitChatManager.reload();
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
                                        .suggest((sender, suggestionsBuilder) -> BukkitChatManager.getManager().getBukkitUser(sender).getAvailable().stream().map(Channel::getName).forEach(suggestionsBuilder::suggest))
                                        .executes((sender, helper) -> {
                                            String channelName = helper.getAsString("channel");
                                            Channel channel = BukkitChatManager.getManager().getChannel(channelName);
                                            if (channel == null) {
                                                Lang.send(sender, "command.channel-notexists");
                                            } else {
                                                BukkitUserEntity user = (BukkitUserEntity) BukkitChatManager.getManager().getBukkitUser(sender);
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
                                    BukkitUserEntity user = (BukkitUserEntity) BukkitChatManager.getManager().getBukkitUser(sender);
                                    user.moveTo(BukkitChatManager.getManager().getGlobal());
                                    Lang.send(sender, "command.channel-switch-success", ConfigUtils.globalChannelDisplayName());
                                    return 1;
                                })))
                )
                .then(new EzCommand("send").executes((sender, helper) -> {
                    Lang.send(sender, "command.channel-gotohelp");
                    return 1;
                }).then(new EzArgument(BaseArguments.string(), "channel")
                        .suggest((sender, suggestionsBuilder) -> BukkitChatManager.getManager().getBukkitUser(sender).getAvailable().stream().map(Channel::getName).forEach(suggestionsBuilder::suggest))
                        .executes((sender, helper) -> {
                            String channelName = helper.getAsString("channel");
                            Channel channel = BukkitChatManager.getManager().getChannel(channelName);
                            if (channel == null) {
                                Lang.send(sender, "command.channel-notexists");
                            } else {
                                BukkitUserEntity user = (BukkitUserEntity) BukkitChatManager.getManager().getBukkitUser(sender);
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
                            Channel channel = BukkitChatManager.getManager().getChannel(channelName);
                            if (channel == null) {
                                Lang.send(sender, "command.channel-notexists");
                            } else {
                                BukkitUserEntity user = (BukkitUserEntity) BukkitChatManager.getManager().getBukkitUser(sender);
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
                            Message msg = DynamicBukkitChatManager.getChatCache(cache_id);
                            if (msg == null) {
                                Lang.send(sender, "command.message.cannotlocate");
                            } else {
                                ComponentBuilder componentBuilder = new ComponentBuilder();
                                Map<Integer, ? extends MessageButton> buttons = BukkitChatManager.getManager().getButtons();
                                int maxIndex = BukkitChatManager.getManager().getButtonMaxIndex();
                                int minIndex = BukkitChatManager.getManager().getButtonMinIndex();
                                if (maxIndex >= minIndex) {
                                    for (int i = 0; i < maxIndex + 1; i++) {
                                        if (!buttons.containsKey(i))
                                            continue;
                                        MessageButton button = buttons.get(i);
                                        ComponentBuilder buttonBuilder;
                                        String display = button.display(BukkitChatManager.getManager().getBukkitUser(sender), msg);
                                        if (display == null)
                                            continue;
                                        if (!display.isBlank() && !display.isEmpty()) {
                                            buttonBuilder = new ComponentBuilder(display);
                                        } else {
                                            buttonBuilder = new ComponentBuilder("Button" + (i + 1));
                                        }
                                        String hover = button.hover(BukkitChatManager.getManager().getBukkitUser(sender), msg);
                                        if (hover != null) {
                                            buttonBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
                                        }
                                        if (button instanceof CopyMessageButton copyButton) {
                                            buttonBuilder.event(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyButton.value(BukkitChatManager.getManager().getBukkitUser(sender), msg)));
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
                                    Message msg = DynamicBukkitChatManager.getChatCache(cache_id);
                                    if (msg == null) {
                                        Lang.send(sender, "command.message.cannotlocate");
                                    } else {
                                        try {
                                            BukkitChatManager.getManager().getButtons().get(index).click(BukkitChatManager.getManager().getBukkitUser(sender), msg);
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
                                        BukkitChatManager.getManager().getBukkitUser(player).kick(reason);
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
                                BukkitChatManager.getManager().getBukkitUser(player).kick();
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
                                                            Time time = BukkitChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
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
                                                    Time time = BukkitChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
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
                                            Time time = BukkitChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
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
                                                            Time time = BukkitChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
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
                                                    Time time = BukkitChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
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
                                            Time time = BukkitChatManager.getManager().createTime((date.getTime() - new Date().getTime()) / 1000L);
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
        EzCommandManager.register("dynamichat", new EzCommand("banip", "dynamichat.mnb.banip", PermissionDefault.OP)
                .then(new EzArgument(ArgumentPlayer.argumentType(), "target")
                        .then(new EzArgument(BaseArguments.string(), "time")
                                .then(new EzArgument(ArgumentChat.argumentType(), "reason")
                                        .executes((sender, helper) -> {
                                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                                Lang.send(sender, "command.extensionrequired");
                                                return 1;
                                            }
                                            List<Player> players = helper.getAsPlayers("target");
                                            try {
                                                Time time = TimeEntity.of(helper.getAsString("time"));
                                                boolean isPlayer = sender instanceof Player;
                                                Player senderPlayer = isPlayer ? (Player) sender : null;
                                                for (Player player : players) {
                                                    if (isPlayer) {
                                                        if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                            Lang.send(sender, "extension.mute-and-ban.command.ban-ip.notself");
                                                            continue;
                                                        }
                                                    }
                                                    if (BukkitChatManager.getManager().getBanIPManager().isBannedWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player))) {
                                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", player.getName());
                                                    } else {
                                                        BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIPWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player), sender.getName(), helper.getAsChatMessage("reason"), new Date(), time.after(new Date()));
                                                        player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.temporary", time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), punishment.getReason()));
                                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", player.getName());
                                                    }
                                                }
                                            } catch (TimeParseException e) {
                                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.time-format");
                                            }
                                            return 1;
                                        })
                                )
                                .executes((sender, helper) -> {
                                    if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                        Lang.send(sender, "command.extensionrequired");
                                        return 1;
                                    }
                                    List<Player> players = helper.getAsPlayers("target");
                                    try {
                                        Time time = TimeEntity.of(helper.getAsString("time"));
                                        boolean isPlayer = sender instanceof Player;
                                        Player senderPlayer = isPlayer ? (Player) sender : null;
                                        for (Player player : players) {
                                            if (isPlayer) {
                                                if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                    Lang.send(sender, "extension.mute-and-ban.command.ban-ip.notself");
                                                    continue;
                                                }
                                            }
                                            if (BukkitChatManager.getManager().getBanIPManager().isBannedWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player))) {
                                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", player.getName());
                                            } else {
                                                BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIPWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player), sender.getName(), Lang.lang(sender, "extension.mute-and-ban.default.no-reason"), new Date(), time.after(new Date()));
                                                player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.temporary", time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMonths(), time.getSeconds(), punishment.getReason()));
                                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", player.getName());
                                            }
                                        }
                                    } catch (TimeParseException e) {
                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.time-format");
                                    }
                                    return 1;
                                })
                        )
                        .then(new EzCommand("forever")
                                .then(new EzArgument(ArgumentChat.argumentType(), "reason")
                                        .executes((sender, helper) -> {if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                            Lang.send(sender, "command.extensionrequired");
                                            return 1;
                                        }
                                            List<Player> players = helper.getAsPlayers("target");
                                            try {
                                                boolean isPlayer = sender instanceof Player;
                                                Player senderPlayer = isPlayer ? (Player) sender : null;
                                                for (Player player : players) {
                                                    if (isPlayer) {
                                                        if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                            Lang.send(sender, "extension.mute-and-ban.command.ban-ip.notself");
                                                            continue;
                                                        }
                                                    }
                                                    if (BukkitChatManager.getManager().getBanIPManager().isBannedWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player))) {
                                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", player.getName());
                                                    } else {
                                                        BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIPWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player), sender.getName(), helper.getAsChatMessage("reason"), new Date(), null);
                                                        player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.forever", punishment.getReason()));
                                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", player.getName());
                                                    }
                                                }
                                            } catch (TimeParseException e) {
                                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.time-format");
                                            }
                                            return 1;
                                        })
                                )
                                .executes((sender, helper) -> {
                                    if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                        Lang.send(sender, "command.extensionrequired");
                                        return 1;
                                    }
                                    List<Player> players = helper.getAsPlayers("target");
                                    try {
                                        boolean isPlayer = sender instanceof Player;
                                        Player senderPlayer = isPlayer ? (Player) sender : null;
                                        for (Player player : players) {
                                            if (isPlayer) {
                                                if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                                    Lang.send(sender, "extension.mute-and-ban.command.ban-ip.notself");
                                                    continue;
                                                }
                                            }
                                            if (BukkitChatManager.getManager().getBanIPManager().isBannedWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player))) {
                                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", player.getName());
                                            } else {
                                                BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIPWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player), sender.getName(), Lang.lang(sender, "extension.mute-and-ban.default.no-reason"), new Date(), null);
                                                player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.forever", punishment.getReason()));
                                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", player.getName());
                                            }
                                        }
                                    } catch (TimeParseException e) {
                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.time-format");
                                    }
                                    return 1;
                                })
                        )
                        .executes((sender, helper) -> {
                            if (!ExtensionUtils.enabledMuteAndBanExtension()) {
                                Lang.send(sender, "command.extensionrequired");
                                return 1;
                            }
                            List<Player> players = helper.getAsPlayers("target");
                            try {
                                boolean isPlayer = sender instanceof Player;
                                Player senderPlayer = isPlayer ? (Player) sender : null;
                                for (Player player : players) {
                                    if (isPlayer) {
                                        if (senderPlayer.getUniqueId() == player.getUniqueId()) {
                                            Lang.send(sender, "extension.mute-and-ban.command.ban-ip.notself");
                                            continue;
                                        }
                                    }
                                    if (BukkitChatManager.getManager().getBanIPManager().isBannedWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player))) {
                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", player.getName());
                                    } else {
                                        BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIPWithPlayer(BukkitChatManager.getManager().getBukkitPlayerUser(player), sender.getName(), Lang.lang(sender, "extension.mute-and-ban.default.no-reason"), new Date(), null);
                                        player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.forever", punishment.getReason()));
                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", player.getName());
                                    }
                                }
                            } catch (TimeParseException e) {
                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.time-format");
                            }
                            return 1;
                        })
                )
                .then(new EzArgument(BaseArguments.string(), "host")
                        .then(new EzArgument(BaseArguments.string(), "time")
                                .then(new EzArgument(ArgumentChat.argumentType(), "reason")
                                        .executes((sender, helper) -> {
                                            String host = helper.getAsString("host");
                                            try {
                                                Time time = TimeEntity.of(helper.getAsString("time"));
                                                if (BukkitChatManager.getManager().getBanIPManager().isBanned(host)) {
                                                    Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", host);
                                                } else {
                                                    BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIP(host, sender.getName(), helper.getAsChatMessage("reason"), new Date(), time.after(new Date()));
                                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                                        if (!Objects.equals(host, player.getAddress().getHostString()))
                                                            continue;
                                                        player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.forever", punishment.getReason()));
                                                    }
                                                    Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", host);
                                                }
                                            } catch (TimeParseException e) {
                                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.time-format");
                                            }
                                            return 1;
                                        })
                                )
                                .executes((sender, helper) -> {
                                    String host = helper.getAsString("host");
                                    try {
                                        Time time = TimeEntity.of(helper.getAsString("time"));
                                        if (BukkitChatManager.getManager().getBanIPManager().isBanned(host)) {
                                            Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", host);
                                        } else {
                                            BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIP(host, sender.getName(), Lang.lang(sender, "extension.mute-and-ban.default.no-reason"), new Date(), time.after(new Date()));
                                            for (Player player : Bukkit.getOnlinePlayers()) {
                                                if (!Objects.equals(host, player.getAddress().getHostString()))
                                                    continue;
                                                player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.forever", punishment.getReason()));
                                            }
                                            Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", host);
                                        }
                                    } catch (TimeParseException e) {
                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.time-format");
                                    }
                                    return 1;
                                })
                        )
                        .then(new EzCommand("forever")
                                .then(new EzArgument(ArgumentChat.argumentType(), "reason")
                                        .executes((sender, helper) -> {
                                            String host = helper.getAsString("host");
                                            if (BukkitChatManager.getManager().getBanIPManager().isBanned(host)) {
                                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", host);
                                            } else {
                                                BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIP(host, sender.getName(), helper.getAsChatMessage("reason"), new Date(), null);
                                                for (Player player : Bukkit.getOnlinePlayers()) {
                                                    if (!Objects.equals(host, player.getAddress().getHostString()))
                                                        continue;
                                                    player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.forever", punishment.getReason()));
                                                }
                                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", host);
                                            }
                                            return 1;
                                        })
                                )
                                .executes((sender, helper) -> {
                                    String host = helper.getAsString("host");
                                    if (BukkitChatManager.getManager().getBanIPManager().isBanned(host)) {
                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", host);
                                    } else {
                                        BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIP(host, sender.getName(), Lang.lang(sender, "extension.mute-and-ban.default.no-reason"), new Date(), null);
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            if (!Objects.equals(host, player.getAddress().getHostString()))
                                                continue;
                                            player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.forever", punishment.getReason()));
                                        }
                                        Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", host);
                                    }
                                    return 1;
                                })
                        )
                        .executes((sender, helper) -> {
                            String host = helper.getAsString("host");
                            if (BukkitChatManager.getManager().getBanIPManager().isBanned(host)) {
                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.failed", host);
                            } else {
                                BanIPPunishment punishment = BukkitChatManager.getManager().getBanIPManager().banIP(host, sender.getName(), Lang.lang(sender, "extension.mute-and-ban.default.no-reason"), new Date(), null);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (!Objects.equals(host, player.getAddress().getHostString()))
                                        continue;
                                    player.kickPlayer(Lang.lang(player, "extension.mute-and-ban.message.banned-ip.forever", punishment.getReason()));
                                }
                                Lang.send(sender, "extension.mute-and-ban.command.ban-ip.success", host);
                            }
                            return 1;
                        })
                )
        );
        EzCommandManager.register("dynamichat", new EzCommand("unbanip", "dynamichat.mnb.unbanip", PermissionDefault.OP)
                .then(new EzArgument(BaseArguments.string(), "target")
                        .suggest((sender, suggestion) -> {
                            BukkitChatManager.getManager().getBanIPManager().getBanned().forEach(suggestion::suggest);
                            BukkitChatManager.getManager().getBanIPManager().getBannedWithPlayer().forEach(suggestion::suggest);
                        })
                        .executes((sender, helper) -> {
                            String target = helper.getAsString("target");
                            if (BukkitChatManager.getManager().getBanIPManager().isBanned(target)) {
                                BukkitChatManager.getManager().getBanIPManager().unbanIP(target);
                                Lang.send(sender, "extension.mute-and-ban.command.unban-ip.success", target);
                            } else if (BukkitChatManager.getManager().getBanIPManager().isBannedWithPlayer(target)) {
                                BukkitChatManager.getManager().getBanIPManager().unbanIPWithPlayer(target);
                                Lang.send(sender, "extension.mute-and-ban.command.unban-ip.success", target);
                            } else {
                                Lang.send(sender, "extension.mute-and-ban.command.unban-ip.failed", target);
                            }
                            return 1;
                        })
                )
        );
        NMSCommandKiller.kill(this);
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }

}
