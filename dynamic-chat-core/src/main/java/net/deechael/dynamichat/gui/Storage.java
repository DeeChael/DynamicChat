package net.deechael.dynamichat.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Storage implements Slot {

    private ItemStack defaultItem = null;

    private final Map<Player, ItemStack> itemStacks = new HashMap<>();

    public abstract boolean isAllow(Player viewer, ItemStack cursorItem);

    public ItemStack getStored(Player viewer) {
        return itemStacks.containsKey(viewer) ? itemStacks.get(viewer) : defaultItem;
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

    public void setDefault(ItemStack itemStack) {
        this.defaultItem = itemStack;
    }

}
