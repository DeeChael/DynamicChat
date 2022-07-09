package net.deechael.dynamichat.gui.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class Storage implements Slot {

    private final Map<Player, ItemStack> itemStacks = new HashMap<>();

    public abstract boolean isAllow(Player viewer, ItemStack itemStack);

    public ItemStack getStored(Player viewer) {
        return itemStacks.get(viewer);
    }

    final void setStored(Player viewer, ItemStack itemStack) {
        this.itemStacks.put(viewer, itemStack);
    }

    final void switchCursor(Player viewer) {
        ItemStack tempItem = viewer.getItemOnCursor();
        viewer.setItemOnCursor(this.itemStacks.get(viewer));
        this.itemStacks.put(viewer, tempItem);
    }

}
