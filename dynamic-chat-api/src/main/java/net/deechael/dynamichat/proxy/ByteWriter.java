package net.deechael.dynamichat.proxy;

import java.util.ArrayList;
import java.util.List;

public class ByteWriter {

    private final List<Byte> byteList = new ArrayList<>();

    public void write(byte b) {
        this.byteList.add(b);
    }

    public void write(byte... bytes) {
        for (byte b : bytes) {
            this.byteList.add(b);
        }
    }

    public byte[] array() {
        return classByteToPrimByte(this.byteList.toArray(new Byte[0]));
    }

    private static byte[] classByteToPrimByte(Byte[] bytes) {
        byte[] primBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            primBytes[i] = bytes[i];
        }
        return primBytes;
    }

}
