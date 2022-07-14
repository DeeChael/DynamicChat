package net.deechael.dynamichat.extension.report;

import net.deechael.dynamichat.api.*;
import net.deechael.dynamichat.extension.report.commands.CommandReport;
import net.deechael.dynamichat.gui.AnvilGUI;
import net.deechael.dynamichat.gui.AnvilOutputButton;
import net.deechael.dynamichat.gui.Image;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

public final class DyChatReportExtensionPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().addPermission(new Permission("dynamichat.report.admin", PermissionDefault.OP));
        Config.checkConfig();
        Lang.checkLanguage();
        getCommandMap().register("dynamic-chat", new CommandReport());
        ChatManager.getManager().registerButton(2, new MessageButton() {
            @Override
            public String display(CommandSender clicker, Message message) {
                Player player = (Player) clicker;
                return Lang.lang(player, "button.display");
            }

            @Override
            public void click(CommandSender clicker, Message message) {
                Player player = (Player) clicker;
                if (!player.hasPermission("dynamichat.report.admin")) {
                    if (DateUtils.delay(ReportManager.getLastReport(player.getUniqueId()), new Date()) < (Config.delay() * 60 * 1000)) {
                        Lang.send(player, "command.report.toofast");
                        return;
                    }
                }
                if (message.getSender().getSender() == player) {
                    Lang.send(player, "message.notself");
                    return;
                }
                if (ReportManager.isReported(player.getUniqueId(), message)) {
                    Lang.send(player, "message.hasreported");
                    return;
                }
                AnvilGUI gui = new AnvilGUI(DyChatReportExtensionPlugin.getInstance());
                gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, (Image) (viewer, inventory) -> {
                    ItemStack itemStack = new ItemStack(Material.PAPER);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        itemMeta.setDisplayName(Lang.lang(viewer, "gui.reason.tips"));
                        itemStack.setItemMeta(itemMeta);
                    }
                    return itemStack;
                });
                gui.setOutput(new AnvilOutputButton() {
                    @Override
                    public void click(Player viewer, Inventory inventory, String outputItem, ClickType type, InventoryAction action) {
                        if (message.getSender() instanceof PlayerUser playerUser) {
                            Player beReported = playerUser.getSender();
                            String reason = "No reason";
                            if (!outputItem.isEmpty() && !outputItem.isBlank() && !outputItem.equalsIgnoreCase(Lang.lang(viewer, "gui.reason.tips"))) {
                                reason = outputItem;
                            }
                            ReportManager.reportMessage(viewer, beReported, message.getContent(), message.getId(), reason);
                            Lang.send(viewer, "command.report.success", player.getName());
                        } else {
                            Lang.send(viewer, "command.message.cannotlocate");
                        }
                        viewer.closeInventory();
                        gui.drop();
                    }

                    @Override
                    public ItemStack draw(Player viewer, Inventory inventory, String outputName) {
                        ItemStack itemStack = new ItemStack(Material.DIAMOND);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta != null) {
                            itemMeta.setDisplayName(Lang.lang(viewer, "gui.reason.button.report.display"));
                            itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reason.button.report.lore")));
                            itemStack.setItemMeta(itemMeta);
                        }
                        return itemStack;
                    }
                });
                gui.open(player);
            }

            @Override
            public String hover(CommandSender clicker, Message message) {
                Player player = (Player) clicker;
                return Lang.lang(player, "button.hover");
            }
        });
    }

    public static DyChatReportExtensionPlugin getInstance() {
        return JavaPlugin.getPlugin(DyChatReportExtensionPlugin.class);
    }

    private static CommandMap getCommandMap() {
        try {
            return (CommandMap) Bukkit.getServer().getClass().getMethod("getCommandMap").invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
