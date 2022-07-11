package net.deechael.dynamichat.gui;

import org.bukkit.inventory.Inventory;

public interface AnvilInterface {

    void open(String title);

    Inventory castToBukkit();

    int getWindowId();

}
