package net.deechael.dynamichat.api;

import java.util.List;

public interface TemporaryChannel extends Channel {

    void drop();

    boolean isDropped();

    void addUser(User user);

    void removeUser(User user);

}
