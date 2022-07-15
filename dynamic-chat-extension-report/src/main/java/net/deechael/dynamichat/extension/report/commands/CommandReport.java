package net.deechael.dynamichat.extension.report.commands;

import net.deechael.dynamichat.api.Message;
import net.deechael.dynamichat.api.PlayerUser;
import net.deechael.dynamichat.entity.DynamicChatManager;
import net.deechael.dynamichat.entity.PlayerUserEntity;
import net.deechael.dynamichat.extension.report.*;
import net.deechael.dynamichat.gui.AnvilGUI;
import net.deechael.dynamichat.gui.AnvilOutputButton;
import net.deechael.dynamichat.gui.Image;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class CommandReport extends Command {

    public CommandReport() {
        super("report");
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 1) {
                list.add("help");
                list.add("report");
                list.add("reports");
                if (sender.hasPermission("dynamichat.report.admin")) {
                    list.add("admin");
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("report")) {
                    list.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList());
                    list.remove(sender.getName());
                } else {
                    list.add(" ");
                }
            } else {
                list.add(" ");
            }
        } else {
            list.add(" ");
        }
        return list;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (sender instanceof Player player) {
            /*if (args.length == 3) {
                if (args[0].equalsIgnoreCase("message")) {
                    if (args[1].equalsIgnoreCase("reaction")) {
                        Message message = DynamicChatManager.getChatCache(args[2]);
                        if (message == null) {
                            Lang.send(player, "command.message.cannotlocate");
                            return true;
                        }

                    } else {
                        Lang.send(player, "command.report.gotohelp");
                    }
                } else {
                    Lang.send(player, "command.report.gotohelp");
                }
            } else */
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("report")) {
                    Player suspect = Bukkit.getPlayer(args[1]);
                    if (args[1].equalsIgnoreCase(player.getName())) {
                        Lang.send(player, "message.notself");
                        return true;
                    }
                    if (suspect == null) {
                        Lang.send(player, "command.report.notonline", args[1]);
                    } else {
                        if (!player.hasPermission("dynamichat.report.admin")) {
                            if (DateUtils.delay(ReportManager.getLastReport(player.getUniqueId()), new Date()) < (Config.delay() * 60 * 1000)) {
                                Lang.send(player, "command.report.toofast");
                                return true;
                            }
                        }
                        if (player.getUniqueId().equals(suspect.getUniqueId())) {
                            Lang.send(player, "message.notself");
                            return true;
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
                                String reason = "No reason";
                                if (outputItem != null) {
                                    if (!outputItem.isEmpty() && !outputItem.isBlank() && !outputItem.equalsIgnoreCase(Lang.lang(viewer, "gui.reason.tips"))) {
                                        reason = outputItem;
                                    }
                                }
                                ReportManager.report(viewer, suspect, reason);
                                Lang.send(viewer, "command.report.success", suspect.getName());
                                viewer.closeInventory();
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
                } else {
                    Lang.send(player, "command.report.gotohelp");
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    Lang.sendConsole(sender, "command.report.help");
                } else if (args[0].equalsIgnoreCase("reports")) {
                    GUIUtils.openReportManagementMain(player);
                } else if (args[0].equalsIgnoreCase("admin")) {
                    if (player.hasPermission("dynamichat.report.admin")) {
                        GUIUtils.openAdminReportManagementPage(player, 1);
                    } else {
                        Lang.send(player, "command.nopermission");
                    }
                } else {
                    Lang.send(player, "command.report.gotohelp");
                }
            } else {
                Lang.send(player, "command.report.gotohelp");
            }
        } else {
            Lang.sendConsole(sender, "command.mustbeplayer");
        }
        return true;
    }

}
