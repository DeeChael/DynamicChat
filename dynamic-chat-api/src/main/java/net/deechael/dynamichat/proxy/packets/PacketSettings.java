package net.deechael.dynamichat.proxy.packets;

import net.deechael.dynamichat.proxy.ByteReader;
import net.deechael.dynamichat.proxy.ByteWriter;
import net.deechael.dynamichat.proxy.DyCPacket;

public final class PacketSettings implements DyCPacket {

    private boolean sendMessageToAllServers = false;
    private boolean chatColorEnabled = false;
    private boolean gradientColorEnabled = false;
    private boolean mentionPlayerEnabled = false;
    private boolean channelEnabled = false;
    private boolean whisperSoundEnabled = false;
    private boolean replaceEnabled = false;
    private boolean filterEnabled = false;

    public PacketSettings() {

    }

    public PacketSettings(ByteReader reader) {
        this.sendMessageToAllServers = reader.read() == 1;
        this.chatColorEnabled = reader.read() == 1;
        this.gradientColorEnabled = reader.read() == 1;
        this.mentionPlayerEnabled = reader.read() == 1;
        this.channelEnabled = reader.read() == 1;
        this.whisperSoundEnabled = reader.read() == 1;
        this.replaceEnabled = reader.read() == 1;
        this.filterEnabled = reader.read() == 1;
    }

    @Override
    public void serialize(ByteWriter writer) {
        writer.write((byte) (sendMessageToAllServers ? 1 : 0));
        writer.write((byte) (chatColorEnabled ? 1 : 0));
        writer.write((byte) (gradientColorEnabled ? 1 : 0));
        writer.write((byte) (mentionPlayerEnabled ? 1 : 0));
        writer.write((byte) (channelEnabled ? 1 : 0));
        writer.write((byte) (whisperSoundEnabled ? 1 : 0));
        writer.write((byte) (replaceEnabled ? 1 : 0));
        writer.write((byte) (filterEnabled ? 1 : 0));
    }

}
