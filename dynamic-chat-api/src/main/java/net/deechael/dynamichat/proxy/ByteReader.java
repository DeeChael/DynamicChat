package net.deechael.dynamichat.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteReader {

    private final byte[] bytes;
    private int index = 0;

    public ByteReader(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte read() {
        byte b = bytes[index];
        index ++;
        return b;
    }

    public byte[] read(int amount) {
        List<Byte> byteList = new ArrayList<>();
        if (this.index + amount >= bytes.length)
            throw new IndexOutOfBoundsException("Bytes are not enough");
        for (int i = 0; i < amount; i++) {
            byteList.add(read());
        }
        return classByteToPrimByte(byteList.toArray(new Byte[0]));
    }

    public byte[] readRest() {
        List<Byte> byteList = new ArrayList<>();
        while (this.readable()) {
            byteList.add(this.read());
        }
        return classByteToPrimByte(byteList.toArray(new Byte[0]));
    }

    public byte[] readAll() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    public boolean readable() {
        return index < bytes.length;
    }

    public int length() {
        return this.bytes.length;
    }

    private static byte[] classByteToPrimByte(Byte[] bytes) {
        byte[] primBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            primBytes[i] = bytes[i];
        }
        return primBytes;
    }

}
