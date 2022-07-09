package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.Ref;
import net.deechael.ref.RefInvoker;
import org.bukkit.Particle;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentParticle implements Argument {

    private ArgumentParticle() {
    }

    public static ArgumentParticle instance() {
        return new ArgumentParticle();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    public static Particle nmsParticleToBukkitParticle(Object particleParam) {
        RefInvoker ParticleParam = RefInvoker.ref(Ref.getNmsOrOld("core.particles.ParticleParam", "ParticleParam"));
        RefInvoker CraftParticle = RefInvoker.ref(Ref.getObcClass("CraftParticle"));
        return (Particle) CraftParticle.invokeStatic("toBukkit", new Class[]{ParticleParam.instanceClass()}, new Object[]{particleParam});
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentParticle().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentParticle() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentParticle");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentParticle");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Object get(CommandContext<Object> commandContext, String name) {
        try {
            return ArgumentParticle().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
