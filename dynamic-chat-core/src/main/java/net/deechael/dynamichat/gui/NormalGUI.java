package net.deechael.dynamichat.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class NormalGUI {

    private final Plugin plugin;

    private final Map<Player, Inventory> cache = new HashMap<>();
    private final Map<Integer, Slot> inputs = new HashMap<>();

    private boolean dropped = false;

    public NormalGUI(Plugin plugin) {
        this.plugin = plugin;
    }

}
