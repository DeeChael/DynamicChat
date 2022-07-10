package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.User;

import java.util.ArrayList;
import java.util.List;

public class ChannelEntity implements Channel {

    private final List<UserEntity> users = new ArrayList<>();

    private final String name;
    private String displayName = null;

    public ChannelEntity(String name) {
        this.name = name;
    }

    @Override
    public void broadcast(String message) {
    }

    void broadcast0(String message) {
        for (UserEntity user : this.users) {
            user.getSender().sendMessage(message);
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(this.users);
    }

    @Override
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

}
