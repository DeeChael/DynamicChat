package net.deechael.dynamichat.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface PageImage extends PageSlot {

    ItemStack draw(Player viewer, Inventory inventory);
}
