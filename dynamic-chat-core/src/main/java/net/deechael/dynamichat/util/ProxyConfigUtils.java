package net.deechael.dynamichat.util;

import lombok.Getter;
import lombok.Setter;

public final class ProxyConfigUtils {

    @Getter
    @Setter
    private static boolean sendMessageToAllServers;
    @Getter
    @Setter
    private static boolean chatColorEnabled;
    @Getter
    @Setter
    private static boolean gradientColorEnabled;
    @Getter
    @Setter
    private static boolean mentionPlayerEnabled;
    @Getter
    @Setter
    private static boolean channelEnabled;
    @Getter
    @Setter
    private static boolean whisperSoundEnabled;
    @Getter
    @Setter
    private static boolean replaceEnabled;
    @Getter
    @Setter
    private static boolean filterEnabled;

    private ProxyConfigUtils() {}

}
