package net.deechael.dynamichat.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

public interface Clicker extends Slot {

    void click(Player viewer, ClickType type, InventoryAction action);

}
