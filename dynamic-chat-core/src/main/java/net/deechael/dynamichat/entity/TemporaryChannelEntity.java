package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.ChatManager;
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
        for (User user : this.getUsers()) {
            removeUser(user);
        }
        dropped = true;
    }

    @Override
    public boolean isDropped() {
        return dropped;
    }

    @Override
    public void addUser(User user) {
        check();
        if (!this.getUsersRaw().contains((UserEntity) user)) {
            this.getUsersRaw().add((UserEntity) user);
        }
    }

    @Override
    public List<TriObj<String, String, Integer>> getPermissionFormats() {
        check();
        return super.getPermissionFormats();
    }

    @Override
    public List<UserEntity> getUsers() {
        check();
        return super.getUsers();
    }

    @Override
    public List<UserEntity> getUsersRaw() {
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
    public void removeUser(User user) {
        check();
        if (user.getCurrent() == this) {
            ((UserEntity) user).setCurrent((ChannelEntity) ChatManager.getManager().getGlobal());
            this.getUsersRaw().remove(user);
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
