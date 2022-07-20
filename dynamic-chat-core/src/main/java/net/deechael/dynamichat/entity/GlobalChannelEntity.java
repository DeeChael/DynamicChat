package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.BukkitChatManager;
import net.deechael.dynamichat.util.ConfigUtils;
import net.deechael.useless.objs.TriObj;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GlobalChannelEntity extends ChannelEntity {

    public GlobalChannelEntity() {
        super("global");
    }

    @Override
    public void broadcast(String message) {
    }

    void broadcast0(String message) {
        for (BukkitUserEntity user : this.getUsers()) {
            user.getSender().sendMessage(message);
        }
    }

    @Override
    public List<BukkitUserEntity> getUsers() {
        List<BukkitUserEntity> users = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            users.add((BukkitUserEntity) BukkitChatManager.getManager().getBukkitUser(player));
        }
        users.add((BukkitUserEntity) BukkitChatManager.getManager().getBukkitUser(Bukkit.getConsoleSender()));
        return users;
    }

    @Override
    @NotNull
    public String getName() {
        return "global";
    }

    @Override
    public String getDisplayName() {
        return ConfigUtils.globalChannelDisplayName();
    }

    @Override
    public List<TriObj<String, String, Integer>> getPermissionFormats() {
        return ConfigUtils.permissionFormat();
    }

    @Override
    public @Nullable String getFormat() {
        return ConfigUtils.getChatFormat();
    }

    @Override
    public boolean isGlobal() {
        return true;
    }

}
