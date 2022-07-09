package net.deechael.dynamichat.temp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.deechael.dynamichat.api.ChatManager;
import net.deechael.dynamichat.api.User;
import net.deechael.dynamichat.command.argument.ArgumentChat;
import net.deechael.dynamichat.command.argument.ArgumentEntities;
import net.deechael.dynamichat.command.argument.ArgumentPlayer;
import net.deechael.dynamichat.util.Ref;
import net.deechael.ref.RefInvoker;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.*;

public final class NMSCommandKiller {

    public static void kill(Plugin plugin) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            CommandDispatcher<Object> bukkitDispatcher = nmsToMojang(getBukkitCommandDispatcher());
            if (bukkitDispatcher == null) return;
            forkedTellCommand(bukkitDispatcher.getRoot());
            forkedSayCommand(bukkitDispatcher.getRoot());
            CommandDispatcher<Object> nmsDispatcher = nmsToMojang(getMinecraftCommandDispatcher());
            if (nmsDispatcher == null) return;
            forkedTellCommand(nmsDispatcher.getRoot());
            forkedSayCommand(nmsDispatcher.getRoot());
        });
    }

    private static void removeCommand(RootCommandNode<Object> root, String command) {
        RefInvoker rootInvoker = RefInvoker.ref(CommandNode.class);
        RefInvoker.ref(rootInvoker).setField("instance", root);
        ((Map<?, ?>) rootInvoker.getField("children")).remove(command);
        ((Map<?, ?>) rootInvoker.getField("literals")).remove(command);
        ((Map<?, ?>) rootInvoker.getField("arguments")).remove(command);
    }

    private static void forkedSayCommand(RootCommandNode<Object> root) {
        removeCommand(root, "say");
        removeCommand(root, "minecraft:say");
        CommandNode<Object> commandNode = LiteralArgumentBuilder.literal("say")
                .requires(requirement -> permissionCheck(requirement, 2))
                .then(RequiredArgumentBuilder.argument("message", ArgumentChat.instance().argument())
                        .executes(commandContext -> {
                            CommandSender sender = getBukkitSender(commandContext.getSource());
                            ChatManager.getManager().getUser(sender).say(ArgumentChat.chatToString(ArgumentChat.instance().get(commandContext, "message")));
                            return 1;
                        })
                ).build();
        root.addChild(commandNode);
        root.addChild(LiteralArgumentBuilder.literal("minecraft:say").redirect(commandNode).build());
    }

    private static void forkedTellCommand(RootCommandNode<Object> root) {
        removeCommand(root, "minecraft:msg");
        removeCommand(root, "minecraft:tell");
        removeCommand(root, "minecraft:w");
        removeCommand(root, "msg");
        removeCommand(root, "tell");
        removeCommand(root, "w");
        CommandNode<Object> commandNode = LiteralArgumentBuilder.literal("msg")
                .then(processTellCommand())
                .then(
                        RequiredArgumentBuilder
                                .argument("targets", ArgumentPlayer.argumentType())
                                .then(
                                        RequiredArgumentBuilder
                                                .argument("message", ArgumentChat.argumentType())
                                                .executes((commandContext) -> {
                                                    int i = 0;
                                                    User sender = ChatManager.getManager().getUser(getBukkitSender(commandContext.getSource()));

                                                    List<Player> list = new ArrayList<>();
                                                    Collection<Object> collection = (Collection<Object>) ArgumentPlayer.instance().get(commandContext, "targets");
                                                    if (collection != null) {
                                                        for (Object object : collection) {
                                                            Player player = (Player) ArgumentEntities.nmsEntityToBukkitEntity(object);
                                                            if (player != null) list.add(player);
                                                        }
                                                    }
                                                    for (Player player : list) {
                                                        sender.whisper(ChatManager.getManager().getPlayerUser(player), ArgumentChat.chatToString(ArgumentChat.instance().get(commandContext, "message")));
                                                        i++;
                                                    }
                                                    return i;
                                                })
                                )
                ).build();
        root.addChild(commandNode);
        root.addChild(LiteralArgumentBuilder.literal("tell").redirect(commandNode).build());
        root.addChild(LiteralArgumentBuilder.literal("w").redirect(commandNode).build());
        root.addChild(LiteralArgumentBuilder.literal("minecraft:msg").redirect(commandNode).build());
        root.addChild(LiteralArgumentBuilder.literal("minecraft:tell").redirect(commandNode).build());
        root.addChild(LiteralArgumentBuilder.literal("minecraft:w").redirect(commandNode).build());
    }

    private static CommandNode<Object> processTellCommand() {
        return LiteralArgumentBuilder.literal("console")
                .then(RequiredArgumentBuilder.argument("message", ArgumentChat.argumentType())
                        .executes(commandContext -> {
                            User sender = ChatManager.getManager().getUser(getBukkitSender(commandContext.getSource()));
                            User receiver = ChatManager.getManager().getUser(Bukkit.getConsoleSender());
                            sender.whisper(receiver, ArgumentChat.chatToString(ArgumentChat.instance().get(commandContext, "message")));
                            return 1;
                        })
                )
                .build();
    }

    private static Command createVanillaWrapper(Object commandDispatcher, CommandNode<Object> commandNode) {
        return (Command) Ref.newInstance(Objects.requireNonNull(Ref.getConstructor(Ref.getObcClass("command.VanillaCommandWrapper"), Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), CommandNode.class)), commandDispatcher, commandNode);
    }

    private static CommandSender getBukkitSender(Object commandListenerWrapper) {
        return ((CommandSender) Ref.invoke(commandListenerWrapper, Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender")));
    }

    private static CommandDispatcher<Object> nmsToMojang(Object commandDispatcher) {
        return (CommandDispatcher<Object>) Ref.invoke(commandDispatcher, Objects.requireNonNull(Ref.getMethod(Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), "a")));
    }

    private static Object getBukkitCommandDispatcher() {
        return Ref.invoke(getMinecraftServer(), Objects.requireNonNull(Ref.getMethod(Ref.getNmsOrOld("server.MinecraftServer", "MinecraftServer"), "aC")));
    }

    private static Object getMinecraftCommandDispatcher() {
        return Ref.invoke(getMinecraftServer(), Objects.requireNonNull(Ref.getField(Ref.getNmsOrOld("server.MinecraftServer", "MinecraftServer"), "vanillaCommandDispatcher")));
    }

    private static Object getMinecraftServer() {
        return Ref.invoke(Bukkit.getServer(), Objects.requireNonNull(Ref.getMethod(Bukkit.getServer().getClass(), "getServer")));
    }

    private static void setDispatcher(Command vanillaListenerWrapper, Object nmsCommandDispatcher) {
        try {
            Field field = Ref.getObcClass("command.VanillaCommandWrapper").getDeclaredField("dispatcher");
            field.setAccessible(true);
            field.set(vanillaListenerWrapper, nmsCommandDispatcher);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void setKnownCommands(Map<String,Command> knownCommands) {
        try {
            Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            field.set(getCommandMap(), knownCommands);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Map<String,Command> getKnownCommands() {
        return (Map<String, Command>) Ref.invoke(getCommandMap(), Objects.requireNonNull(Ref.getField(SimpleCommandMap.class, "knownCommands")));
    }

    private static CommandMap getCommandMap() {
        return (CommandMap) Ref.invoke(Bukkit.getServer(), Objects.requireNonNull(Ref.getMethod(Ref.getObcClass("CraftServer"), "getCommandMap")));
    }

    private static boolean permissionCheck(Object commandListenerWrapper, int level) {
        return (boolean) Ref.invoke(commandListenerWrapper, Objects.requireNonNull(Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "c", int.class)), level);
    }


}
