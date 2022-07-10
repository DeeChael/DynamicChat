package net.deechael.dynamichat.api;

import java.util.List;

public interface Channel {

    void broadcast(String message);

    List<User> getUsers();

    String getName();

    String getDisplayName();

}
