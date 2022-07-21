package net.deechael.dynamichat.api;

import net.deechael.useless.objs.DuObj;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Ban or unban the ip
 */
public interface BanIPManager {

    /**
     * Get all banned host
     *
     * @return hosts (Unbanned not included)
     */
    List<String> getBanned();

    /**
     * Get all player name which is connected with a banned host
     *
     * @return player names (Lowercase)
     */
    List<String> getBannedWithPlayer();

    /**
     * Get the punishments of a host
     *
     * @param host the owner of punishments
     * @return all punishments including unbanned
     */
    List<BanIPPunishment> getPunishments(String host);

    /**
     * Get the punishments of a host connected with the player
     *
     * @param playerName the player name connected to the owner of punishments
     * @return all punishments including unbanned
     */
    List<BanIPPunishment> getPunishmentsWithPlayer(String playerName);

    /**
     * To get the current punishment of the host
     *
     * @param host the owner of punishments
     * @return if host not banned, return (false, null), else return (true, BanIPPunishment)
     */
    DuObj<Boolean, BanIPPunishment> getCurrent(String host);

    /**
     * To get the current punishment of the host connected with the player
     *
     * @param playerName the player name connected to the owner of punishments
     * @return if host not banned, return (false, null), else return (true, BanIPPunishment)
     */
    DuObj<Boolean, BanIPPunishment> getCurrentWithPlayer(String playerName);

    /**
     * To check whether the host is banned
     *
     * @param host the owner of punishments
     * @return the banned status
     */
    boolean isBanned(String host);

    /**
     * To check whether the host connected with the player is banned
     *
     * @param user the user connected with the owner of punishments
     * @return the banned status
     */
    boolean isBannedWithPlayer(PlayerUser user);

    /**
     * To check whether the host connected with the player is banned
     *
     * @param playerName the user connected with the owner of punishments
     * @return the banned status
     */
    boolean isBannedWithPlayer(String playerName);

    /**
     * Get the punishment by id
     *
     * @param banId the id of punishment
     * @return if id not exists, return null, else BanIPPunishment
     */
    BanIPPunishment get(String banId);

    /**
     * Ban the host
     *
     * @param host the host to be banned
     * @param operator the operator, should be the name of a player or "CONSOLE"
     * @param reason the reason why ban the player
     * @param start the start time, should be new Date()
     * @param end the end time, if is null, means forever
     * @return the punishment object
     */
    BanIPPunishment banIP(String host, String operator, String reason, Date start, Date end);

    /**
     * Ban the host
     *
     * @param address the host to be banned
     * @param operator the operator, should be the name of a player or "CONSOLE"
     * @param reason the reason why ban the player
     * @param start the start time, should be new Date()
     * @param end the end time, if is null, means forever
     * @return the punishment object
     */
    BanIPPunishment banIP(InetSocketAddress address, String operator, String reason, Date start, Date end);

    /**
     * Ban the host connected with the player
     *
     * @param user the player connected with the host
     * @param operator the operator, should be the name of a player or "CONSOLE"
     * @param reason the reason why ban the player
     * @param start the start time, should be new Date()
     * @param end the end time, if is null, means forever
     * @return the punishment object
     */
    BanIPPunishment banIPWithPlayer(PlayerUser user, String operator, String reason, Date start, Date end);

    /**
     * Unban the host by the id of punishment
     *
     * @param id the id of punishment
     */
    void unbanIPWithId(String id);

    /**
     * Unban the host by host
     *
     * @param host the host to be unbanned
     */
    void unbanIP(String host);

    /**
     * Unban the host by host
     *
     * @param address the host to be unbanned
     */
    void unbanIP(InetSocketAddress address);

    /**
     * Unban the host connected with the player
     *
     * @param uuid the player connected with the host
     */
    void unbanIPWithPlayer(UUID uuid);

    /**
     * Unban the host connected with the player
     *
     * @param playerName the player connected with the host
     */
    void unbanIPWithPlayer(String playerName);

}
