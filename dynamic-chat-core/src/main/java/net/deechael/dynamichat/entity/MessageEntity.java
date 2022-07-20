package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.BukkitUser;
import net.deechael.dynamichat.api.Message;

public class MessageEntity extends MuteMessageEntity implements Message {

    private final BukkitUser bukkitUser;

    public MessageEntity(BukkitUser bukkitUser, String content, String id, String sendTime) {
        super(bukkitUser.getName(), content, id, sendTime);
        this.bukkitUser = bukkitUser;
    }

    @Override
    public BukkitUser getSender() {
        return bukkitUser;
    }

}
