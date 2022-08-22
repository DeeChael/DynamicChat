package net.deechael.dynamichat.proxy.packets;

import net.deechael.dynamichat.proxy.ByteReader;
import net.deechael.dynamichat.proxy.ByteWriter;
import net.deechael.dynamichat.proxy.DyCPacket;

public final class PacketRequestSettings implements DyCPacket {

    public PacketRequestSettings() {
    }

    public PacketRequestSettings(ByteReader reader) {
        if (reader.read() != 0)
            throw new RuntimeException("Error packet");
    }

    @Override
    public void serialize(ByteWriter writer) {
        writer.write((byte) 0);
    }

}
