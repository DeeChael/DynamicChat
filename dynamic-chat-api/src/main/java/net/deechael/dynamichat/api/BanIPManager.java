package net.deechael.dynamichat.api;

import net.deechael.dynamichat.object.BanIPPunishment;
import net.deechael.useless.objs.DuObj;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface BanIPManager {

    List<String> getBanned();

    List<String> getBannedWithPlayer();

    List<BanIPPunishment> getPunishments(String host);

    List<BanIPPunishment> getPunishmentsWithPlayer(String playerName);

    DuObj<Boolean, BanIPPunishment> getCurrent(String host);

    DuObj<Boolean, BanIPPunishment> getCurrentWithPlayer(String playerName);

    boolean isBanned(String host);

    boolean isBannedWithPlayer(PlayerUser user);

    boolean isBannedWithPlayer(String playerName);

    BanIPPunishment get(String banId);

    BanIPPunishment banIP(String host, String operator, String reason, Date start, Date end);

    BanIPPunishment banIP(InetSocketAddress address, String operator, String reason, Date start, Date end);

    BanIPPunishment banIPWithPlayer(PlayerUser user, String operator, String reason, Date start, Date end);

    BanIPPunishment banIPWithPlayer(String playerName, String operator, String reason, Date start, Date end);

    void unbanIPWithId(String id);

    void unbanIP(String host);

    void unbanIP(InetSocketAddress address);

    void unbanIPWithPlayer(UUID uuid);

    void unbanIPWithPlayer(String playerName);

}
