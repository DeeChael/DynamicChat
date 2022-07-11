package net.deechael.dynamichat.api;

import java.util.List;

public interface User {

    void whisper(User another, String message);

    void say(String message);

    void chat(String message);

    Channel getCurrent();

    String getName();

    String getDisplayName();

    List<? extends Channel> getAvailable();

    void moveTo(Channel channel);

    void available(Channel channel);

    void unavailable(Channel channel);

}
