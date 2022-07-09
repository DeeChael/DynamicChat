package net.deechael.dynamichat;

import net.deechael.dynamichat.command.EzCommand;
import net.deechael.dynamichat.command.EzCommandManager;
import net.deechael.dynamichat.command.EzCommandRegistered;
import net.deechael.dynamichat.temp.NMSCommandKiller;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DyChatPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(EzCommandManager.INSTANCE, this);
        EzCommandRegistered mainCommand = EzCommandManager.register("dyanmichat", new EzCommand("dynamic-chat").executes((sender, helper) -> {
            sender.sendMessage("§cHello!");
            return 1;
        }));
        EzCommandManager.register("dynamichat", new EzCommand("dynamicchat").redirect(mainCommand));
        EzCommandManager.register("dynamichat", new EzCommand("dynamichat").redirect(mainCommand));
        EzCommandManager.register("dynamichat", new EzCommand("dychat").redirect(mainCommand));
        EzCommandManager.register("dynamichat", new EzCommand("dchat").redirect(mainCommand));
        EzCommandManager.register("dynamichat", new EzCommand("dc").redirect(mainCommand));
        EzCommandManager.register("dynamichat", new EzCommand("dy-chat").redirect(mainCommand));
        EzCommandManager.register("dynamichat", new EzCommand("dee-chat").redirect(mainCommand));
        EzCommandManager.register("dynamichat", new EzCommand("deechat").redirect(mainCommand));
        EzCommandManager.register("dynamichat", new EzCommand("deechaelchat").redirect(mainCommand));
        NMSCommandKiller.kill(this);
    }

}
