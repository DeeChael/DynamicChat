package net.deechael.dynamichat.api;

/**
 * Button will show after the message is clicked
 */
public interface MessageButton {

    /**
     * The text will show
     * @param clicker the user clicked message
     * @param message the message was clicked
     * @return the text, if null means the button won't show for the user
     */
    String display(User clicker, Message message);

    /**
     * Will be invoked after the user click the button
     * @param clicker user
     * @param message the owner of the button
     */
    void click(User clicker, Message message);

    /**
     * The tips will be shown if the user move their cursor on the message button
     * @param clicker user
     * @param message the owner of the button
     * @return the text will be shown
     */
    String hover(User clicker, Message message);

}
