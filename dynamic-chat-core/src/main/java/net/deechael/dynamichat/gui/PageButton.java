package net.deechael.dynamichat.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;

public interface PageButton extends PageSlot{

    void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action);

}
