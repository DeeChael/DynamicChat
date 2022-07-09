package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.EzClass;
import net.deechael.dynamichat.util.Ref;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentChatColor implements Argument {

    private ArgumentChatColor() {
    }

    public static ArgumentChatColor instance() {
        return new ArgumentChatColor();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    public static char getColorChar(Object enumChatFormat) {
        EzClass EnumChatFormat = Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? new EzClass(Ref.getNmsClass("EnumChatFormat")) : new EzClass("net.minecraft.EnumChatFormat");
        EnumChatFormat.setInstance(enumChatFormat);
        return (char) (Ref.getVersion() <= 15 && Ref.getVersion() >= 9 ? EnumChatFormat.getField("character") : EnumChatFormat.invokeMethod("a", new Class[0], new Object[0]));
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentChatFormat().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentChatFormat() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentChatFormat");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentChatFormat");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Object get(CommandContext<Object> commandContext, String name) {
        try {
            return ArgumentChatFormat().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
