package net.deechael.dynamichat.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NormalGUI implements Listener {

    private final Plugin plugin;
    private final Map<Player, Inventory> cache = new HashMap<>();
    private final Map<Integer, Slot> inputs = new HashMap<>();
    private final Type type;
    private final String title;
    private boolean dropped = false;

    public NormalGUI(Plugin plugin, Type type) {
        this(plugin, type, "GUI");
    }

    public NormalGUI(Plugin plugin, Type type, String title) {
        this.plugin = plugin;
        this.type = type;
        this.title = title;
    }

    public boolean hasItem(int slot) {
        if (isDropped()) return false;
        return inputs.containsKey(slot);
    }

    public Slot getItem(int slot) {
        if (isDropped()) throw new RuntimeException("This GUI has been dropped!");
        if (!hasItem(slot)) throw new RuntimeException("The slot is empty");
        return inputs.get(slot);
    }

    public void fill(Slot input) {
        for (int i = 0; i < this.type.getSlots(); i++) {
            setItem(i, input);
        }
    }

    public void setItem(int slot, Slot input) {
        if (isDropped()) return;
        inputs.put(slot, input);
        for (Map.Entry<Player, Inventory> entry : cache.entrySet()) {
            if (input instanceof Image image) {
                entry.getValue().setItem(slot, image.draw(entry.getKey(), entry.getValue()));
            }
        }
    }

    public Slot remove(int slot) {
        if (isDropped()) throw new RuntimeException("The GUI has been dropped!");
        return inputs.remove(slot);
    }

    public Inventory getBukkit(Player player) {
        if (isDropped() || !cache.containsKey(player))
            throw new RuntimeException("The gui which player is watching is not this gui!");
        return cache.get(player);
    }


    public void open(Player player) {
        if (isDropped()) return;
        Inventory inventory;
        if (title != null) {
            inventory = Bukkit.createInventory(null, this.type.getSlots(), title);
        } else {
            inventory = Bukkit.createInventory(null, this.type.getSlots());
        }
        for (int i : inputs.keySet()) {
            Slot input = inputs.get(i);
            if (input instanceof Image) {
                inventory.setItem(i, ((Image) input).draw(player, inventory));
            }
        }
        player.openInventory(inventory);
        cache.put(player, inventory);
    }

    public boolean isDropped() {
        return this.dropped;
    }

    public void drop() {
        if (isDropped()) return;
        cache.keySet().forEach(Player::closeInventory);
        HandlerList.unregisterAll(this);
        dropped = true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (isDropped()) return;
        if (event.getWhoClicked() instanceof Player player) {
            if (cache.containsKey(player)) {
                if (event.getView().getTopInventory().equals(cache.get(player))) {
                    if (Objects.equals(event.getClickedInventory(), event.getView().getTopInventory())) {
                        event.setCancelled(true);
                    } else {
                        if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                            event.setCancelled(true);
                        }
                    }
                    if (event.getRawSlot() >= 0 && event.getRawSlot() <= type.getSlots()) {
                        if (inputs.containsKey(event.getRawSlot())) {
                            event.setCancelled(true);
                            Slot input = inputs.get(event.getRawSlot());
                            if (input instanceof Clicker clicker) {
                                clicker.click(player, event.getView().getTopInventory(), event.getClick(), event.getAction());
                            } else if (input instanceof Storage storage) {
                                ItemStack cursor = event.getCursor();
                                if (cursor == null)
                                    cursor = new ItemStack(Material.AIR);
                                ItemStack storageItem = storage.getStored(player);
                                if (cursor.getType() == Material.AIR) {
                                    storage.setStored(player, null);
                                    event.getView().setCursor(storageItem);
                                    event.getClickedInventory().setItem(event.getRawSlot(), null);
                                } else {
                                    if (storage.isAllow(player, cursor)) {
                                        storage.setStored(player, cursor);
                                        event.getView().setCursor(storageItem);
                                        event.getClickedInventory().setItem(event.getRawSlot(), cursor);
                                    } else {
                                        event.getView().setCursor(cursor);
                                        event.getClickedInventory().setItem(event.getRawSlot(), storageItem);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (isDropped()) return;
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (cache.containsKey(player)) {
                if (event.getView().getTopInventory().equals(cache.get(player))) {
                    cache.remove(player);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (isDropped()) return;
        cache.remove(event.getPlayer());
    }

    public enum Type {

        NORMAL_1X9(1),
        NORMAL_2X9(2),
        NORMAL_3X9(3),
        NORMAL_4X9(4),
        NORMAL_5X9(5),
        NORMAL_6X9(6);

        private final int lines;

        Type(int lines) {
            this.lines = lines;
        }

        public int getLines() {
            return lines;
        }

        public int getSlots() {
            return lines * 9;
        }

    }

}
