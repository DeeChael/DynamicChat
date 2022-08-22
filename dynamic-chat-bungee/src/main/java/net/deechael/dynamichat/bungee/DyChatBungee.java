package net.deechael.dynamichat.bungee;

import net.deechael.dynamichat.proxy.DyCPacket;
import net.deechael.dynamichat.proxy.DyCPacketSolver;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public final class DyChatBungee extends Plugin implements Listener {

    private static DyChatBungee instance;

    public static DyChatBungee getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getProxy().registerChannel("dynamichat");
    }

    public static void sendPacket(DyCPacket packet) {
        getInstance().getProxy().getServers().values().forEach(info -> info.sendData("dynamichat", DyCPacketSolver.serialize(packet)));
    }

    @EventHandler
    public void receivePacket(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("dynamichat"))
            return;
        DyCPacket packet = DyCPacketSolver.deserialize(event.getData());


        //if (!(event.getReceiver() instanceof ProxiedPlayer))
        //    return;
        //ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
    }

}
