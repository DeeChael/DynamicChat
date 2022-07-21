package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.DyChatPlugin;
import net.deechael.dynamichat.api.BanIPManager;
import net.deechael.dynamichat.api.PlayerBukkitUser;
import net.deechael.dynamichat.api.PlayerUser;
import net.deechael.dynamichat.api.BanIPPunishment;
import net.deechael.dynamichat.sql.Sqlite;
import net.deechael.dynamichat.util.StringUtils;
import net.deechael.useless.objs.DuObj;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class DynamicBanIPManager implements BanIPManager {

    final static DynamicBanIPManager INSTANCE = new DynamicBanIPManager();

    private Sqlite sqlite;

    public static void reload() {
        if (INSTANCE.sqlite != null) {
            INSTANCE.sqlite.close();
        }
        File dbFile = new File(DyChatPlugin.getInstance().getDataFolder(), "banned-ip.db");
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        INSTANCE.sqlite = new Sqlite(dbFile);
        INSTANCE.sqlite.executeUpdate("CREATE TABLE IF NOT EXISTS `punishments` ( `ban_id` TEXT , `host` BIGINT , `withUser` TEXT  , `operator` TEXT , `start_date` TEXT , `end_date` TEXT  , `reason` TEXT , `unbanned` BOOLEAN);");
    }

    @Override
    public List<String> getBanned() {
        List<String> banned = new ArrayList<>();
        ResultSet resultSet = this.sqlite.executeQuery("SELECT * FROM `punishments`;");
        try {
            while (resultSet.next()) {
                if (resultSet.getBoolean("unbanned"))
                    continue;
                long hostLong = resultSet.getLong("host");
                String host = (hostLong >>> 24) + "." + ((hostLong & 0x00FFFFFF) >>> 16) + "." + ((hostLong & 0x0000FFFF) >>> 8) + "." + (hostLong & 0x000000FF);
                if (!banned.contains(host)) {
                    banned.add(host);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return banned;
    }

    @Override
    public List<String> getBannedWithPlayer() {
        List<String> banned = new ArrayList<>();
        ResultSet resultSet = this.sqlite.executeQuery("SELECT * FROM `punishments`;");
        try {
            while (resultSet.next()) {
                if (resultSet.getBoolean("unbanned"))
                    continue;
                String user = resultSet.getString("withUser");
                if (!banned.contains(user)) {
                    banned.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return banned;
    }

    @Override
    public List<BanIPPunishment> getPunishments(String host) {
        List<BanIPPunishment> punishments = new ArrayList<>();
        PreparedStatement statement = this.sqlite.preparedStatement("SELECT * FROM `punishments` WHERE host=?;");
        try {
            statement.setString(1, host);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String withUser = resultSet.getString("withUser");
                String operator = resultSet.getString("operator");
                String start_date = resultSet.getString("start_date");
                String end_date = resultSet.getString("end_date");
                String reason = resultSet.getString("reason");
                boolean unbanned = resultSet.getBoolean("unbanned");
                punishments.add(new BanIPPunishmentEntity(resultSet.getString("ban_id"), host, withUser, operator, start_date, end_date, unbanned, reason));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return punishments;
    }

    @Override
    public List<BanIPPunishment> getPunishmentsWithPlayer(String playerName) {
        List<BanIPPunishment> punishments = new ArrayList<>();
        PreparedStatement statement = this.sqlite.preparedStatement("SELECT * FROM `punishments` WHERE withUser=?;");
        try {
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long hostLong = resultSet.getLong("host");
                String host = (hostLong >>> 24) + "." + ((hostLong & 0x00FFFFFF) >>> 16) + "." + ((hostLong & 0x0000FFFF) >>> 8) + "." + (hostLong & 0x000000FF);
                String operator = resultSet.getString("operator");
                String start_date = resultSet.getString("start_date");
                String end_date = resultSet.getString("end_date");
                String reason = resultSet.getString("reason");
                boolean unbanned = resultSet.getBoolean("unbanned");
                punishments.add(new BanIPPunishmentEntity(resultSet.getString("ban_id"), host, playerName, operator, start_date, end_date, unbanned, reason));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return punishments;
    }

    @Override
    public DuObj<Boolean, BanIPPunishment> getCurrent(String host) {
        PreparedStatement statement = this.sqlite.preparedStatement("SELECT * FROM `punishments` WHERE host=?;");
        try {
            statement.setString(1, host);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String withUser = resultSet.getString("withUser");
                String operator = resultSet.getString("operator");
                String start_date = resultSet.getString("start_date");
                String end_date = resultSet.getString("end_date");
                String reason = resultSet.getString("reason");
                boolean unbanned = resultSet.getBoolean("unbanned");
                String end = resultSet.getString("end");
                if (resultSet.getBoolean("unbanned"))
                    continue;
                if (Objects.equals(end, "null")) {
                    return new DuObj<>(true, new BanIPPunishmentEntity(resultSet.getString("ban_id"), host, withUser, operator, start_date, end_date, unbanned, reason));
                } else {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
                    if (date.getTime() > new Date().getTime())
                        return new DuObj<>(true, new BanIPPunishmentEntity(resultSet.getString("ban_id"), host, withUser, operator, start_date, end_date, unbanned, reason));
                    else
                        unbanIPWithId(resultSet.getString("ban_id"));
                }
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        return new DuObj<>(false, null);
    }

    @Override
    public DuObj<Boolean, BanIPPunishment> getCurrentWithPlayer(String playerName) {
        PreparedStatement statement = this.sqlite.preparedStatement("SELECT * FROM `punishments` WHERE withUser=?;");
        try {
            statement.setString(1, playerName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long hostLong = resultSet.getLong("host");
                String host = (hostLong >>> 24) + "." + ((hostLong & 0x00FFFFFF) >>> 16) + "." + ((hostLong & 0x0000FFFF) >>> 8) + "." + (hostLong & 0x000000FF);
                String operator = resultSet.getString("operator");
                String start_date = resultSet.getString("start_date");
                String end_date = resultSet.getString("end_date");
                String reason = resultSet.getString("reason");
                boolean unbanned = resultSet.getBoolean("unbanned");
                String end = resultSet.getString("end");
                if (resultSet.getBoolean("unbanned"))
                    continue;
                if (Objects.equals(end, "null")) {
                    return new DuObj<>(true, new BanIPPunishmentEntity(resultSet.getString("ban_id"), host, playerName, operator, start_date, end_date, unbanned, reason));
                } else {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
                    if (date.getTime() > new Date().getTime())
                        return new DuObj<>(true, new BanIPPunishmentEntity(resultSet.getString("ban_id"), host, playerName, operator, start_date, end_date, unbanned, reason));
                    else
                        unbanIPWithId(resultSet.getString("ban_id"));
                }
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        return new DuObj<>(false, null);
    }

    @Override
    public boolean isBanned(String host) {
        PreparedStatement statement = this.sqlite.preparedStatement("SELECT * FROM `punishments` WHERE host=?;");
        try {
            statement.setString(1, host);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String end = resultSet.getString("end");
                if (resultSet.getBoolean("unbanned"))
                    continue;
                if (Objects.equals(end, "null")) {
                    return true;
                } else {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
                    if (date.getTime() > new Date().getTime())
                        return true;
                    else
                        unbanIPWithId(resultSet.getString("ban_id"));
                }
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean isBannedWithPlayer(PlayerUser user) {
        return this.isBannedWithPlayer(user.getName());
    }

    @Override
    public boolean isBannedWithPlayer(String playerName) {
        PreparedStatement statement = this.sqlite.preparedStatement("SELECT * FROM `punishments` WHERE withUser=?;");
        try {
            statement.setString(1, playerName.toLowerCase());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String end = resultSet.getString("end");
                if (resultSet.getBoolean("unbanned"))
                    continue;
                if (Objects.equals(end, "null")) {
                    return true;
                } else {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(end);
                    if (date.getTime() > new Date().getTime())
                        return true;
                    else
                        unbanIPWithId(resultSet.getString("ban_id"));
                }
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public BanIPPunishment get(String banId) {
        PreparedStatement statement = this.sqlite.preparedStatement("SELECT * FROM `punishments` WHERE ban_id=?;");
        try {
            statement.setString(1, banId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long hostLong = resultSet.getLong("host");
                String host = (hostLong >>> 24) + "." + ((hostLong & 0x00FFFFFF) >>> 16) + "." + ((hostLong & 0x0000FFFF) >>> 8) + "." + (hostLong & 0x000000FF);
                String withUser = resultSet.getString("withUser");
                String operator = resultSet.getString("operator");
                String start_date = resultSet.getString("start_date");
                String end_date = resultSet.getString("end_date");
                String reason = resultSet.getString("reason");
                boolean unbanned = resultSet.getBoolean("unbanned");
                return new BanIPPunishmentEntity(banId, host, withUser, operator, start_date, end_date, unbanned, reason);
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public BanIPPunishment banIP(String host, String operator, String reason, Date start, Date end) {
        PreparedStatement preparedStatement = this.sqlite.preparedStatement("INSERT INTO `punishments` (`ban_id`, `host`, `withUser`, `operator`, `start_date`, `end_date`, `reason`, `unbanned`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        String banId = StringUtils.random64();
        while (get(banId) != null) {
            banId = StringUtils.random64();
        }
        String[] sp = host.split("\\.");
        if (sp.length != 4)
            throw new RuntimeException("Invalided host");
        long[] spInt = new long[4];
        for (int i = 0; i < 4; i++) {
            try {
                spInt[i] = Long.parseLong(sp[i]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Host should be made of numbers!", e);
            }
        }
        try {
            preparedStatement.setString(1, banId);
            preparedStatement.setLong(2, (spInt[0] << 24) | (spInt[1] << 16) | (spInt[2] << 8) | spInt[3]);
            preparedStatement.setString(3, "null");
            preparedStatement.setString(4, operator);
            preparedStatement.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start));
            preparedStatement.setString(6, end != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end) : "null");
            preparedStatement.setString(7, reason);
            preparedStatement.setBoolean(8, false);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new BanIPPunishmentEntity(banId, host, null, operator, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start), end != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end) : "null", false, reason);
    }

    @Override
    public BanIPPunishment banIP(InetSocketAddress address, String operator, String reason, Date start, Date end) {
        return banIP(address.getHostString(), operator, reason, start, end);
    }

    @Override
    public BanIPPunishment banIPWithPlayer(PlayerUser user, String operator, String reason, Date start, Date end) {
        PreparedStatement preparedStatement = this.sqlite.preparedStatement("INSERT INTO `punishments` (`ban_id`, `host`, `withUser`, `operator`, `start_date`, `end_date`, `reason`, `unbanned`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
        String banId = StringUtils.random64();
        while (get(banId) != null) {
            banId = StringUtils.random64();
        }
        String host = ((PlayerBukkitUser) user).getSender().getAddress().getHostString();
        String[] sp = host.split("\\.");
        if (sp.length != 4)
            throw new RuntimeException("Invalided host");
        long[] spInt = new long[4];
        for (int i = 0; i < 4; i++) {
            try {
                spInt[i] = Long.parseLong(sp[i]);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Host should be made of numbers!", e);
            }
        }
        try {
            preparedStatement.setString(1, banId);
            preparedStatement.setLong(2, (spInt[0] << 24) | (spInt[1] << 16) | (spInt[2] << 8) | spInt[3]);
            preparedStatement.setString(3, user.getName().toLowerCase());
            preparedStatement.setString(4, operator);
            preparedStatement.setString(5, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start));
            preparedStatement.setString(6, end != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end) : "null");
            preparedStatement.setString(7, reason);
            preparedStatement.setBoolean(8, false);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new BanIPPunishmentEntity(banId, host, user.getName(), operator, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start), end != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end) : "null", false, reason);
    }

    @Override
    public void unbanIPWithId(String id) {
        PreparedStatement statement = this.sqlite.preparedStatement("UPDATE `punishments` SET unbanned=? WHERE ban_id=?;");
        try {
            statement.setBoolean(1, true);
            statement.setString(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unbanIP(String host) {
        PreparedStatement statement = this.sqlite.preparedStatement("UPDATE `punishments` SET unbanned=? WHERE host=?;");
        try {
            statement.setBoolean(1, true);
            statement.setString(2, host);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unbanIP(InetSocketAddress address) {
        this.unbanIP(address.getHostString());
    }

    @Override
    public void unbanIPWithPlayer(UUID uuid) {
        PreparedStatement statement = this.sqlite.preparedStatement("UPDATE `punishments` SET unbanned=? WHERE withUser=?;");
        try {
            statement.setBoolean(1, true);
            statement.setString(2, Bukkit.getOfflinePlayer(uuid).getName().toLowerCase());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unbanIPWithPlayer(String playerName) {
        PreparedStatement statement = this.sqlite.preparedStatement("UPDATE `punishments` SET unbanned=? WHERE withUser=?;");
        try {
            statement.setBoolean(1, true);
            statement.setString(2, Bukkit.getOfflinePlayer(playerName).getName().toLowerCase());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private DynamicBanIPManager() {}

}
