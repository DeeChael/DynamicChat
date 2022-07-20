import java.util.Arrays;

public class AddressTest {

    public static void main(String[] args) {
        a();
        b();
    }

    // 256 128 64 32 16 8 4 2 1
    public static void a() {
        String host = "192.168.0.1";
        String[] sp = host.split("\\.");
        if (sp.length != 4)
            throw new RuntimeException("Invalided host");
        long[] spInt = new long[4];
        for (int i = 0; i < 4; i++) {
            try {
                spInt[i] = Long.parseLong(sp[i]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Host should be made of numbers!", e);
            }
        }
        System.out.println(Arrays.toString(spInt));
        System.out.println((spInt[0] << 24) | (spInt[1] << 16) | (spInt[2] << 8) | spInt[3]);
    }

    public static void b() {
        long hostInt = 3232235521L;
        String host = (hostInt >>> 24) + "." + ((hostInt & 0x00FFFFFF) >>> 16) + "." + ((hostInt & 0x0000FFFF) >>> 8) + "." + (hostInt & 0x000000FF);
        System.out.println(host);
    }

}
