package net.deechael.dynamichat.command.argument;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.Ref;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

public final class ArgumentOfflinePlayer implements Argument {

    private ArgumentOfflinePlayer() {
    }

    public static ArgumentOfflinePlayer instance() {
        return new ArgumentOfflinePlayer();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentProfile().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentProfile() {
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentProfile");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentProfile");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Collection<GameProfile> get(CommandContext<Object> commandContext, String name) {
        try {
            return (Collection<GameProfile>) ArgumentProfile().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
