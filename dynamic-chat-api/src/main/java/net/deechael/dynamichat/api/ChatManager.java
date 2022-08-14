package net.deechael.dynamichat.api;

import net.deechael.dynamichat.annotation.Included;
import net.deechael.dynamichat.annotation.NotIncluded;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * To manage all the things of DynamicChat
 */
public interface ChatManager {

    /**
     * Get the channel by name
     * @param name channel name
     * @return channel
     */
    Channel getChannel(String name);

    /**
     * Get all channels
     *
     * @return all channels
     */
    List<? extends Channel> getChannels();

    /**
     * Get all temporary channels
     *
     * @return all temporary channels
     */
    List<? extends TemporaryChannel> getTemporaryChannels();

    /**
     * Get the global channel
     * @return global channel
     */
    Channel getGlobal();

    /**
     * Create a temporary channel
     *
     * @param displayName the display name
     * @param format chat format
     * @return the temporary channel
     */
    TemporaryChannel createTemporaryChannel(@Nullable String displayName, @Nullable String format);

    /**
     * Register the button showing when click the message
     * @param index the index, don't be in [0, 10], this is the range of the buttons of official extensions
     * @param button button object
     */
    void registerButton(int index, MessageButton button);

    /**
     * Get all the registered buttons
     *
     * @return (index, button object)
     */
    Map<Integer, ? extends MessageButton> getButtons();

    /**
     * Because the index of buttons can be customized, this will return the max index of the registered buttons
     *
     * @return the max index
     */
    int getButtonMaxIndex();

    /**
     * Because the index of buttons can be customized, this will return the min index of the registered buttons
     *
     * @return the min index
     */
    int getButtonMinIndex();

    /**
     * To check whether language should follow the client
     *
     * @return status
     */
    boolean languageFollowClient();

    /**
     * Get default language name
     *
     * @return default language
     */
    String getDefaultLanguage();

    /**
     * Get the index of the message
     *
     * @param message message
     * @return the index
     */
    int getIndex(MuteMessage message);

    /**
     * Get a message by index
     *
     * @param index the index of the message
     * @return the message
     */
    MuteMessage getMessageByIndex(int index);

    /**
     * Get the message by id
     * @param id the id of the message
     * @return the message
     */
    MuteMessage getMessageById(String id);

    /**
     * Get context
     *
     * @param startIndex start index
     * @param endIndex end index
     * @return the context
     */
    Context getContext(@Included int startIndex, @NotIncluded int endIndex);

    /**
     * Get the context start from a message object
     *
     * @param startMessage start message
     * @param amount the amount
     * @return the context
     */
    Context getContext(MuteMessage startMessage, int amount);

    /**
     * To get how many messages that have been stored
     * @return the amount of the stored messages
     */
    int recordedMessages();

    /**
     * Create a time obejct
     * @param years yesrs
     * @param months months
     * @param weeks weeks
     * @param days days
     * @param hours hours
     * @param minutes minutes
     * @param seconds seconds
     * @return a time object
     */
    Time createTime(int years, int months, int weeks, int days, int hours, int minutes, int seconds);

    /**
     * To create time from seconds long
     *
     * @param seconds seconds not milliseconds
     * @return a time objects
     */
    Time createTime(long seconds);

    /**
     * Parse time from string
     *
     * @param timeString e.g. 1y2mo3wk4d5h6min7s
     * @return a time object
     */
    Time parseTime(String timeString);

    /**
     * Get ban ip manager to ban or unban ip
     *
     * @return BanIPManager
     */
    BanIPManager getBanIPManager();

}
