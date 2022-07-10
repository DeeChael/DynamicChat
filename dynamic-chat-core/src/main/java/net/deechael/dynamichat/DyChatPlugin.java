package net.deechael.dynamichat;

import net.deechael.dynamichat.command.EzCommand;
import net.deechael.dynamichat.command.EzCommandManager;
import net.deechael.dynamichat.command.EzCommandRegistered;
import net.deechael.dynamichat.temp.NMSCommandKiller;
import net.deechael.dynamichat.util.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DyChatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(EzCommandManager.INSTANCE, this);
        EzCommandRegistered mainCommand = EzCommandManager.register("dyanmichat", new EzCommand("dynamic-chat").executes((sender, helper) -> {
            sender.sendMessage(ConfigUtils.lang("command.gotohelp"));
            /*if (sender instanceof Player) {
                try {
                    AnvilGUI gui = new AnvilGUI(DyChatPlugin.this, "§c§lDynamicChat");
                    //gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, (Clicker) (viewer, type, action) -> viewer.sendMessage("Type: " + type.name() + ", Action: " + action.name()));
                    gui.setItem(AnvilGUI.AnvilSlot.LEFT_INPUT, new Storage() {
                        @Override
                        public boolean isAllow(Player viewer, ItemStack cursorItem) {
                            return cursorItem.getType().isFuel();
                        }
                    });
                    gui.open((Player) sender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            */
            return 1;
        }).then(new EzCommand("help").executes((sender, helper) -> {
            sender.sendMessage(ConfigUtils.lang("command.main-help"));
            return 1;
        })).then(new EzCommand("reload").executes((sender, helper) -> {
            ConfigUtils.load();
            sender.sendMessage(ConfigUtils.lang("command.main-reload-success"));
            return 1;
        })));
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
