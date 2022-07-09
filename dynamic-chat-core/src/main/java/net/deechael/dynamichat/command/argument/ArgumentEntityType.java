package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.deechael.dynamichat.util.Ref;
import net.deechael.ref.RefInvoker;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Optional;

public final class ArgumentEntityType implements Argument {

    private ArgumentEntityType() {
    }

    public static ArgumentEntityType instance() {
        return new ArgumentEntityType();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    public static EntityType nmsEntityTypesToBukkitEntityType(Object nmsEntityTypes) {
        try {
            Object nmsEntity = EntityTypes().getMethod("a", nmsWorld().instanceClass()).invoke(nmsEntityTypes, getNmsWorld(Bukkit.getWorlds().get(0)));
            Entity entity = (Entity) nmsEntity.getClass().getMethod("getBukkitEntity").invoke(nmsEntity);
            EntityType entityType = entity.getType();
            if (EntityLiving().instanceClass().isInstance(nmsEntity)) {
                RefInvoker EntityLiving = RefInvoker.ref(nmsEntity);
                EntityLiving.setField("drops", new ArrayList<>());
                EntityLiving.setField("expToDrop", 0);
            }
            nmsEntity.getClass().getMethod("die").invoke(nmsEntity);
            return entityType;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SuggestionProvider<Object> suggests() {
        RefInvoker refInvoker = CompletionProviders();
        return (SuggestionProvider<Object>) refInvoker.getStatic("d");
    }

    private static RefInvoker nmsWorld() {
        return RefInvoker.ref(Ref.getNmsOrOld("world.level.World", "World"));
    }

    private static RefInvoker EntityLiving() {
        return RefInvoker.ref(Ref.getNmsOrOld("world.entity.EntityLiving", "EntityLiving"));
    }

    private static Object getNmsWorld(World world) {
        try {
            return Bukkit.getWorlds().get(0).getClass().getMethod("getHandle").invoke(world);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentEntitySummon().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> EntityTypes() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("EntityTypes");
        } else {
            return Ref.getClass("net.minecraft.world.entity.EntityTypes");
        }
    }

    public static Class<?> ArgumentEntitySummon() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentEntitySummon");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentEntitySummon");
        }
    }

    private static RefInvoker CompletionProviders() {
        return RefInvoker.ref(Ref.getNmsOrOld("commands.synchronization.CompletionProviders", "CompletionProviders"));
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Object get(CommandContext<Object> commandContext, String name) {
        try {
            Object minecraftKey = ArgumentEntitySummon().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);

            String key = (String) minecraftKey.getClass().getMethod("a").invoke(minecraftKey);
            Optional<Object> optional = (Optional<Object>) EntityTypes().getMethod("a", String.class).invoke(null, key);
            if (optional.isPresent()) {
                return optional.get();
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
