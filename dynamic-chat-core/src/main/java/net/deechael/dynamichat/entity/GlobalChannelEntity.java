package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.ChatManager;
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
        for (UserEntity user : this.getUsers()) {
            user.getSender().sendMessage(message);
        }
    }

    @Override
    public List<UserEntity> getUsers() {
        List<UserEntity> users = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            users.add((UserEntity) ChatManager.getManager().getUser(player));
        }
        users.add((UserEntity) ChatManager.getManager().getUser(Bukkit.getConsoleSender()));
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
