package net.deechael.dynamichat.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class NormalGUIHolder implements InventoryHolder {

    private final String id;

    private Inventory inventory;

    public NormalGUIHolder(String id) {
        this.id = id;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getId() {
        return id;
    }
}
