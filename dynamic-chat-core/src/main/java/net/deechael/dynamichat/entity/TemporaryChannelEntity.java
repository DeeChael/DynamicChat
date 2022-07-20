package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.BukkitUser;
import net.deechael.dynamichat.api.BukkitChatManager;
import net.deechael.dynamichat.api.TemporaryChannel;
import net.deechael.dynamichat.api.User;
import net.deechael.dynamichat.util.ConfigUtils;
import net.deechael.useless.objs.TriObj;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TemporaryChannelEntity extends ChannelEntity implements TemporaryChannel {

    private boolean dropped = false;

    public TemporaryChannelEntity(String displayName, String format) {
        super("");
        this.setDisplayName(displayName);
        if (format == null)
            format = ConfigUtils.getChatFormat();
        this.setFormat(format);
    }

    @Override
    @NotNull
    public String getName() {
        check();
        throw new RuntimeException("Temporary channel cannot have name");
    }

    @Override
    public void drop() {
        check();
        for (BukkitUser bukkitUser : this.getUsers()) {
            removeUser(bukkitUser);
        }
        dropped = true;
    }

    @Override
    public boolean isDropped() {
        return dropped;
    }

    @Override
    public void addUser(User bukkitUser) {
        check();
        if (!this.getUsersRaw().contains((BukkitUserEntity) bukkitUser)) {
            this.getUsersRaw().add((BukkitUserEntity) bukkitUser);
        }
    }

    @Override
    public List<TriObj<String, String, Integer>> getPermissionFormats() {
        check();
        return super.getPermissionFormats();
    }

    @Override
    public List<BukkitUserEntity> getUsers() {
        check();
        return super.getUsers();
    }

    @Override
    public List<BukkitUserEntity> getUsersRaw() {
        check();
        return super.getUsersRaw();
    }

    @Override
    public @Nullable String getFormat() {
        check();
        return super.getFormat();
    }

    public void setFormat(String format) {
        check();
        super.setFormat(format);
    }

    @Override
    public String getDisplayName() {
        check();
        return super.getDisplayName();
    }

    @Override
    public void removeUser(User bukkitUser) {
        check();
        if (bukkitUser.getCurrent() == this) {
            ((BukkitUserEntity) bukkitUser).setCurrent((ChannelEntity) BukkitChatManager.getManager().getGlobal());
            this.getUsersRaw().remove(bukkitUser);
        }
    }

    private void check() {
        if (isDropped()) {
            throw new RuntimeException("This channel has been dropped");
        }
    }

    @Override
    public void setAvailableForAll(boolean available) {
        check();
        setAvailable(available);
    }

}
