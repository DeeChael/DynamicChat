package net.deechael.dynamichat.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.deechael.dynamichat.util.Ref;
import net.deechael.useless.function.Functions.Function;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public final class EzCommand {

    private static final int SINGLE_SUCCESS = 1;

    protected final LiteralArgumentBuilder<Object> literalArgumentBuilder;
    protected List<String> aliases = new ArrayList<>();
    private boolean registered = false;
    private EzCommandRegistered ezCommandRegistered;

    /**
     * Command API command
     *
     * @param commandName command name
     */
    public EzCommand(String commandName) {
        this.literalArgumentBuilder = (LiteralArgumentBuilder<Object>) Ref.invoke(null, Ref.getMethod(Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), "a", String.class), commandName);
    }

    /**
     * Command API command
     * If requires bukkit permission hasn't been registered will auto register it
     *
     * @param commandName       command name
     * @param permission        argument requires int permission over 0 less than 4
     * @param bukkitPermission  argument requires bukkit permission
     * @param permissionDefault permission defaults owner
     */
    public EzCommand(String commandName, int permission, String bukkitPermission, PermissionDefault permissionDefault) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = (LiteralArgumentBuilder<Object>) Ref.invoke(null, Ref.getMethod(Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), "a", String.class), commandName);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        literalArgumentBuilder.requires((requirement) -> (boolean) Ref.invoke(requirement,
                Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "hasPermission", int.class, String.class),
                finalPermission, bukkitPermission.toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, permissionDefault));
        } else {
            Bukkit.getPluginManager().getPermission(bukkitPermission).setDefault(permissionDefault);
        }
    }

    /**
     * Command API command
     * If requires bukkit permission hasn't been registered will auto register it
     *
     * @param commandName      command name
     * @param permission       argument requires int permission over 0 less then 4
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzCommand(String commandName, int permission, Permission bukkitPermission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = (LiteralArgumentBuilder<Object>) Ref.invoke(null, Ref.getMethod(Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), "a", String.class), commandName);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        literalArgumentBuilder.requires((requirement) -> (boolean) Ref.invoke(requirement,
                Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "hasPermission", int.class, String.class),
                finalPermission, bukkitPermission.getName().toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission.getName()) == null) {
            Bukkit.getPluginManager().addPermission(bukkitPermission);
        }
    }

    /**
     * Command API command
     * If requires bukkit permission hasn't been registered will auto register it with OP as default
     *
     * @param commandName      command name
     * @param permission       argument requires int permission over 0 less then 4
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzCommand(String commandName, int permission, String bukkitPermission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = (LiteralArgumentBuilder<Object>) Ref.invoke(null, Ref.getMethod(Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), "a", String.class), commandName);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        literalArgumentBuilder.requires((requirement) -> (boolean) Ref.invoke(requirement,
                Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "hasPermission", int.class, String.class),
                finalPermission, bukkitPermission.toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, PermissionDefault.OP));
        }
    }

    /**
     * Command API command
     * Won't check bukkit permission
     *
     * @param commandName command name
     * @param permission  argument requires int permission over 0 less then 4
     */
    public EzCommand(String commandName, int permission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = (LiteralArgumentBuilder<Object>) Ref.invoke(null, Ref.getMethod(Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), "a", String.class), commandName);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        literalArgumentBuilder.requires((requirement) -> (boolean) Ref.invoke(requirement,
                Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "c", int.class),
                finalPermission));
    }

    /**
     * Command API command
     * If requires bukkit permission hasn't been registered will auto register it with OP as default
     * Won't check integer permission
     *
     * @param commandName      command name
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzCommand(String commandName, String bukkitPermission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = (LiteralArgumentBuilder<Object>) Ref.invoke(null, Ref.getMethod(Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), "a", String.class), commandName);
        literalArgumentBuilder.requires((requirement) -> ((CommandSender) Ref.invoke(requirement, Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender"))).hasPermission(bukkitPermission.toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, PermissionDefault.OP));
        }
    }

    /**
     * Command API command
     *
     * @param commandName       command name
     * @param bukkitPermission  argument requires bukkit permission
     * @param permissionDefault permission defaults owner
     */
    public EzCommand(String commandName, String bukkitPermission, PermissionDefault permissionDefault) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = (LiteralArgumentBuilder<Object>) Ref.invoke(null, Ref.getMethod(Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), "a", String.class), commandName);
        literalArgumentBuilder.requires((requirement) -> ((CommandSender) Ref.invoke(requirement, Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender"))).hasPermission(bukkitPermission.toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, permissionDefault));
        } else {
            Bukkit.getPluginManager().getPermission(bukkitPermission).setDefault(permissionDefault);
        }
    }

    public EzCommand(String commandName, Permission bukkitPermission) {
        commandName = commandName.toLowerCase();
        this.literalArgumentBuilder = (LiteralArgumentBuilder<Object>) Ref.invoke(null, Ref.getMethod(Ref.getNmsOrOld("commands.CommandDispatcher", "CommandDispatcher"), "a", String.class), commandName);
        literalArgumentBuilder.requires((requirement) -> ((CommandSender) Ref.invoke(requirement, Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender"))).hasPermission(bukkitPermission.getName().toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission.getName()) == null) {
            Bukkit.getPluginManager().addPermission(bukkitPermission);
        }
    }

    /**
     * Set next argument
     *
     * @param ezArgument argument
     * @return self
     */
    public EzCommand then(EzArgument ezArgument) {
        if (registered) return this;
        literalArgumentBuilder.then(ezArgument.requiredArgumentBuilder);
        return this;
    }

    /**
     * Set next static argument
     *
     * @param ezCommand static argument
     * @return self
     */
    public EzCommand then(EzCommand ezCommand) {
        if (registered) return this;
        literalArgumentBuilder.then(ezCommand.literalArgumentBuilder);
        return this;
    }

    /**
     * Set next static argument
     *
     * @param ezCommandRegistered static argument
     * @return self
     */
    public EzCommand then(EzCommandRegistered ezCommandRegistered) {
        if (registered) return this;
        literalArgumentBuilder.then(ezCommandRegistered.commandNode);
        return this;
    }

    /**
     * Executes on arguments length equals this argument position in main command
     *
     * @param executes lambda with EzSender and EzArgumentHelper returns int
     * @return self
     */
    public EzCommand executes(BiFunction<CommandSender, EzArgumentHelper, Integer> executes) {
        if (registered) return this;
        literalArgumentBuilder.executes(commandContext -> executes.apply((CommandSender) Ref.invoke(commandContext.getSource(), Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender")), new EzArgumentHelper(commandContext)));
        return this;
    }

    /**
     * Redirect to the other command
     *
     * @param ezCommandRegistered command to be redirected
     */
    public EzCommand redirect(EzCommandRegistered ezCommandRegistered) {
        if (registered) return this;
        literalArgumentBuilder.redirect(ezCommandRegistered.commandNode);
        return this;
    }

    public EzCommand requires(Function<CommandSender, Boolean> function) {
        Predicate<Object> predicate = this.literalArgumentBuilder.getRequirement();
        this.literalArgumentBuilder.requires((object) -> {
            CommandSender sender = (CommandSender) Ref.invoke(object, Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender"));
            return predicate.test(object) && function.apply(sender);
        });
        return this;
    }

    /**
     * Add aliases to the command
     *
     * @param aliases aliases
     */
    public void addAliases(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
    }

    /**
     * Get if this command is registered
     *
     * @return registered
     */
    public boolean isRegistered() {
        return registered;
    }

    EzCommandRegistered register() {
        if (!registered) {
            ezCommandRegistered = new EzCommandRegistered(this);
            registered = true;
        }
        return ezCommandRegistered;
    }

}
