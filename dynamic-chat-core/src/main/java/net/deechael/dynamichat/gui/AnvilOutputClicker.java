package net.deechael.dynamichat.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface AnvilOutputClicker extends AnvilOutputSlot {

    void click(Player viewer, Inventory inventory, ItemStack outputItem, ClickType type, InventoryAction action);

}
