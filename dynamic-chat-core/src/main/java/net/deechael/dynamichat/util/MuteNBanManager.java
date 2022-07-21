package net.deechael.dynamichat.util;

import net.deechael.dynamichat.DyChatPlugin;
import net.deechael.dynamichat.entity.BanPunishmentEntity;
import net.deechael.dynamichat.entity.MutePunishmentEntity;
import net.deechael.dynamichat.api.Punishment;
import net.deechael.useless.objs.FoObj;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public final class MuteNBanManager {

    public static FoObj<Boolean, String, Date, String> isNowBanned(OfflinePlayer player) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("banned-history")) {
                ConfigurationSection section = configuration.getConfigurationSection("banned-history");
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        if (section.contains(key + ".unbanned") && section.getBoolean(key + ".unbanned"))
                            continue;
                        if (section.contains(key + ".end-date")) {
                            long endDate = section.getLong(key + ".end-date");
                            if (new Date().getTime() < endDate)
                                return new FoObj<>(true, section.getString(key + ".reason"), new Date(endDate), key);
                            else {
                                if (!(section.contains(key + ".unbanned") && section.getBoolean(key + ".unbanned"))) {
                                    unbanned(player, key);
                                }
                            }
                        } else {
                            return new FoObj<>(true, section.getString(key + ".reason"), null, key);
                        }
                    }
                }
            }
        }
        return new FoObj<>(false, null, null, null);
    }

    public static FoObj<Boolean, String, Date, String> isNowMuted(OfflinePlayer player) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("muted-history")) {
                ConfigurationSection section = configuration.getConfigurationSection("muted-history");
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        if (section.contains(key + ".unbanned") && section.getBoolean(key + ".unbanned"))
                            continue;
                        if (section.contains(key + ".end-date")) {
                            long endDate = section.getLong(key + ".end-date");
                            if (new Date().getTime() < endDate)
                                return new FoObj<>(true, section.getString(key + ".reason"), new Date(endDate), key);
                            else {
                                if (!(section.contains(key + ".unbanned") && section.getBoolean(key + ".unbanned"))) {
                                    unmuted(player, key);
                                }
                            }
                        } else {
                            return new FoObj<>(true, section.getString(key + ".reason"), null, key);
                        }
                    }
                }
            }
        }
        return new FoObj<>(false, null, null, null);
    }

    public static BanPunishmentEntity addBanned(String operator, OfflinePlayer player, Date endDate, String reason) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        File parent = playerFile.getParentFile();
        if (parent != null)
            if (!(parent.exists() && parent.isDirectory()))
                parent.mkdirs();
        if (playerFile.exists() && playerFile.isFile()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
        String key;
        if (configuration.contains("banned-history")) {
            ConfigurationSection section = configuration.getConfigurationSection("banned-history");
            if (section != null) {
                Collection<String> existedKeys = section.getKeys(false);
                key = StringUtils.random64();
                while (existedKeys.contains(key)) {
                    key = StringUtils.random64();
                }
                configuration.set("banned-history." + key + ".operator", operator);
                configuration.set("banned-history." + key + ".start-date", new Date().getTime());
                if (endDate != null) {
                    configuration.set("banned-history." + key + ".end-date", endDate.getTime());
                }
                configuration.set("banned-history." + key + ".reason", reason);
                configuration.set("banned-history." + key + ".unbanned", false);
            } else {
                key = StringUtils.random64();
                configuration.set("banned-history." + key + ".operator", operator);
                configuration.set("banned-history." + key + ".start-date", new Date().getTime());
                if (endDate != null) {
                    configuration.set("banned-history." + key + ".end-date", endDate.getTime());
                }
                configuration.set("banned-history." + key + ".reason", reason);
                configuration.set("banned-history." + key + ".unbanned", false);
            }
        } else {
            key = StringUtils.random64();
            configuration.set("banned-history." + key + ".operator", operator);
            configuration.set("banned-history." + key + ".start-date", new Date().getTime());
            if (endDate != null) {
                configuration.set("banned-history." + key + ".end-date", endDate.getTime());
            }
            configuration.set("banned-history." + key + ".reason", reason);
            configuration.set("banned-history." + key + ".unbanned", false);
        }
        try {
            configuration.save(playerFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new BanPunishmentEntity(key, player.getName(), operator, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), endDate != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate) : null, false, reason);
    }

    public static void unbanned(OfflinePlayer player, String bannedId) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("banned-history")) {
                ConfigurationSection section = configuration.getConfigurationSection("banned-history");
                if (section != null) {
                    if (section.contains(bannedId)) {
                        configuration.set("banned-history." + bannedId + ".unbanned", true);
                        try {
                            configuration.save(playerFile);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public static MutePunishmentEntity addMuted(String operator, OfflinePlayer player, Date endDate, String reason) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        File parent = playerFile.getParentFile();
        if (parent != null)
            if (!(parent.exists() && parent.isDirectory()))
                parent.mkdirs();
        if (playerFile.exists() && playerFile.isFile()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
        String key;
        if (configuration.contains("muted-history")) {
            ConfigurationSection section = configuration.getConfigurationSection("muted-history");
            if (section != null) {
                Collection<String> existedKeys = section.getKeys(false);
                key = StringUtils.random64();
                while (existedKeys.contains(key)) {
                    key = StringUtils.random64();
                }
                configuration.set("muted-history." + key + ".operator", operator);
                configuration.set("muted-history." + key + ".start-date", new Date().getTime());
                if (endDate != null) {
                    configuration.set("muted-history." + key + ".end-date", endDate.getTime());
                }
                configuration.set("muted-history." + key + ".reason", reason);
                configuration.set("muted-history." + key + ".unbanned", false);
            } else {
                key = StringUtils.random64();
                configuration.set("muted-history." + key + ".operator", operator);
                configuration.set("muted-history." + key + ".start-date", new Date().getTime());
                if (endDate != null) {
                    configuration.set("muted-history." + key + ".end-date", endDate.getTime());
                }
                configuration.set("muted-history." + key + ".reason", reason);
                configuration.set("muted-history." + key + ".unbanned", false);
            }
        } else {
            key = StringUtils.random64();
            configuration.set("muted-history." + key + ".operator", operator);
            configuration.set("muted-history." + key + ".start-date", new Date().getTime());
            if (endDate != null) {
                configuration.set("muted-history." + key + ".end-date", endDate.getTime());
            }
            configuration.set("muted-history." + key + ".reason", reason);
            configuration.set("muted-history." + key + ".unbanned", false);
        }
        try {
            configuration.save(playerFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new MutePunishmentEntity(key, player.getName(), operator, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), endDate != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate) : null, false, reason);
    }

    public static void unmuted(OfflinePlayer player, String bannedId) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("muted-history")) {
                ConfigurationSection section = configuration.getConfigurationSection("muted-history");
                if (section != null) {
                    if (section.contains(bannedId)) {
                        configuration.set("muted-history." + bannedId + ".unbanned", true);
                        try {
                            configuration.save(playerFile);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public static List<Punishment> getBannedHistory(OfflinePlayer player) {
        List<Punishment> punishments = new ArrayList<>();
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("banned-history")) {
                ConfigurationSection section = configuration.getConfigurationSection("banned-history");
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        if (!(section.contains(key + ".operator") && section.isString(key + ".operator")))
                            continue;
                        if (!(section.contains(key + ".start-date") && section.isLong(key + ".start-date")))
                            continue;
                        if (!(section.contains(key + ".unbanned") && section.isBoolean(key + ".unbanned")))
                            continue;
                        String operator = section.getString(key + ".operator");
                        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(section.getLong("start-date")));
                        String end = null;
                        if ((section.contains(key + ".start-date") && section.isLong(key + ".start-date")))
                            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(section.getLong("end-date")));
                        String reason = section.getString(key + ".reason", null);
                        punishments.add(new BanPunishmentEntity(key, player.getName(), operator, start, end, section.getBoolean(key + ".unbanned"), reason));
                    }
                }
            }
        }
        return punishments;
    }

    public static List<Punishment> getMutedHistory(OfflinePlayer player) {
        List<Punishment> punishments = new ArrayList<>();
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("muted-history")) {
                ConfigurationSection section = configuration.getConfigurationSection("muted-history");
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        if (!(section.contains(key + ".operator") && section.isString(key + ".operator")))
                            continue;
                        if (!(section.contains(key + ".start-date") && section.isLong(key + ".start-date")))
                            continue;
                        if (!(section.contains(key + ".unbanned") && section.isBoolean(key + ".unbanned")))
                            continue;
                        String operator = section.getString(key + ".operator");
                        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(section.getLong("start-date")));
                        String end = null;
                        if ((section.contains(key + ".start-date") && section.isLong(key + ".start-date")))
                            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(section.getLong("end-date")));
                        String reason = section.getString(key + ".reason", null);
                        punishments.add(new MutePunishmentEntity(key, player.getName(), operator, start, end, section.getBoolean(key + ".unbanned"), reason));
                    }
                }
            }
        }
        return punishments;
    }

    public static Punishment getBanById(OfflinePlayer player, String id) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("banned-history")) {
                ConfigurationSection section = configuration.getConfigurationSection("banned-history");
                if (section != null) {
                    if (section.contains(id)) {
                        String key = id;
                        if (!(section.contains(key + ".operator") && section.isString(key + ".operator")))
                            return null;
                        if (!(section.contains(key + ".start-date") && section.isLong(key + ".start-date")))
                            return null;
                        if (!(section.contains(key + ".unbanned") && section.isBoolean(key + ".unbanned")))
                            return null;
                        String operator = section.getString(key + ".operator");
                        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(section.getLong("start-date")));
                        String end = null;
                        if ((section.contains(key + ".start-date") && section.isLong(key + ".start-date")))
                            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(section.getLong("end-date")));
                        String reason = section.getString(key + ".reason", null);
                        return new BanPunishmentEntity(id, player.getName(), operator, start, end, section.getBoolean(key + ".unbanned"), reason);
                    }
                }
            }
        }
        return null;
    }

    public static Punishment getMuteById(OfflinePlayer player, String id) {
        File playerFile = new File(new File(DyChatPlugin.getInstance().getDataFolder(), "players"), player.getUniqueId() + ".yml");
        if (playerFile.exists() && playerFile.isFile()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(playerFile);
            if (configuration.contains("muted-history")) {
                ConfigurationSection section = configuration.getConfigurationSection("muted-history");
                if (section != null) {
                    if (section.contains(id)) {
                        String key = id;
                        if (!(section.contains(key + ".operator") && section.isString(key + ".operator")))
                            return null;
                        if (!(section.contains(key + ".start-date") && section.isLong(key + ".start-date")))
                            return null;
                        if (!(section.contains(key + ".unbanned") && section.isBoolean(key + ".unbanned")))
                            return null;
                        String operator = section.getString(key + ".operator");
                        String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(section.getLong("start-date")));
                        String end = null;
                        if ((section.contains(key + ".start-date") && section.isLong(key + ".start-date")))
                            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(section.getLong("end-date")));
                        String reason = section.getString(key + ".reason", null);
                        return new BanPunishmentEntity(id, player.getName(), operator, start, end, section.getBoolean(key + ".unbanned"), reason);
                    }
                }
            }
        }
        return null;
    }

    private MuteNBanManager() {
    }

}
