package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.Ref;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public final class ArgumentUUID implements Argument {

    private ArgumentUUID() {
    }

    public static ArgumentUUID instance() {
        return new ArgumentUUID();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentUUID().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentUUID() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentUUID");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentUUID");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public UUID get(CommandContext<Object> commandContext, String name) {
        try {
            return (UUID) ArgumentUUID().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
