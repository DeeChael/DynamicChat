package net.deechael.dynamichat.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface AnvilInterface {

    void open(Player player);

    Inventory castToBukkit();

    int getWindowId();

}
