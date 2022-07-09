package net.deechael.dynamichat.api;

import java.util.UUID;

public interface User {

    void whisper(User another, String message);

    void say(String message);

}
