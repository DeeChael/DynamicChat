package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.deechael.dynamichat.util.Ref;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public final class ArgumentItemStack implements Argument {

    private ArgumentItemStack() {
    }

    public static ArgumentItemStack instance() {
        return new ArgumentItemStack();
    }

    public static ArgumentType<Object> argumentType() {
        return instance().argument();
    }

    public static ItemStack nmsItemStackToBukkitItemStack(Object nmsItemStack, int amount) {
        try {
            ItemStack itemStack = (ItemStack) Ref.getObcClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStack.getClass()).invoke(null, nmsItemStack);
            if (amount > itemStack.getMaxStackSize()) amount = itemStack.getMaxStackSize();
            itemStack.setAmount(amount);
            return itemStack;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new ItemStack(Material.AIR);
    }

    private static ArgumentType<Object> get() {
        try {
            return (ArgumentType<Object>) ArgumentItemStack().getMethod("a").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?> ArgumentItemStack() {
        if (Ref.getVersion() < 9) return null;
        if (Ref.getVersion() <= 15 && Ref.getVersion() >= 9) {
            return Ref.getNmsClass("ArgumentItemStack");
        } else {
            return Ref.getClass("net.minecraft.commands.arguments.item.ArgumentItemStack");
        }
    }

    @Override
    public ArgumentType<Object> argument() {
        return get();
    }

    @Override
    public Object get(CommandContext<Object> commandContext, String name) {
        try {
            Object object = ArgumentItemStack().getMethod("a", CommandContext.class, String.class).invoke(null, commandContext, name);
            return object.getClass().getMethod("a", int.class, boolean.class).invoke(object, 1, false);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
