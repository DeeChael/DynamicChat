package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.Ref;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class ArgumentLocation implements Argument {

    private ArgumentLocation() {
    }

    public static ArgumentLocation instance() {
        return new ArgumentLocation();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    public static Location vec3DToLocation(World world, Object vec3D) {
        Location location = vec3DToLocation(vec3D);
        location.setWorld(world);
        return location;
    }

    public static Location vec3DToLocation(Object vec3D) {
        return new Location(Bukkit.getWorlds().get(0), getX(vec3D), getY(vec3D), getZ(vec3D));
    }

    private static double getX(Object vec3D) {
        if (Ref.getVersion() < 9) return 0.0;
        if (Ref.getVersion() == 9 || Ref.getVersion() == 10) {
            try {
                Field field = vec3D.getClass().getDeclaredField("x");
                field.setAccessible(true);
                return (double) field.get(vec3D);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return 0.0;
        }
        try {
            return (double) vec3D.getClass().getMethod("getX").invoke(vec3D);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private static double getY(Object vec3D) {
        if (Ref.getVersion() < 9) return 0.0;
        if (Ref.getVersion() == 9 || Ref.getVersion() == 10) {
            try {
                Field field = vec3D.getClass().getDeclaredField("y");
                field.setAccessible(true);
                return (double) field.get(vec3D);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return 0.0;
        }
        try {
            return (double) vec3D.getClass().getMethod("getY").invoke(vec3D);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private static double getZ(Object vec3D) {
        if (Ref.getVersion() < 9) return 0.0;
        if (Ref.getVersion() == 9 || Ref.getVersion() == 10) {
            try {
                Field field = vec3D.getClass().getDeclaredField("z");
                field.setAccessible(true);
                return (double) field.get(vec3D);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return 0.0;
        }
        try {
            return (double) vec3D.getClass().getMethod("getZ").invoke(vec3D);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentVec3().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentVec3() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentVec3");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.coordinates.ArgumentVec3");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Object get(CommandContext<Object> commandContext, String name) {
        try {
            return ArgumentVec3().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
