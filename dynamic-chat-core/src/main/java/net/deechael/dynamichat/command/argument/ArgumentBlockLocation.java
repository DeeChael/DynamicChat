package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.Ref;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentBlockLocation implements Argument {

    private ArgumentBlockLocation() {
    }

    public static ArgumentBlockLocation instance() {
        return new ArgumentBlockLocation();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    public static Location blockPositionToLocation(World world, Object blockPosition) {
        Location location = blockPositionToLocation(blockPosition);
        location.setWorld(world);
        return location;
    }

    public static Location blockPositionToLocation(Object blockPosition) {
        return new Location(Bukkit.getWorlds().get(0), getX(blockPosition), getY(blockPosition), getZ(blockPosition));
    }

    private static int getX(Object blockPosition) {
        if (Ref.getVersion() < 9) return 0;
        if (Ref.getVersion() >= 9 && Ref.getVersion() <= 16) {
            try {
                return (int) blockPosition.getClass().getMethod("getX").invoke(blockPosition);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static int getY(Object blockPosition) {
        if (Ref.getVersion() < 9) return 0;
        if (Ref.getVersion() >= 9 && Ref.getVersion() <= 16) {
            try {
                return (int) blockPosition.getClass().getMethod("getY").invoke(blockPosition);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static int getZ(Object blockPosition) {
        if (Ref.getVersion() < 9) return 0;
        if (Ref.getVersion() >= 9 && Ref.getVersion() <= 16) {
            try {
                return (int) blockPosition.getClass().getMethod("getZ").invoke(blockPosition);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentPosition().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentPosition() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentPosition");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.coordinates.ArgumentPosition");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Object get(CommandContext<Object> commandContext, String name) {
        try {
            return ArgumentPosition().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
