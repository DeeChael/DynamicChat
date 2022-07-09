package net.deechael.dynamichat.gui.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Image extends Slot {

    ItemStack draw(Player viewer);

}
