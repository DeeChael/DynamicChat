package net.deechael.dynamichat.proxy.packets;

import net.deechael.dynamichat.proxy.ByteReader;
import net.deechael.dynamichat.proxy.ByteWriter;
import net.deechael.dynamichat.proxy.DyCPacket;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public final class PacketChat implements DyCPacket {

    private UUID sender;
    private String content;
    private Date sendDate;

    public PacketChat(UUID sender, String content, Date sendDate) {
        this.sender = sender;
        this.content = content;
        this.sendDate = sendDate;
    }

    public PacketChat(ByteReader reader) {
        this.sender = UUID.fromString(new String(reader.read(36)));
        try {
            this.sendDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new String(reader.read(19)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.content = new String(reader.readRest());
    }

    @Override
    public void serialize(ByteWriter writer) {
        writer.write(sender.toString().getBytes(StandardCharsets.UTF_8));
        writer.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.sendDate).getBytes(StandardCharsets.UTF_8));
        writer.write(this.content.getBytes(StandardCharsets.UTF_8));
    }

}
