package net.deechael.dynamichat.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.deechael.dynamichat.util.Ref;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EzCommandManager implements Listener {

    public static final EzCommandManager INSTANCE = new EzCommandManager();
    private static final Map<String, List<EzCommand>> REGISTERED = new HashMap<>();
    private static CommandMap BUKKIT_COMMAND_MAP;

    private EzCommandManager() {
        Bukkit.getPluginManager().addPermission(new Permission("ez.api.command.check", PermissionDefault.TRUE));
    }

    /**
     * Register command with default prefix "ez-api"
     *
     * @param ezCommand command
     * @return registered command
     */
    public static EzCommandRegistered register(EzCommand ezCommand) {
        return register("ez-api", ezCommand);
    }

    /**
     * Register command with prefix
     *
     * @param prefix    prefix
     * @param ezCommand command
     * @return registered command
     */
    public static EzCommandRegistered register(String prefix, EzCommand ezCommand) {
        if (BUKKIT_COMMAND_MAP == null) {
            try {
                BUKKIT_COMMAND_MAP = (CommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
        EzCommandRegistered ezCommandRegistered = ezCommand.register();
        //EasyAPI.getBukkitCommandMap().register(prefix, new UnsupportCommand(ezCommandRegistered.commandNode.getName(), ezCommand.aliases.toArray(new String[0])));
        if (!REGISTERED.containsKey(prefix)) REGISTERED.put(prefix, new ArrayList<>());
        REGISTERED.get(prefix).add(ezCommandRegistered.ezCommand);
        Command command = createVanillaCommandWrapper(null, ezCommandRegistered.commandNode);
        command.setPermission("ez.api.command.check");
        command.setAliases(ezCommand.aliases);
        BUKKIT_COMMAND_MAP.register(prefix.toLowerCase(), command);
        return ezCommandRegistered;
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

    private static <T> LiteralCommandNode<?> clone(String prefix, CommandNode commandNode, Class<T> clazz) {
        return new LiteralCommandNode<T>(prefix.toLowerCase() + ":" + commandNode.getName().toLowerCase(), commandNode.getCommand(), commandNode.getRequirement(), commandNode.getRedirect(), commandNode.getRedirectModifier(), commandNode.isFork());
    }

    private static <T> LiteralCommandNode<?> clone(CommandNode commandNode, Class<T> clazz) {
        return new LiteralCommandNode<T>(commandNode.getName().toLowerCase(), commandNode.getCommand(), commandNode.getRequirement(), commandNode.getRedirect(), commandNode.getRedirectModifier(), commandNode.isFork());
    }

    private static Class<?> CommandListenerWrapper() {
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("CommandListenerWrapper");
        } else {
            return Ref.getClass("net.minecraft.commands.CommandListenerWrapper");
        }
    }

    private static void registerToCommandPatcher(Object nmsCommandDispatcher, CommandNode<Object> commandNode) {
        CommandDispatcher<Object> commandDispatcher = getMojangCommandDispatcher(nmsCommandDispatcher);
        commandDispatcher.getRoot().addChild(commandNode);
    }

    private static Map<String, Command> getKnownCommands() {
        if (BUKKIT_COMMAND_MAP == null) {
            try {
                BUKKIT_COMMAND_MAP = (CommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            return (Map<String, Command>) field.get(BUKKIT_COMMAND_MAP);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setKnownCommands(Map<String, Command> knownCommands) {
        try {
            Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
            field.setAccessible(true);
            field.set(BUKKIT_COMMAND_MAP, knownCommands);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void removeFromRoot(RootCommandNode<Object> rootCommandNode, CommandNode<Object> commandNode) {
        try {
            Field field = CommandNode.class.getDeclaredField("children");
            field.setAccessible(true);
            Map<String, CommandNode<Object>> map = (Map<String, CommandNode<Object>>) field.get(rootCommandNode);
            for (String key : map.keySet()) {
                if (map.get(key).equals(commandNode)) {
                    map.remove(key);
                }
            }
            field.set(rootCommandNode, map);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Command createVanillaCommandWrapper(Object nmsCommandDispatcher, CommandNode<Object> commandNode) {
        try {
            return (Command) Ref.getObcClass("command.VanillaCommandWrapper").getConstructor(nmsCommandDispatcher(), CommandNode.class).newInstance(nmsCommandDispatcher, commandNode);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CommandDispatcher<Object> getMojangCommandDispatcher(Object nmsCommandDispatcher) {
        try {
            return (CommandDispatcher<Object>) nmsCommandDispatcher().getMethod("a").invoke(nmsCommandDispatcher);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> nmsCommandDispatcher() {
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("CommandDispatcher");
        } else {
            return Ref.getClass("net.minecraft.commands.CommandDispatcher");
        }
    }

    private static Object getVanillaCommandDispatcher() {
        Object minecraftServer = getMinecraftServer();
        try {
            return MinecraftServer().getField("vanillaCommandDispatcher").get(minecraftServer);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getBukkitCommandDispatcher() {
        Object minecraftServer = getMinecraftServer();
        try {
            return MinecraftServer().getMethod("aC").invoke(minecraftServer);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> MinecraftServer() {
        return Ref.getNmsOrOld("server.MinecraftServer", "MinecraftServer");
    }

    private static Object getMinecraftServer() {
        try {
            return Bukkit.getServer().getClass().getMethod("getServer").invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @EventHandler
    public void onLoad(ServerLoadEvent serverLoadEvent) {
        Object commandDispatcher = getBukkitCommandDispatcher();
        Map<String, Command> knownCommands = getKnownCommands();
        if (knownCommands != null) {
            for (String prefix : REGISTERED.keySet()) {
                for (EzCommand ezCommand : REGISTERED.get(prefix)) {
                    CommandNode<Object> commandNode = ezCommand.register().commandNode;
                    Command command = knownCommands.get(prefix.toLowerCase() + ":" + commandNode.getName().toLowerCase());
                    List<String> aliases = ezCommand.aliases;
                    for (String string : aliases) {
                        String key = prefix.toLowerCase() + ":" + string.toLowerCase();
                        Command aliaCommand = knownCommands.get(key);
                        if (aliaCommand.getPermission().equalsIgnoreCase("ez.api.command.check")) {
                            LiteralCommandNode<?> literalCommandNode = clone(prefix, commandNode, CommandListenerWrapper());
                            registerToCommandPatcher(commandDispatcher, (CommandNode<Object>) literalCommandNode);
                            setDispatcher(aliaCommand, commandDispatcher);
                            knownCommands.put(key, aliaCommand);
                        }
                        Command aliaCmd = knownCommands.get(string.toLowerCase());
                        if (aliaCmd.getPermission().equalsIgnoreCase("ez.api.command.check")) {
                            LiteralCommandNode<?> literalCommandNode = clone(commandNode, CommandListenerWrapper());
                            registerToCommandPatcher(commandDispatcher, (CommandNode<Object>) literalCommandNode);
                            setDispatcher(aliaCmd, commandDispatcher);
                            knownCommands.put(string.toLowerCase(), aliaCmd);
                        }
                    }
                    Command cmd = knownCommands.get(commandNode.getName().toLowerCase());
                    if (cmd.getPermission().equalsIgnoreCase("ez.api.command.check")) {
                        registerToCommandPatcher(commandDispatcher, commandNode);
                        setDispatcher(cmd, commandDispatcher);
                        knownCommands.put(commandNode.getName().toLowerCase(), cmd);
                    }
                    LiteralCommandNode<?> literalCommandNode = clone(prefix, commandNode, CommandListenerWrapper());
                    registerToCommandPatcher(commandDispatcher, (CommandNode<Object>) literalCommandNode);
                    setDispatcher(command, commandDispatcher);
                    knownCommands.put(prefix.toLowerCase() + ":" + commandNode.getName().toLowerCase(), command);
                }
            }
            setKnownCommands(knownCommands);
        }
    }

}
