package net.deechael.dynamichat.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public final class CollectionUtils {

    public static <T> T randomElement(Collection<? extends T> collection) {
        List<T> list = new ArrayList<>(collection);
        return list.get(new Random().nextInt(list.size()));
    }

    public static byte[] classByteToPrimByte(Byte[] bytes) {
        byte[] primBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            primBytes[i] = bytes[i];
        }
        return primBytes;
    }

    private CollectionUtils() {}

}
