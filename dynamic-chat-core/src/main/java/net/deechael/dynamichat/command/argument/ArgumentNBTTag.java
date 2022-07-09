package net.deechael.dynamichat.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.Ref;

import java.lang.reflect.InvocationTargetException;

public class ArgumentNBTTag implements Argument {

    private ArgumentNBTTag() {
    }

    public static ArgumentNBTTag instance() {
        return new ArgumentNBTTag();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentNBTTag().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentNBTTag() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentNBTTag");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentNBTTag");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public JsonObject get(CommandContext<Object> commandContext, String name) {
        /*try {
            return NBTUtils.parseNBTTagCompound(ArgumentNBTTag().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
          TODO
         */
        return new JsonObject();
    }

}
