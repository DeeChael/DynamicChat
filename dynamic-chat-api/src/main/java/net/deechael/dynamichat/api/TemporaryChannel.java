package net.deechael.dynamichat.api;

public interface TemporaryChannel extends Channel {

    void drop();

    boolean isDropped();

    void addUser(User user);

    void removeUser(User user);

}
