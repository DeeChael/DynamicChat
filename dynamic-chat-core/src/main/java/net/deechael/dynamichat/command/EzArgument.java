package net.deechael.dynamichat.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.deechael.dynamichat.command.argument.ArgumentAttribute;
import net.deechael.dynamichat.command.argument.ArgumentEntityType;
import net.deechael.dynamichat.util.Ref;
import net.deechael.useless.function.parameters.DuParameter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.function.BiFunction;

public final class EzArgument {

    final RequiredArgumentBuilder<Object, ?> requiredArgumentBuilder;

    /**
     * Command API argument
     * It's not static
     *
     * @param argumentType argument type
     * @param argumentName argument name
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName) {
        argumentName = argumentName.toLowerCase();
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Command API argument
     * It's not static
     *
     * @param argumentType      argument type
     * @param argumentName      argument name
     * @param permission        argument requires int permission over 0 less than 4
     * @param bukkitPermission  argument requires bukkit permission
     * @param permissionDefault permission defaults owner
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName, int permission, String bukkitPermission, PermissionDefault permissionDefault) {
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        requiredArgumentBuilder.requires((requirement) -> (boolean) Ref.invoke(requirement,
                Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "hasPermission", int.class, String.class),
                finalPermission, bukkitPermission.toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, permissionDefault));
        } else {
            Bukkit.getPluginManager().getPermission(bukkitPermission).setDefault(permissionDefault);
        }
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Command API argument
     * It's not static
     *
     * @param argumentType     argument type
     * @param argumentName     argument name
     * @param permission       argument requires int permission over 0 less than 4
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName, int permission, Permission bukkitPermission) {
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        requiredArgumentBuilder.requires((requirement) -> (boolean) Ref.invoke(requirement,
                Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "hasPermission", int.class, String.class),
                finalPermission, bukkitPermission.getName().toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission.getName()) == null) {
            Bukkit.getPluginManager().addPermission(bukkitPermission);
        }
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Command API argument
     * It's not static
     *
     * @param argumentType     argument type
     * @param argumentName     argument name
     * @param permission       argument requires int permission over 0 less than 4
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName, int permission, String bukkitPermission) {
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        requiredArgumentBuilder.requires((requirement) -> (boolean) Ref.invoke(requirement,
                Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "hasPermission", int.class, String.class),
                finalPermission, bukkitPermission.toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, PermissionDefault.OP));
        }
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Command API argument
     * It's not static
     *
     * @param argumentType argument type
     * @param argumentName argument name
     * @param permission   argument requires int permission over 0 less than 4
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName, int permission) {
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        if (permission < 0) permission = 0;
        if (permission > 4) permission = 4;
        int finalPermission = permission;
        requiredArgumentBuilder.requires((requirement) -> (boolean) Ref.invoke(requirement,
                Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "c", int.class),
                finalPermission));
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Command API argument
     * It's not static
     *
     * @param argumentType     argument type
     * @param argumentName     argument name
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName, Permission bukkitPermission) {
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        requiredArgumentBuilder.requires((requirement) -> ((CommandSender) Ref.invoke(requirement, Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender"))).hasPermission(bukkitPermission.getName().toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission.getName()) == null) {
            Bukkit.getPluginManager().addPermission(bukkitPermission);
        }
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Command API argument
     * It's not static
     *
     * @param argumentType     argument type
     * @param argumentName     argument name
     * @param bukkitPermission argument requires bukkit permission
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName, String bukkitPermission) {
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        requiredArgumentBuilder.requires((requirement) -> ((CommandSender) Ref.invoke(requirement, Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender"))).hasPermission(bukkitPermission.toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, PermissionDefault.OP));
        }
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Command API argument
     * It's not static
     *
     * @param argumentType      argument type
     * @param argumentName      argument name
     * @param bukkitPermission  argument requires bukkit permission
     * @param permissionDefault permission defaults owner
     */
    public EzArgument(ArgumentType<?> argumentType, String argumentName, String bukkitPermission, PermissionDefault permissionDefault) {
        requiredArgumentBuilder = RequiredArgumentBuilder.argument(argumentName, argumentType);
        requiredArgumentBuilder.requires((requirement) -> ((CommandSender) Ref.invoke(requirement, Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender"))).hasPermission(bukkitPermission.toLowerCase()));
        if (Bukkit.getPluginManager().getPermission(bukkitPermission) == null) {
            Bukkit.getPluginManager().addPermission(new Permission(bukkitPermission, permissionDefault));
        }
        if (argumentType.getClass().equals(ArgumentAttribute.ArgumentMinecraftKeyRegistered())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentAttribute.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
        if (argumentType.getClass().equals(ArgumentEntityType.ArgumentEntitySummon())) {
            SuggestionProvider<Object> suggestionProvider = ArgumentEntityType.suggests();
            if (suggestionProvider != null) {
                requiredArgumentBuilder.suggests(suggestionProvider);
            }
        }
    }

    /**
     * Set next argument
     *
     * @param ezArgument argument
     * @return self
     */
    public EzArgument then(EzArgument ezArgument) {
        requiredArgumentBuilder.then(ezArgument.requiredArgumentBuilder);
        return this;
    }

    /**
     * Set next static argument
     *
     * @param ezCommand static argument
     * @return self
     */
    public EzArgument then(EzCommand ezCommand) {
        requiredArgumentBuilder.then(ezCommand.literalArgumentBuilder);
        return this;
    }

    /**
     * Set next static argument
     *
     * @param ezCommandRegistered static argument
     * @return self
     */
    public EzArgument then(EzCommandRegistered ezCommandRegistered) {
        requiredArgumentBuilder.then(ezCommandRegistered.commandNode);
        return this;
    }

    /**
     * Executes on arguments length equals this argument position in main command
     *
     * @param executes lambda with EzSender and EzArgumentHelper returns int
     * @return self
     */
    public EzArgument executes(BiFunction<CommandSender, EzArgumentHelper, Integer> executes) {
        requiredArgumentBuilder.executes(commandContext -> executes.apply((CommandSender) Ref.invoke(commandContext.getSource(), Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender")), new EzArgumentHelper(commandContext)));
        return this;
    }

    /**
     * Redirect to the other command
     *
     * @param ezCommandRegistered command to be redirected
     */
    public void redirect(EzCommandRegistered ezCommandRegistered) {
        requiredArgumentBuilder.redirect(ezCommandRegistered.commandNode);
    }

    /**
     * Set tab complete suggestion
     *
     * @param biFunction lambda with EzSender and SuggestionsBuilder returns CompletableFuture
     * @return self
     */
    public EzArgument suggest(DuParameter<CommandSender, SuggestionsBuilder> biFunction) {
        requiredArgumentBuilder.suggests(((commandContext, suggestionsBuilder) -> {
            biFunction.apply((CommandSender) Ref.invoke(commandContext.getSource(), Ref.getMethod(Ref.getNmsOrOld("commands.CommandListenerWrapper", "CommandListenerWrapper"), "getBukkitSender")), suggestionsBuilder);
            return suggestionsBuilder.buildFuture();
        }));
        return this;
    }

}
