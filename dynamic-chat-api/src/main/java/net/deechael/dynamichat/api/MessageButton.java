package net.deechael.dynamichat.api;

public interface MessageButton {

    String display(User clicker, Message message);

    void click(User clicker, Message message);

    String hover(User clicker, Message message);

}
