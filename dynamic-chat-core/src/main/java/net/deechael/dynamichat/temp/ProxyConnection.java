package net.deechael.dynamichat.temp;

import net.deechael.dynamichat.DyChatPlugin;
import net.deechael.dynamichat.proxy.DyCPacket;
import net.deechael.dynamichat.proxy.DyCPacketSolver;
import net.deechael.dynamichat.proxy.packets.PacketSettings;
import net.deechael.dynamichat.util.ProxyConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ProxyConnection implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("dynamichat"))
            return;
        DyCPacket packet = DyCPacketSolver.deserialize(message);
        if (packet instanceof PacketSettings) {
            for (Field field : PacketSettings.class.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()))
                    continue;
                if (field.getType() != boolean.class)
                    continue;
                field.setAccessible(true);
                try {
                    Field proxyConfigField = ProxyConfigUtils.class.getDeclaredField(field.getName());
                    proxyConfigField.setAccessible(true);
                    proxyConfigField.set(null, field.get(packet));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void sendPacket(DyCPacket packet) {
        Bukkit.getServer().sendPluginMessage(DyChatPlugin.getInstance(), "dynamichat", DyCPacketSolver.serialize(packet));
    }

}
