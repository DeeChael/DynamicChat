package net.deechael.dynamichat;

import net.deechael.dynamichat.command.EzCommand;
import net.deechael.dynamichat.command.EzCommandManager;
import net.deechael.dynamichat.command.EzCommandRegistered;
import net.deechael.dynamichat.gui.AnvilGUI;
import net.deechael.dynamichat.gui.items.Clicker;
import net.deechael.dynamichat.temp.NMSCommandKiller;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.plugin.java.JavaPlugin;

public class DyChatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(EzCommandManager.INSTANCE, this);
        EzCommandRegistered mainCommand = EzCommandManager.register("dyanmichat", new EzCommand("dynamic-chat").executes((sender, helper) -> {
            if (sender instanceof Player) {
                try {
                    AnvilGUI gui = new AnvilGUI(DyChatPlugin.this);
                    gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, (Clicker) (viewer, type, action) -> viewer.sendMessage("Type: " + type.name() + ", Action: " + action.name()));
                    gui.open((Player) sender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 1;
        }));
        //EzCommandManager.register("dynamichat", new EzCommand("dynamicchat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dynamichat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dychat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dchat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dc").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dy-chat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("dee-chat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("deechat").redirect(mainCommand));
        //EzCommandManager.register("dynamichat", new EzCommand("deechaelchat").redirect(mainCommand));
        NMSCommandKiller.kill(this);
    }

    public static DyChatPlugin getInstance() {
        return JavaPlugin.getPlugin(DyChatPlugin.class);
    }

}
