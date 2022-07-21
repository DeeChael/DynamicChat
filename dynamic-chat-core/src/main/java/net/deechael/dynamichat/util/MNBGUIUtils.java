package net.deechael.dynamichat.util;

import net.deechael.dynamichat.DyChatPlugin;
import net.deechael.dynamichat.entity.TimeEntity;
import net.deechael.dynamichat.gui.*;
import net.deechael.dynamichat.api.Time;
import net.deechael.useless.function.parameters.TriParameter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MNBGUIUtils {

    public static void openSetTime(Player player, Player bePunished, long timeLong, TriParameter<Player, String, Long> then) {
        NormalGUI gui = new NormalGUI(DyChatPlugin.getInstance(), NormalGUI.Type.NORMAL_5X9, Lang.lang(player, "extension.mute-and-ban.gui.time.title"));
        gui.dropOnClose();
        /*
         * 0  1  2  3  4  5  6  7  8
         * 9  10 11 12 13 14 15 16 17
         * 18 19 20 21 22 23 24 25 26
         * 27 28 29 30 31 32 33 34 35
         * 36 37 38 39 40 41 42 43 44
         */
        gui.fill((Image) (viewer, inventory) -> {
            ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(" ");
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        });
        gui.setItem(22, (Image) (viewer, inventory) -> {
            ItemStack itemStack = new ItemStack(Material.CLOCK);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                Time time = new TimeEntity(timeLong);
                if (timeLong == 0) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.item.time-forever"));
                } else {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.item.time", time.getYears(), time.getMonths(), time.getWeeks(), time.getDays(), time.getHours(), time.getMinutes(), time.getSeconds()));
                }
                itemStack.setItemMeta(itemMeta);
            }
            return itemStack;
        });

        gui.setItem(12, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                if (timeLong - 1 < 0) {
                    Lang.send(viewer, "extension.mute-and-ban.gui.time.message.notlower0");
                } else {
                    viewer.closeInventory();
                    openSetTime(player, bePunished, timeLong - 1, then);
                    gui.drop();
                }
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.minus.second"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(21, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                if (timeLong - 60 < 0) {
                    Lang.send(viewer, "extension.mute-and-ban.gui.time.message.notlower0");
                } else {
                    viewer.closeInventory();
                    openSetTime(player, bePunished, timeLong - 60, then);
                    gui.drop();
                }
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                itemStack.setAmount(2);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.minus.minute"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(30, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                if (timeLong - 60 * 60 < 0) {
                    Lang.send(viewer, "extension.mute-and-ban.gui.time.message.notlower0");
                } else {
                    viewer.closeInventory();
                    openSetTime(player, bePunished, timeLong - 60 * 60, then);
                    gui.drop();
                }
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                itemStack.setAmount(4);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.minus.hour"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(11, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                if (timeLong - 24 * 60 * 60 < 0) {
                    Lang.send(viewer, "extension.mute-and-ban.gui.time.message.notlower0");
                } else {
                    viewer.closeInventory();
                    openSetTime(player, bePunished, timeLong - 24 * 60 * 60, then);
                    gui.drop();
                }
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                itemStack.setAmount(8);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.minus.day"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(20, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                if (timeLong - 7 * 24 * 60 * 60 < 0) {
                    Lang.send(viewer, "extension.mute-and-ban.gui.time.message.notlower0");
                } else {
                    viewer.closeInventory();
                    openSetTime(player, bePunished, timeLong - 7 * 24 * 60 * 60, then);
                    gui.drop();
                }
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                itemStack.setAmount(16);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.minus.week"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(29, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                if (timeLong - 30 * 24 * 60 * 60 < 0) {
                    Lang.send(viewer, "extension.mute-and-ban.gui.time.message.notlower0");
                } else {
                    viewer.closeInventory();
                    openSetTime(player, bePunished, timeLong - 30 * 24 * 60 * 60, then);
                    gui.drop();
                }
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                itemStack.setAmount(32);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.minus.month"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(19, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                if (timeLong - 365 * 24 * 60 * 60 < 0) {
                    Lang.send(viewer, "extension.mute-and-ban.gui.time.message.notlower0");
                } else {
                    viewer.closeInventory();
                    openSetTime(player, bePunished, timeLong - 365 * 24 * 60 * 60, then);
                    gui.drop();
                }
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                itemStack.setAmount(64);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.minus.year"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(14, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openSetTime(player, bePunished, timeLong + 1, then);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.plus.second"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(23, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openSetTime(player, bePunished, timeLong + 60, then);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                itemStack.setAmount(2);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.plus.minute"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(32, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openSetTime(player, bePunished, timeLong + 60 * 60, then);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                itemStack.setAmount(4);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.plus.hour"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(15, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openSetTime(player, bePunished, timeLong + 24 * 60 * 60, then);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                itemStack.setAmount(8);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.plus.day"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(24, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openSetTime(player, bePunished, timeLong + 7 * 24 * 60 * 60, then);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                itemStack.setAmount(16);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.plus.week"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(33, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openSetTime(player, bePunished, timeLong + 30 * 24 * 60 * 60, then);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                itemStack.setAmount(32);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.plus.month"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.setItem(25, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openSetTime(player, bePunished, timeLong + 365 * 24 * 60 * 60, then);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                itemStack.setAmount(64);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.plus.year"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });

        gui.setItem(13, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openSetTime(player, bePunished, 0L, then);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.BARRIER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.reset"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });

        gui.setItem(31, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openReason(player, bePunished, timeLong, then);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.DIAMOND);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.time.button.apply"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.open(player);
    }

    public static void openReason(Player player, Player bePunished, long time, TriParameter<Player, String, Long> then) {
        AnvilGUI gui = new AnvilGUI(DyChatPlugin.getInstance(), Lang.lang(player, "extension.mute-and-ban.gui.reason.title"));
        gui.dropOnClose();
        gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, (Image) (viewer, inventory) -> {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.reason.tips"));
                itemStack.setItemMeta(itemMeta);
            }
            return itemStack;
        });
        gui.setOutput(new AnvilOutputButton() {
            @Override
            public void click(Player viewer, Inventory inventory, String outputItem, ClickType type, InventoryAction action) {
                String reason = Lang.lang(viewer, "extension.mute-and-ban.default.no-reason");
                if (outputItem != null) {
                    if (!outputItem.isEmpty() && !outputItem.isBlank() && !outputItem.equalsIgnoreCase(Lang.lang(viewer, "extension.mute-and-ban.gui.reason.tips"))) {
                        reason = outputItem;
                    }
                }
                then.apply(bePunished, reason, time);
                viewer.closeInventory();
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory, String outputName) {
                ItemStack itemStack = new ItemStack(Material.DIAMOND);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "extension.mute-and-ban.gui.reason.button"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.open(player);
    }

}
