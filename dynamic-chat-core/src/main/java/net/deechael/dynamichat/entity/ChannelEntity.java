package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.util.ConfigUtils;
import net.deechael.useless.objs.TriObj;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChannelEntity implements Channel {

    private final List<BukkitUserEntity> users = new ArrayList<>();

    private final String name;
    private final List<TriObj<String, String, Integer>> permissionFormats = new ArrayList<>();
    private String displayName = null;
    private String format = null;
    private boolean available = true;

    public ChannelEntity(@NotNull String name) {
        this.name = name;
    }

    @Override
    public void broadcast(String message) {
        broadcast0(ConfigUtils.channelMessageFormat().replace("%message%", DynamicChatPlaceholder.replaceChannel(this, message)));
    }

    void broadcast0(String message) {
        for (BukkitUserEntity entity : getUsers()) {
            entity.getSender().sendMessage(message);
        }
    }

    @Override
    public List<BukkitUserEntity> getUsers() {
        return new ArrayList<>(this.users);
    }

    public List<BukkitUserEntity> getUsersRaw() {
        return this.users;
    }

    @Override
    @NotNull
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.displayName == null ? this.name : this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    @Nullable
    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<TriObj<String, String, Integer>> getPermissionFormats() {
        return permissionFormats;
    }

    public void setPermissionFormats(List<TriObj<String, String, Integer>> permissionFormats) {
        this.permissionFormats.clear();
        this.permissionFormats.addAll(permissionFormats);
    }

    @Override
    public boolean isGlobal() {
        return false;
    }

    @Override
    public void setAvailableForAll(boolean available) {
        setAvailable(available);
    }

}
