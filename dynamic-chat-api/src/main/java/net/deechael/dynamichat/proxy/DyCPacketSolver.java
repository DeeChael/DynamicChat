package net.deechael.dynamichat.proxy;

import net.deechael.dynamichat.proxy.packets.PacketChat;
import net.deechael.dynamichat.proxy.packets.PacketRequestSettings;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class DyCPacketSolver {

    private final static Map<Integer, Class<? extends DyCPacket>> PACKET_TYPES = new HashMap<>();

    static {
        PACKET_TYPES.put(1, PacketChat.class);
        PACKET_TYPES.put(2, PacketRequestSettings.class);
    }

    public static DyCPacket deserialize(byte[] bytes) {
        ByteReader reader = new ByteReader(bytes);
        byte id = reader.read();
        Class<? extends DyCPacket> packetClass = PACKET_TYPES.get((int) id);
        if (packetClass == null)
            throw new RuntimeException("Failed to parse packet with the id " + id);
        try {
            Constructor<? extends DyCPacket> constructor = packetClass.getConstructor(ByteReader.class);
            return constructor.newInstance(reader);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] serialize(DyCPacket packet) {
        ByteWriter writer = new ByteWriter();
        for (Map.Entry<Integer, Class<? extends DyCPacket>> entry : PACKET_TYPES.entrySet()) {
            if (entry.getValue() == packet.getClass())
                writer.write(entry.getKey().byteValue());
        }
        packet.serialize(writer);
        return writer.array();
    }

}
