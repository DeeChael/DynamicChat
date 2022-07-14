package net.deechael.dynamichat.extension.report;

import net.deechael.dynamichat.gui.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class GUIUtils {

    private final static List<Integer> itemSlot = List.of(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    );

    private final static List<Integer> outline = List.of(
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53
    );

    private final static Image OUTLINE_IMAGE = new Image() {
        @Override
        public ItemStack draw(Player viewer, Inventory inventory) {
            ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(" ");
                itemStack.setItemMeta(itemMeta);
            }
            return itemStack;
        }
    };

    public static void openReportManagementMain(Player player) {
        openReportManagementPage(player, 1);
    }

    public static void openReportManagementPage(Player player, int page) {
        List<Report> reports = ReportManager.getOnesReports(player);
        int pages = reports.size() % 28 == 0 ? reports.size() / 28 : reports.size() / 28 + 1;
        if (page < 1)
            page = 1;
        if (page > pages)
            page = pages;
        int finalPage = page;
        NormalGUI gui = new NormalGUI(DyChatReportExtensionPlugin.getInstance(), NormalGUI.Type.NORMAL_6X9, Lang.lang(player, "gui.reports.title", page, pages));
        outline.forEach(i -> gui.setItem(i, OUTLINE_IMAGE));
        if (reports.size() <= 28) {
            for (int i = 0; i < reports.size(); i++) {
                Report report = reports.get(i);
                gui.setItem(itemSlot.get(i), new Button() {
                    @Override
                    public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                        viewer.closeInventory();
                        openSingleReport(player, finalPage, report);
                        gui.drop();
                    }

                    @Override
                    public ItemStack draw(Player viewer, Inventory inventory) {
                        ItemStack itemStack = new ItemStack(Material.PAPER);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta != null) {
                            if (report.isApproached()) {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.approved.prefix", report.getCreateDateString()));
                                itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.item.lore", report.getSuspectName(), Lang.lang(viewer, "gui.reports.item.approved.prefix", Lang.lang(viewer, "gui.reports.item.name.approved.text"))).split("\\n")));
                            } else {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.unapproved.prefix", report.getCreateDateString()));
                                itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.item.lore", report.getSuspectName(), Lang.lang(viewer, "gui.reports.item.unapproved.prefix", Lang.lang(viewer, "gui.reports.item.name.unapproved.text"))).split("\\n")));
                            }
                            itemStack.setItemMeta(itemMeta);
                        }
                        return itemStack;
                    }
                });
            }
        } else {
            for (int i = 28 * (page - 1); i < Math.min(28 * (page), reports.size()); i++) {
                Report report = reports.get(i);
                gui.setItem(itemSlot.get(i - (28 * (page - 1))), new Button() {
                    @Override
                    public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                        viewer.closeInventory();;
                        openSingleReport(player, finalPage, report);
                        gui.drop();
                    }

                    @Override
                    public ItemStack draw(Player viewer, Inventory inventory) {
                        ItemStack itemStack = new ItemStack(Material.PAPER);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta != null) {
                            if (report.isApproached()) {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.approved.prefix", report.getCreateDateString()));
                                itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.item.lore", report.getSuspectName(), Lang.lang(viewer, "gui.reports.item.approved.prefix", Lang.lang(viewer, "gui.reports.item.name.approved.text"))).split("\\n")));
                            } else {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.unapproved.prefix", report.getCreateDateString()));
                                itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.item.lore", report.getSuspectName(), Lang.lang(viewer, "gui.reports.item.unapproved.prefix", Lang.lang(viewer, "gui.reports.item.name.unapproved.text"))).split("\\n")));
                            }
                            itemStack.setItemMeta(itemMeta);
                        }
                        return itemStack;
                    }
                });
            }
            if (page == 1) {
                if (pages > 1) {
                    gui.setItem(53, new Button() {
                        @Override
                        public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                            viewer.closeInventory();
                            openReportManagementPage(viewer, finalPage + 1);
                            gui.drop();
                        }

                        @Override
                        public ItemStack draw(Player viewer, Inventory inventory) {
                            ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if (itemMeta != null) {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.next"));
                                itemStack.setItemMeta(itemMeta);
                            }
                            return itemStack;
                        }
                    });
                }
            } else if (page == pages) {
                if (pages > 1) {
                    gui.setItem(45, new Button() {
                        @Override
                        public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                            viewer.closeInventory();
                            openReportManagementPage(viewer, finalPage - 1);
                            gui.drop();
                        }

                        @Override
                        public ItemStack draw(Player viewer, Inventory inventory) {
                            ItemStack itemStack = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if (itemMeta != null) {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.previous"));
                                itemStack.setItemMeta(itemMeta);
                            }
                            return itemStack;
                        }
                    });
                }
            } else {
                gui.setItem(45, new Button() {
                    @Override
                    public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                        viewer.closeInventory();
                        openReportManagementPage(viewer, finalPage - 1);
                        gui.drop();
                    }

                    @Override
                    public ItemStack draw(Player viewer, Inventory inventory) {
                        ItemStack itemStack = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta != null) {
                            itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.previous"));
                            itemStack.setItemMeta(itemMeta);
                        }
                        return itemStack;
                    }
                });
                gui.setItem(53, new Button() {
                    @Override
                    public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                        viewer.closeInventory();
                        openReportManagementPage(viewer, finalPage + 1);
                        gui.drop();
                    }

                    @Override
                    public ItemStack draw(Player viewer, Inventory inventory) {
                        ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta != null) {
                            itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.next"));
                            itemStack.setItemMeta(itemMeta);
                        }
                        return itemStack;
                    }
                });
            }
        }
        gui.open(player);
    }

    public static void openSingleReport(Player player, int backPage, Report report) {
        NormalGUI gui = new NormalGUI(DyChatReportExtensionPlugin.getInstance(), NormalGUI.Type.NORMAL_3X9, Lang.lang(player, "gui.reports.report.title", Lang.lang(player, report.isApproached() ? "gui.reports.item.approved.prefix" : "gui.reports.item.unapproved.prefix", report.getCreateDateString())));
        gui.fill(OUTLINE_IMAGE);
        gui.setItem(0, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openReportManagementPage(player, backPage);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.back"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        if (report instanceof MessageReport messageReport) {
            gui.setItem(10, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    OfflinePlayer suspectPlayer = Bukkit.getOfflinePlayer(report.getSuspectUUID());
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.suspect.display", report.getSuspectName()));
                    itemMeta.setLore(List.of(Lang.lang(player, "gui.reports.report.item.suspect.lore", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suspectPlayer.getLastPlayed())))));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(12, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.PAPER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.message.display"));
                    itemMeta.setLore(List.of(("§r" + ColorUtils.processChatColor(ColorUtils.processGradientColor(messageReport.getMessage()))).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(14, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.NAME_TAG);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.reason.display"));
                    itemMeta.setLore(List.of(("§r" + report.getReason()).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(16, (Image) (viewer, inventory) -> {
                ItemStack itemStack;
                if (report.isApproached()) {
                    itemStack = new ItemStack(Material.LIME_DYE);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.approved.prefix", Lang.lang(viewer, "gui.reports.item.name.approved.text")));
                        itemMeta.setLore(List.of(Lang.lang(viewer,
                                "gui.reports.report.item.approved.lore",
                                report.getAdmin(),
                                report.getSolveDateString(),
                                report.getAdminReply()
                        ).split("\\n")));
                        itemStack.setItemMeta(itemMeta);
                    }
                } else {
                    itemStack = new ItemStack(Material.RED_DYE);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.unapproved.prefix", Lang.lang(viewer, "gui.reports.item.name.unapproved.text")));
                        itemStack.setItemMeta(itemMeta);
                    }
                }
                return itemStack;
            });
        } else {
            gui.setItem(10, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    OfflinePlayer suspectPlayer = Bukkit.getOfflinePlayer(report.getSuspectUUID());
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.suspect.display", report.getSuspectName()));
                    itemMeta.setLore(List.of(Lang.lang(player, "gui.reports.report.item.suspect.lore", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suspectPlayer.getLastPlayed())))));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(13, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.NAME_TAG);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.reason.display"));
                    itemMeta.setLore(List.of(("§r" + report.getReason()).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(16, (Image) (viewer, inventory) -> {
                ItemStack itemStack;
                if (report.isApproached()) {
                    itemStack = new ItemStack(Material.LIME_DYE);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.approved.prefix", Lang.lang(viewer, "gui.reports.item.name.approved.text")));
                        itemMeta.setLore(List.of(Lang.lang(viewer,
                                "gui.reports.report.item.approved.lore",
                                report.getAdmin(),
                                report.getSolveDateString(),
                                report.getAdminReply()
                        ).split("\\n")));
                        itemStack.setItemMeta(itemMeta);
                    }
                } else {
                    itemStack = new ItemStack(Material.RED_DYE);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.unapproved.prefix", Lang.lang(viewer, "gui.reports.item.name.unapproved.text")));
                        itemStack.setItemMeta(itemMeta);
                    }
                }
                return itemStack;
            });
        }
        gui.open(player);
    }

    public static void openAdminReportManagementPage(Player player, int page) {
        List<Report> reports = ReportManager.getReports();
        int pages = reports.size() % 28 == 0 ? reports.size() / 28 : reports.size() / 28 + 1;
        if (page < 1)
            page = 1;
        if (page > pages)
            page = pages;
        int finalPage = page;
        NormalGUI gui = new NormalGUI(DyChatReportExtensionPlugin.getInstance(), NormalGUI.Type.NORMAL_6X9, Lang.lang(player, "gui.admin.title", page, pages));
        outline.forEach(i -> gui.setItem(i, OUTLINE_IMAGE));
        if (reports.size() <= 28) {
            for (int i = 0; i < reports.size(); i++) {
                Report report = reports.get(i);
                gui.setItem(itemSlot.get(i), new Button() {
                    @Override
                    public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                        viewer.closeInventory();
                        if (report.isApproached()) {
                            openAdminSingleReportSolved(player, finalPage, report);
                        } else {
                            openAdminSingleReportUnsolved(player, finalPage, report);
                        }
                        gui.drop();
                    }

                    @Override
                    public ItemStack draw(Player viewer, Inventory inventory) {
                        ItemStack itemStack = new ItemStack(Material.PAPER);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta != null) {
                            if (report.isApproached()) {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.approved.prefix", report.getCreateDateString()));
                                itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.item.admin.lore", report.getReporterName(), report.getSuspectName(), Lang.lang(viewer, "gui.reports.item.approved.prefix", Lang.lang(viewer, "gui.reports.item.name.approved.text"))).split("\\n")));
                            } else {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.unapproved.prefix", report.getCreateDateString()));
                                itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.item.admin.lore", report.getReporterName(), report.getSuspectName(), Lang.lang(viewer, "gui.reports.item.unapproved.prefix", Lang.lang(viewer, "gui.reports.item.name.unapproved.text"))).split("\\n")));
                            }
                            itemStack.setItemMeta(itemMeta);
                        }
                        return itemStack;
                    }
                });
            }
        } else {
            for (int i = 28 * (finalPage - 1); i < Math.min(28 * (finalPage), reports.size()); i++) {
                Report report = reports.get(i);
                gui.setItem(itemSlot.get(i - (28 * (finalPage - 1))), new Button() {
                    @Override
                    public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                        viewer.closeInventory();
                        if (report.isApproached()) {
                            openAdminSingleReportSolved(player, finalPage, report);
                        } else {
                            openAdminSingleReportUnsolved(player, finalPage, report);
                        }
                        gui.drop();
                    }

                    @Override
                    public ItemStack draw(Player viewer, Inventory inventory) {
                        ItemStack itemStack = new ItemStack(Material.PAPER);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta != null) {
                            if (report.isApproached()) {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.approved.prefix", report.getCreateDateString()));
                                itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.item.admin.lore", report.getReporterName(), report.getSuspectName(), Lang.lang(viewer, "gui.reports.item.approved.prefix", Lang.lang(viewer, "gui.reports.item.name.approved.text"))).split("\\n")));
                            } else {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.unapproved.prefix", report.getCreateDateString()));
                                itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.item.admin.lore", report.getReporterName(), report.getSuspectName(), Lang.lang(viewer, "gui.reports.item.unapproved.prefix", Lang.lang(viewer, "gui.reports.item.name.unapproved.text"))).split("\\n")));
                            }
                            itemStack.setItemMeta(itemMeta);
                        }
                        return itemStack;
                    }
                });
            }
            if (page == 1) {
                if (pages > 1) {
                    gui.setItem(53, new Button() {
                        @Override
                        public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                            viewer.closeInventory();
                            openAdminReportManagementPage(viewer, finalPage + 1);
                            gui.drop();
                        }

                        @Override
                        public ItemStack draw(Player viewer, Inventory inventory) {
                            ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if (itemMeta != null) {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.next"));
                                itemStack.setItemMeta(itemMeta);
                            }
                            return itemStack;
                        }
                    });
                }
            } else if (page == pages) {
                if (pages > 1) {
                    gui.setItem(45, new Button() {
                        @Override
                        public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                            viewer.closeInventory();
                            openAdminReportManagementPage(viewer, finalPage - 1);
                            gui.drop();
                        }

                        @Override
                        public ItemStack draw(Player viewer, Inventory inventory) {
                            ItemStack itemStack = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            if (itemMeta != null) {
                                itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.previous"));
                                itemStack.setItemMeta(itemMeta);
                            }
                            return itemStack;
                        }
                    });
                }
            } else {
                gui.setItem(45, new Button() {
                    @Override
                    public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                        viewer.closeInventory();
                        openAdminReportManagementPage(viewer, finalPage - 1);
                        gui.drop();
                    }

                    @Override
                    public ItemStack draw(Player viewer, Inventory inventory) {
                        ItemStack itemStack = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta != null) {
                            itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.previous"));
                            itemStack.setItemMeta(itemMeta);
                        }
                        return itemStack;
                    }
                });
                gui.setItem(53, new Button() {
                    @Override
                    public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                        viewer.closeInventory();
                        openAdminReportManagementPage(viewer, finalPage + 1);
                        gui.drop();
                    }

                    @Override
                    public ItemStack draw(Player viewer, Inventory inventory) {
                        ItemStack itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        if (itemMeta != null) {
                            itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.next"));
                            itemStack.setItemMeta(itemMeta);
                        }
                        return itemStack;
                    }
                });
            }
        }
        gui.open(player);
    }

    public static void openAdminSingleReportUnsolved(Player player, int backPage, Report report) {
        NormalGUI gui = new NormalGUI(DyChatReportExtensionPlugin.getInstance(), NormalGUI.Type.NORMAL_5X9, Lang.lang(player, "gui.reports.report.admin.title", report.getReporterName(), Lang.lang(player, report.isApproached() ? "gui.reports.item.approved.prefix" : "gui.reports.item.unapproved.prefix", report.getCreateDateString())));
        gui.fill(OUTLINE_IMAGE);
        gui.setItem(0, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openAdminReportManagementPage(player, backPage);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.back"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        if (report instanceof MessageReport messageReport) {
            gui.setItem(10, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    OfflinePlayer suspectPlayer = Bukkit.getOfflinePlayer(report.getSuspectUUID());
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.suspect.display", report.getSuspectName()));
                    itemMeta.setLore(List.of(Lang.lang(player, "gui.reports.report.item.suspect.lore", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suspectPlayer.getLastPlayed())))));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(12, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.PAPER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.message.display"));
                    itemMeta.setLore(List.of(("§r" + ColorUtils.processChatColor(ColorUtils.processGradientColor(messageReport.getMessage()))).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(14, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.NAME_TAG);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.reason.display"));
                    itemMeta.setLore(List.of(("§r" + report.getReason()).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(16, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.RED_DYE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.unapproved.prefix", Lang.lang(viewer, "gui.reports.item.name.unapproved.text")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
        } else {
            gui.setItem(10, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    OfflinePlayer suspectPlayer = Bukkit.getOfflinePlayer(report.getSuspectUUID());
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.suspect.display", report.getSuspectName()));
                    itemMeta.setLore(List.of(Lang.lang(player, "gui.reports.report.item.suspect.lore", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suspectPlayer.getLastPlayed())))));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(13, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.NAME_TAG);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.reason.display"));
                    itemMeta.setLore(List.of(("§r" + report.getReason()).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(16, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.RED_DYE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.unapproved.prefix", Lang.lang(viewer, "gui.reports.item.name.unapproved.text")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
        }
        //27 28 29 30 31 32 33 34 35
        gui.setItem(31, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                approveReport(player, backPage, report);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.DIAMOND);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.admin.button.solve.display"));
                    itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.admin.button.solve.lore")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.open(player);
    }

    public static void openAdminSingleReportSolved(Player player, int backPage, Report report) {
        NormalGUI gui = new NormalGUI(DyChatReportExtensionPlugin.getInstance(), NormalGUI.Type.NORMAL_3X9, Lang.lang(player, "gui.reports.report.admin.title", report.getReporterName(), Lang.lang(player, report.isApproached() ? "gui.reports.item.approved.prefix" : "gui.reports.item.unapproved.prefix", report.getCreateDateString())));
        gui.fill(OUTLINE_IMAGE);
        gui.setItem(0, new Button() {
            @Override
            public void click(Player viewer, Inventory inventory, ClickType type, InventoryAction action) {
                viewer.closeInventory();
                openAdminReportManagementPage(player, backPage);
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory) {
                ItemStack itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.button.back"));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        if (report instanceof MessageReport messageReport) {
            gui.setItem(10, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    OfflinePlayer suspectPlayer = Bukkit.getOfflinePlayer(report.getSuspectUUID());
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.suspect.display", report.getSuspectName()));
                    itemMeta.setLore(List.of(Lang.lang(player, "gui.reports.report.item.suspect.lore", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suspectPlayer.getLastPlayed())))));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(12, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.PAPER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.message.display"));
                    itemMeta.setLore(List.of(("§r" + ColorUtils.processChatColor(ColorUtils.processGradientColor(messageReport.getMessage()))).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(14, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.NAME_TAG);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.reason.display"));
                    itemMeta.setLore(List.of(("§r" + report.getReason()).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(16, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.LIME_DYE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.approved.prefix", Lang.lang(viewer, "gui.reports.item.name.approved.text")));
                    itemMeta.setLore(List.of(Lang.lang(viewer,
                            "gui.reports.report.item.approved.lore",
                            report.getAdmin(),
                            report.getSolveDateString(),
                            report.getAdminReply()
                    ).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
        } else {
            gui.setItem(10, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    OfflinePlayer suspectPlayer = Bukkit.getOfflinePlayer(report.getSuspectUUID());
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.suspect.display", report.getSuspectName()));
                    itemMeta.setLore(List.of(Lang.lang(player, "gui.reports.report.item.suspect.lore", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suspectPlayer.getLastPlayed())))));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(13, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.NAME_TAG);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.report.item.reason.display"));
                    itemMeta.setLore(List.of(("§r" + report.getReason()).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
            gui.setItem(16, (Image) (viewer, inventory) -> {
                ItemStack itemStack = new ItemStack(Material.LIME_DYE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.item.approved.prefix", Lang.lang(viewer, "gui.reports.item.name.approved.text")));
                    itemMeta.setLore(List.of(Lang.lang(viewer,
                            "gui.reports.report.item.approved.lore",
                            report.getAdmin(),
                            report.getSolveDateString(),
                            report.getAdminReply()
                    ).split("\\n")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            });
        }
        gui.open(player);
    }

    public static void approveReport(Player player, int page, Report report) {
        AnvilGUI gui = new AnvilGUI(DyChatReportExtensionPlugin.getInstance());
        gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, (Image) (viewer, inventory) -> {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(Lang.lang(viewer, "gui.comment.tips"));
                itemStack.setItemMeta(itemMeta);
            }
            return itemStack;
        });
        gui.setOutput(new AnvilOutputButton() {
            @Override
            public void click(Player viewer, Inventory inventory, String outputItem, ClickType type, InventoryAction action) {
                String comment = "No comment";
                if (outputItem != null) {
                    if (!outputItem.isEmpty() && !outputItem.isBlank() && !outputItem.equalsIgnoreCase(Lang.lang(viewer, "gui.comment.tips"))) {
                        comment = outputItem;
                    }
                }
                report.setAdmin(player.getName());
                report.setAdminReply(comment);
                report.setSolveDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                report.setApproached(true);
                ReportManager.solveReport(report);
                Lang.send(viewer, "message.approve.approved");
                openAdminReportManagementPage(player, page);
                viewer.closeInventory();
                gui.drop();
            }

            @Override
            public ItemStack draw(Player viewer, Inventory inventory, String outputName) {
                ItemStack itemStack = new ItemStack(Material.DIAMOND);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(Lang.lang(viewer, "gui.reports.admin.button.solve.display"));
                    itemMeta.setLore(List.of(Lang.lang(viewer, "gui.reports.admin.button.solve.lore")));
                    itemStack.setItemMeta(itemMeta);
                }
                return itemStack;
            }
        });
        gui.open(player);
    }

}
