package net.deechael.dynamichat.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface AnvilOutputImage extends AnvilOutputSlot {

    ItemStack draw(Player viewer, Inventory inventory, ItemStack outputItem);

}
