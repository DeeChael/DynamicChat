package net.deechael.dynamichat.util;

import net.deechael.dynamichat.DyChatPlugin;
import net.deechael.dynamichat.gui.Button;
import net.deechael.dynamichat.gui.Image;
import net.deechael.dynamichat.gui.NormalGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class GUIUtils {

    private GUIUtils() {
    }

    public static void main(Player player) {
        NormalGUI gui = new NormalGUI(DyChatPlugin.getInstance(), NormalGUI.Type.NORMAL_3X9, Lang.lang(player, "gui.main.title"));
        gui.fill((Image) (viewer, inventory) -> {
            ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("");
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        });
        gui.setItem(10, new Button() {

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                if (viewer.isOp()) {
                    return new ItemStack(Material.DIAMOND);
                }
                return null;
            }

            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                if (viewer.isOp()) {
                    if (type.isLeftClick()) {
                        // Do
                    } else if (type.isRightClick()) {
                        // Do
                    }
                }
            }
        });
        gui.open(player);
    }

}
