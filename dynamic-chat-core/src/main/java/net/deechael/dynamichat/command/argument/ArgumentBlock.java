package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.Ref;
import org.bukkit.Material;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentBlock implements Argument {

    private ArgumentBlock() {
    }

    public static ArgumentBlock instance() {
        return new ArgumentBlock();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    public static Material nmsBlockToMaterial(Object object) {
        try {
            return (Material) Ref.getObcClass("util.CraftMagicNumbers").getMethod("getMaterial", Block()).invoke(null, object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentTile().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentTile() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentTile");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentTile");
        }
    }

    private static Class<?> Block() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("Block");
        } else {
            return Ref.getClass("net.minecraft.world.level.block.Block");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Object get(CommandContext<Object> commandContext, String name) {
        try {
            Object a = ArgumentTile().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
            Object b = a.getClass().getMethod("a").invoke(a);
            return b.getClass().getMethod("getBlock").invoke(b);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
