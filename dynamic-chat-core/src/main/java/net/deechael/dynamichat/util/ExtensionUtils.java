package net.deechael.dynamichat.util;

import org.bukkit.Bukkit;

public final class ExtensionUtils {

    private ExtensionUtils() {
    }

    public static boolean enabledMuteAndBanExtension() {
        return Bukkit.getPluginManager().isPluginEnabled("DynamicChat-MNB");
    }

}
