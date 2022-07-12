package net.deechael.dynamichat.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Storage implements Slot {

    private final Map<Player, ItemStack> itemStacks = new HashMap<>();
    private ItemStack defaultItem = null;

    public abstract boolean isAllow(Player viewer, ItemStack cursorItem);

    public final ItemStack getStored(Player viewer) {
        ItemStack itemStack = itemStacks.containsKey(viewer) ? itemStacks.get(viewer) : defaultItem;
        if (itemStack != null)
            itemStack = itemStack.getType() == Material.AIR ? null : itemStack;
        return itemStack;
    }

    final void setStored(Player viewer, ItemStack itemStack) {
        if (itemStack == null)
            itemStack = new ItemStack(Material.AIR);
        this.itemStacks.put(viewer, itemStack);
    }

    final void switchCursor(Player viewer) {
        ItemStack tempItem = viewer.getItemOnCursor();
        viewer.setItemOnCursor(this.itemStacks.get(viewer));
        this.itemStacks.put(viewer, tempItem);
    }

    public final void setDefault(ItemStack itemStack) {
        this.defaultItem = itemStack;
    }

}
