package net.deechael.dynamichat;

import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.ChatManager;
import net.deechael.dynamichat.command.EzArgument;
import net.deechael.dynamichat.command.EzCommand;
import net.deechael.dynamichat.command.EzCommandManager;
import net.deechael.dynamichat.command.EzCommandRegistered;
import net.deechael.dynamichat.command.argument.ArgumentChat;
import net.deechael.dynamichat.command.argument.BaseArguments;
import net.deechael.dynamichat.entity.ChannelEntity;
import net.deechael.dynamichat.entity.DynamicChatManager;
import net.deechael.dynamichat.entity.UserEntity;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.temp.DynamicChatListener;
import net.deechael.dynamichat.temp.NMSCommandKiller;
import net.deechael.dynamichat.util.ConfigUtils;
import net.deechael.dynamichat.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

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
        new DynamicChatPlaceholder().register();
        Bukkit.getPluginManager().registerEvents(EzCommandManager.INSTANCE, this);
        Bukkit.getPluginManager().registerEvents(new DynamicChatListener(), this);
        EzCommandRegistered mainCommand = EzCommandManager.register("dyanmichat", new EzCommand("dynamic-chat", "dynamichat.command.dynamic-chat", PermissionDefault.OP).executes((sender, helper) -> {
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
        })));
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
        //EzCommandManager.register("dynamichat", new EzCommand("dynamicchat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dynamichat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dychat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dchat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dc").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dy-chat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dee-chat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("deechat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("deechaelchat").redirect(mainCommand));
        NMSCommandKiller.kill(this);
    }

    @Override
    public void onDisable() {
        ConfigUtils.save();
    }

}
