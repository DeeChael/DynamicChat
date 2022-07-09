package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.Ref;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentEnchantment implements Argument {

    private ArgumentEnchantment() {
    }

    public static ArgumentEnchantment instance() {
        return new ArgumentEnchantment();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    public static Enchantment nmsEnchantmentToBukkitEnchantment(Object nmsEnchantment) {
        try {
            return (Enchantment) Ref.getObcClass("enchantments.CraftEnchantment").getConstructor(Enchantment()).newInstance(nmsEnchantment);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentEnchantment().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentEnchantment() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentEnchantment");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.ArgumentEnchantment");
        }
    }

    private static Class<?> Enchantment() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("Enchantment");
        } else {
            return Ref.getClass("net.minecraft.world.item.enchantment.Enchantment");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Object get(CommandContext<Object> commandContext, String name) {
        try {
            return ArgumentEnchantment().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
