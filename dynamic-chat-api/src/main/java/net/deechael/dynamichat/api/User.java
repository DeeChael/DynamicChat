package net.deechael.dynamichat.api;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.Date;
import java.util.List;

/**
 * Basic class
 */
public interface User {

    /**
     * Send message to the user
     * @param message the message
     */
    void sendMessage(String message);

    /**
     * Send message to the user
     * @param components the message
     */
    void sendMessage(BaseComponent... components);

    /**
     * Send message to the user
     * @param builder the message
     */
    void sendMessage(ComponentBuilder builder);

    /**
     * Make the user whisper to another user
     *
     * @param another the user will receive the message
     * @param message the message
     */
    void whisper(BukkitUser another, String message);

    /**
     * Make the user invoke "say" command
     * @param message the message
     */
    void say(String message);

    /**
     * Make the user chat the message
     * @param message the message
     */
    void chat(String message);

    /**
     * Get the channel the user is currently chatting
     * @return the channel
     */
    Channel getCurrent();

    /**
     * Get the name of the user
     * @return username
     */
    String getName();

    /**
     * Get the display name of the user
     * @return user display name
     */
    String getDisplayName();

    /**
     * Get all the channels the user has access to
     * @return the channels
     */
    List<? extends Channel> getAvailable();

    /**
     * Set the channel that the user is currently chatting
     * @param channel the channel
     */
    void moveTo(Channel channel);

    /**
     * Make a channel is available for the user
     * @param channel the channel
     */
    void available(Channel channel);

    /**
     * Make a channel is unavailable for the user
     * @param channel the channel
     */
    void unavailable(Channel channel);

    /**
     * Ban the user's ip forever
     */
    void banIP();

    /**
     * Unban the user's ip
     */
    void unbanIP();

    /**
     * Unban the user
     */
    void unban();

    /**
     * Unmute the user
     */
    void unmute();

    /**
     * Ban the user
     */
    void ban();

    /**
     * Ban the user
     *
     * @param time how long
     */
    void ban(Time time);

    /**
     * Ban the user
     *
     * @param unbanDate the date the user will be unbanned
     */
    void ban(Date unbanDate);

    /**
     * Ban the user
     *
     * @param reason the reason why ban the user
     */
    void ban(String reason);

    /**
     * Ban the user
     *
     * @param time how long
     * @param reason the reason why ban the user
     */
    void ban(Time time, String reason);

    /**
     * Ban the user
     *
     * @param unbanDate the date the user will be unbanned
     * @param reason the reason why ban the user
     */
    void ban(Date unbanDate, String reason);

    /**
     * Kick the user
     */
    void kick();

    /**
     * Kick the user
     * @param reason the reason why kick the user
     */
    void kick(String reason);

    /**
     * Mute the user
     */
    void mute();

    /**
     * Mute the user
     *
     * @param time how long
     */
    void mute(Time time);

    /**
     * Mute the user
     *
     * @param unmuteDate the date the user will be unmuted
     */
    void mute(Date unmuteDate);

    /**
     * Mute the user
     *
     * @param reason the reason why mute the user
     */
    void mute(String reason);

    /**
     * Mute the user
     *
     * @param time how long
     * @param reason the reason why mute the user
     */
    void mute(Time time, String reason);

    /**
     * Mute the user
     *
     * @param unmuteDate the date the user will be unmuted
     * @param reason the reason why mute the user
     */
    void mute(Date unmuteDate, String reason);

    /**
     * Mute the user
     *
     * @return all the ban punishments of the player
     */
    List<? extends Punishment> getBanHistory();

    /**
     *
     * @return all the mute punishments of the player
     */
    List<? extends Punishment> getMuteHistory();

    /**
     *
     * @return all the punishments of the player
     */
    List<? extends Punishment> getPunishHistory();

    /**
     * Get whether the user is banned
     *
     * @return always false actually because only the user that can get into the server will create a User object
     */
    boolean isBanned();

    /**
     * Get whether the user is muted
     *
     * @return the status
     */
    boolean isMuted();

    /**
     * Get the ban punishment object
     *
     * @param id the id of the punishment
     * @return punishment object
     */
    Punishment getBan(String id);

    /**
     * Get the mute punishment object
     *
     * @param id the id of the punishment
     * @return punishment object
     */
    Punishment getMute(String id);

}
