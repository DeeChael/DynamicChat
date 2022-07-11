package net.deechael.dynamichat.gui;

import net.deechael.dcg.*;
import net.deechael.dcg.generator.JGenerator;
import net.deechael.dcg.items.Var;
import net.deechael.dynamichat.util.Ref;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class AnvilGUI implements Listener {

    private static Class<?> anvilClass = null;
    private final Plugin plugin;
    private final Map<Player, Inventory> cache = new HashMap<>();
    private final Map<Integer, Slot> inputs = new HashMap<>();
    private final String title;
    private boolean dropped = false;

    public AnvilGUI(Plugin plugin) {
        this(plugin, "Anvil");
    }

    public AnvilGUI(Plugin plugin, String title) {
        if (anvilClass == null) {
            anvilClass = generateAnvilClass();
        }
        this.plugin = plugin;
        this.title = title;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public boolean hasItem(AnvilSlot slot) {
        if (isDropped()) return false;
        return inputs.containsKey(slot.getSlot());
    }

    public Slot getItem(AnvilSlot slot) {
        if (isDropped()) throw new RuntimeException("This GUI has been dropped!");
        if (!hasItem(slot)) throw new RuntimeException("The slot is empty");
        return inputs.get(slot.getSlot());
    }

    public void setItem(AnvilSlot slot, Slot input) {
        if (isDropped()) return;
        inputs.put(slot.getSlot(), input);
    }

    public Slot remove(AnvilSlot slot) {
        if (isDropped()) throw new RuntimeException("The GUI has been dropped!");
        return inputs.remove(slot.getSlot());
    }

    public Inventory getBukkit(Player player) {
        if (isDropped() || !cache.containsKey(player))
            throw new RuntimeException("The gui which player is watching is not this gui!");
        return cache.get(player);
    }

    public void open(Player player) {
        if (isDropped()) return;
        Class<?> clazz = anvilClass;
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor(Player.class);
            constructor.setAccessible(true);
            AnvilInterface anvil = (AnvilInterface) constructor.newInstance(player);
            Inventory inventory = anvil.castToBukkit();
            for (int i : inputs.keySet()) {
                Slot input = inputs.get(i);
                if (input instanceof Image) {
                    inventory.setItem(i, ((Image) input).draw(player));
                }
            }
            anvil.open(title);
            cache.put(player, inventory);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
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
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (cache.containsKey(player)) {
                if (event.getView().getTopInventory().equals(cache.get(player))) {
                    if (Objects.equals(event.getClickedInventory(), event.getView().getTopInventory())) {
                        event.setCancelled(true);
                    } else {
                        if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                            event.setCancelled(true);
                        }
                    }
                    if (event.getRawSlot() >= 0 && event.getRawSlot() <= 2) {
                        if (inputs.containsKey(event.getRawSlot())) {
                            event.setCancelled(true);
                            Slot input = inputs.get(event.getRawSlot());
                            if (input instanceof Clicker clicker) {
                                clicker.click(player, event.getClick(), event.getAction());
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

    ///////////////////////////////////////////////////////////
    //////                Player Listener                //////
    ///////////////////////////////////////////////////////////

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

    private Class<?> generateAnvilClass() {
        JClass anvil = new JClass(Level.PUBLIC, "net.deechael.dynamichat.gui", "NMSAnvil");
        anvil.importClass(AnvilInterface.class);
        anvil.importClass(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutOpenWindow", "PacketPlayOutOpenWindow"));
        anvil.importClass(Ref.getNmsOrOld("world.inventory.Containers", "Containers"));
        anvil.importClass(Ref.getNmsOrOld("world.inventory.ContainerAnvil", "ContainerAnvil"));
        anvil.importClass(Ref.getNmsOrOld("world.inventory.ContainerAccess", "ContainerAccess"));
        anvil.importClass(Ref.getNmsOrOld("world.IInventory", "IInventory"));
        anvil.importClass(Ref.getNmsOrOld("world.level.World", "World"));
        anvil.importClass(Ref.getNmsOrOld("world.entity.player.EntityHuman", "EntityHuman"));
        anvil.importClass(Ref.getNmsOrOld("core.BlockPosition", "BlockPosition"));
        if (Ref.getVersion() >= 19) {
            anvil.importClass(Ref.getNmsOrOld("network.chat.IChatBaseComponent", ""));
        } else {
            anvil.importClass(Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage"));
        }
        anvil.importClass(Ref.getObcClass("entity.CraftPlayer"));
        anvil.importClass(Player.class);
        anvil.importClass(Inventory.class);
        anvil.importClass(ItemStack.class);

        anvil.extend(JType.classType(Ref.getNmsOrOld("world.inventory.ContainerAnvil", "ContainerAnvil")));

        anvil.implement(JType.classType(AnvilInterface.class));

        Var int0 = JStringVar.intVar(0);

        JField bukkitPlayer = anvil.addField(Level.PRIVATE, JType.classType(Player.class), "bukkitPlayer", true, false);

        JConstructor constructor = anvil.addConstructor(Level.PUBLIC);
        Var player = constructor.addParameter(JType.classType(Player.class), "player");
        if (Ref.getVersion() <= 10) {
            Var craftPlayer = Var.castObject(player, JType.classType(Ref.getObcClass("entity.CraftPlayer")));
            Var entityPlayer = Var.invokeMethod(craftPlayer, "getHandle");
            Var inventory = Var.objectsField(entityPlayer, "inventory");
            Var world = Var.objectsField(entityPlayer, "world");
            Var blockPosition = Var.constructor(JType.classType(Ref.getNmsOrOld("core.BlockPosition", "BlockPosition")), int0, int0, int0);
            constructor.superConstructor(inventory, world, blockPosition, entityPlayer);
        } else {
            Var craftPlayer = Var.castObject(player, JType.classType(Ref.getObcClass("entity.CraftPlayer")));
            Var entityPlayer = Var.invokeMethod(craftPlayer, "getHandle");
            Var nextContainerCounter = Var.invokeMethod(entityPlayer, "nextContainerCounter");
            Var inventory;
            if (Ref.getVersion() >= 19) {
                inventory = Var.invokeMethod(entityPlayer, "fB");
            } else if (Ref.getVersion() == 18) {
                inventory = Var.invokeMethod(entityPlayer, "fr");
            } else if (Ref.getVersion() == 17) {
                inventory = Var.invokeMethod(entityPlayer, "fq");
            } else if (Ref.getVersion() == 16) {
                inventory = Var.invokeMethod(entityPlayer, "getInventory");
            } else {
                inventory = Var.objectsField(entityPlayer, "inventory");
            }
            Var blockPosition = Var.constructor(JType.classType(Ref.getNmsOrOld("core.BlockPosition", "BlockPosition")), int0, int0, int0);
            Var containerAccess;
            if (Ref.getVersion() >= 17) {
                containerAccess = Var.invokeMethod(JType.classType(Ref.getNmsOrOld("world.inventory.ContainerAccess", "ContainerAccess")), "a", Var.invokeMethod(Var.castObject(Var.invokeMethod(craftPlayer, "getWorld"), JType.classType(Ref.getObcClass("CraftWorld"))), "getHandle"), blockPosition);
            } else {
                containerAccess = Var.invokeMethod(JType.classType(Ref.getNmsOrOld("world.inventory.ContainerAccess", "ContainerAccess")), "at", Var.invokeMethod(Var.castObject(Var.invokeMethod(craftPlayer, "getWorld"), JType.classType(Ref.getObcClass("CraftWorld"))), "getHandle"), blockPosition);
            }
            constructor.superConstructor(nextContainerCounter, inventory, containerAccess);
        }

        constructor.setThisFieldValue(anvil.getField("checkReachable"), JStringVar.booleanVar(false));
        constructor.setThisFieldValue(bukkitPlayer, player);

        String resetCost_name;
        if (Ref.getVersion() >= 17) {
            resetCost_name = "l";
        } else if (Ref.getVersion() == 16) {
            resetCost_name = "i";
        } else if (Ref.getVersion() >= 11 && Ref.getVersion() <= 15) {
            resetCost_name = "e";
        } else {
            resetCost_name = "d";
        }
        JMethod costMethod = anvil.addMethod(Level.PUBLIC, resetCost_name, false, false, false);
        costMethod.invokeSuperMethod(resetCost_name);
        if (Ref.getVersion() >= 12) {
            if (Ref.getVersion() >= 17) {
                costMethod.invokeMethod(anvil.getField("w"), "a", int0);
            } else if (Ref.getVersion() == 16) {
                costMethod.invokeMethod(anvil.getField("w"), "set", int0);
            } else {
                costMethod.invokeMethod(anvil.getField("levelCost"), "set", int0);
            }
        } else if (Ref.getVersion() == 11) {
            costMethod.invokeMethod(anvil.getField("levelCost"), "a", int0);
        } else {
            costMethod.setThisFieldValue(anvil.getField("levelCost"), int0);
        }

        anvil.addMethod(Level.PUBLIC, "b", false, false, false).addParameter(JType.classType(Ref.getNmsOrOld("world.entity.player.EntityHuman", "EntityHuman")), "entityHuman");

        JMethod aMethod = anvil.addMethod(Level.PROTECTED, "a", false, false, false);
        aMethod.addParameter(JType.classType(Ref.getNmsOrOld("world.entity.player.EntityHuman", "EntityHuman")), "entityHuman");
        aMethod.addParameter(JType.classType(Ref.getNmsOrOld("world.IInventory", "IInventory")), "iInventory");

        JMethod getWindowId = anvil.addMethod(JType.INT, Level.PUBLIC, "getWindowId", false, false, false);
        if (Ref.getVersion() >= 16) {
            getWindowId.returnValue(anvil.getField("j"));
        } else {
            getWindowId.returnValue(anvil.getField("windowId"));
        }

        JMethod castToBukkit = anvil.addMethod(JType.classType(Inventory.class), Level.PUBLIC, "castToBukkit", false, false, false);
        castToBukkit.returnValue(Var.invokeMethod(Var.invokeThisMethod("getBukkitView"), "getTopInventory"));

        JMethod openToPlayer = anvil.addMethod(Level.PUBLIC, "open", false, false, false);

        Var titleString = openToPlayer.addParameter(JType.STRING, "title");
        Var packet;
        JType packetClass = JType.classType(Ref.getNmsOrOld("network.protocol.game.PacketPlayOutOpenWindow", "PacketPlayOutOpenWindow"));
        Var title;
        if (Ref.getVersion() >= 19) {
            title = Var.invokeMethod(JType.classType(Ref.getNmsOrOld("network.chat.IChatBaseComponent", "")), "b", titleString);
        } else {
            title = Var.constructor(JType.classType(Ref.getNmsOrOld("network.chat.ChatMessage", "ChatMessage")), titleString);
        }
        if (Ref.getVersion() >= 16) {
            packet = openToPlayer.createVar(packetClass, "packet", Var.constructor(packetClass, Var.invokeThisMethod("getWindowId"), Var.staticField(Ref.getNmsOrOld("world.inventory.Containers", "Containers"), "h"), title));
        } else if (Ref.getVersion() <= 10) {
            packet = openToPlayer.createVar(packetClass, "packet", Var.constructor(packetClass, Var.invokeThisMethod("getWindowId"), JStringVar.stringVar("minecraft:anvil"), title));
        } else {
            packet = openToPlayer.createVar(packetClass, "packet", Var.constructor(packetClass, Var.invokeThisMethod("getWindowId"), Var.staticField(Ref.getNmsOrOld("world.inventory.Containers", "Containers"), "ANVIL"), title));
        }

        String playerConnectionName;
        if (Ref.getVersion() >= 16) {
            playerConnectionName = "b";
        } else {
            playerConnectionName = "playerConnection";
        }
        Var entityPlayer = Var.invokeMethod(Var.castObject(bukkitPlayer, JType.classType(Ref.getObcClass("entity.CraftPlayer"))), "getHandle");
        Var playerConnection = Var.objectsField(entityPlayer, playerConnectionName);
        if (Ref.getVersion() >= 17) {
            openToPlayer.invokeMethod(playerConnection, "a", packet);
        } else {
            openToPlayer.invokeMethod(playerConnection, "sendPacket", packet);
        }
        if (Ref.getVersion() >= 19) {
            openToPlayer.setOthersFieldValue(entityPlayer, "bU", Var.thisVar());
        } else if (Ref.getVersion() == 18) {
            openToPlayer.setOthersFieldValue(entityPlayer, "bV", Var.thisVar());
        } else if (Ref.getVersion() == 17) {
            openToPlayer.setOthersFieldValue(entityPlayer, "bW", Var.thisVar());
        } else if (Ref.getVersion() == 16) {
            openToPlayer.setOthersFieldValue(entityPlayer, "bV", Var.thisVar());
        } else {
            openToPlayer.setOthersFieldValue(entityPlayer, "activeContainer", Var.thisVar());
        }
        if (Ref.getVersion() >= 17) {
            openToPlayer.invokeThisMethod("a", entityPlayer);
        } else {
            openToPlayer.invokeThisMethod("addSlotListener", entityPlayer);
        }
        try {
            return JGenerator.generate(anvil);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to create anvil gui!");
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////                                     Dynamic Class Generator                                       ///////
    ///////             2022 DCG Project - https://www.github.com/DeeChael/DynamicClassGenerator              ///////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public enum AnvilSlot {
        LEFT_INPUT(0), RIGHT_INPUT(1), OUTPUT(2);

        private final int slot;

        AnvilSlot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }
    }

}
