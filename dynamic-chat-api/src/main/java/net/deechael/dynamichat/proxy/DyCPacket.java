package net.deechael.dynamichat.proxy;

import java.io.Serializable;

public interface DyCPacket extends Serializable {

    void serialize(ByteWriter writer);

}
