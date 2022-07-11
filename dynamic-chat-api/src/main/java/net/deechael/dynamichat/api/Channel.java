package net.deechael.dynamichat.api;

import net.deechael.useless.objs.TriObj;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Channel {

    void broadcast(String message);

    List<? extends User> getUsers();

    @NotNull
    String getName();

    String getDisplayName();

    @Nullable
    String getFormat();

    List<TriObj<String, String, Integer>> getPermissionFormats();

    boolean isGlobal();

    void setAvailableForAll(boolean available);

}
