package net.deechael.dynamichat.extension.report;

import net.deechael.dynamichat.api.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class ReportManager {

    private final static Map<UUID, Date> lastReport = new HashMap<>();

    private final static Map<UUID,List<String>> reported = new HashMap<>();

    public static Date getLastReport(UUID uuid) {
        return lastReport.get(uuid);
    }

    public static boolean isReported(UUID uuid, Message message) {
        List<String> ids = reported.get(uuid);
        if (ids != null) {
            return ids.contains(message.getId());
        }
        return false;
    }

    public static void reportMessage(Player reporter, Player suspect, String message, String messageId, String reason) {
        lastReport.put(reporter.getUniqueId(), new Date());
        if (!reported.containsKey(reporter.getUniqueId())) {
            reported.put(reporter.getUniqueId(), new ArrayList<>());
        }
        if (reported.get(reporter.getUniqueId()).contains(messageId))
            return;
        reported.get(reporter.getUniqueId()).add(messageId);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("dynamichat.report.admin")) {
                Lang.send(player, "message.mention.admin");
            }
        }
        File directory = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "message-reports/" + reporter.getUniqueId());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String time = "report-message-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        File reportFile = new File(directory, time + ".yml");
        int repeatTimes = 0;
        while (reportFile.exists()) {
            reportFile = new File(directory, time + "-" + repeatTimes + ".yml");
            repeatTimes++;
        }
        try {
            reportFile.createNewFile();
        } catch (IOException ignored) {
        }
        FileConfiguration configuration = new YamlConfiguration();
        configuration.set("reporter", reporter.getName());
        configuration.set("reporter-uuid", reporter.getUniqueId().toString());
        configuration.set("suspect", suspect.getName());
        configuration.set("suspect-uuid", suspect.getUniqueId().toString());
        configuration.set("message-id", messageId);
        configuration.set("message-content", message);
        configuration.set("create-date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        configuration.set("reason", reason);
        configuration.set("approached", false);
        configuration.set("admin", "null");
        try {
            configuration.save(reportFile);
        } catch (IOException ignored) {
        }
    }

    public static void report(Player reporter, Player suspect, String reason) {
        lastReport.put(reporter.getUniqueId(), new Date());
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("dynamichat.report.admin")) {
                Lang.send(player, "message.mention.admin");
            }
        }
        File directory = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "reports/" + reporter.getUniqueId());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String time = "report-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        File reportFile = new File(directory, time + ".yml");
        int repeatTimes = 0;
        while (reportFile.exists()) {
            reportFile = new File(directory, time + "-" + repeatTimes + ".yml");
            repeatTimes++;
        }
        try {
            reportFile.createNewFile();
        } catch (IOException ignored) {
        }
        FileConfiguration configuration = new YamlConfiguration();
        configuration.set("reporter", reporter.getName());
        configuration.set("reporter-uuid", reporter.getUniqueId().toString());
        configuration.set("suspect", suspect.getName());
        configuration.set("suspect-uuid", suspect.getUniqueId().toString());
        configuration.set("create-date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        configuration.set("reason", reason);
        configuration.set("approached", false);
        configuration.set("admin", "null");
        try {
            configuration.save(reportFile);
        } catch (IOException ignored) {
        }
    }

    public static List<Report> getOnesReports(Player player) {
        try {
            return getOnesReports0(player);
        } catch (ParseException e) {
            return new ArrayList<>();
        }
    }

    private static List<Report> getOnesReports0(Player player) throws ParseException {
        List<Report> list = new ArrayList<>();
        File directory = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "reports/" + player.getUniqueId());
        File[] reports = directory.listFiles();
        if (reports != null) {
            for (File file : reports) {
                if (!file.getName().endsWith(".yml"))
                    continue;
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                Report report = new Report(file,
                        configuration.getString("reporter"),
                        configuration.getString("reporter-uuid"),
                        configuration.getString("suspect"),
                        configuration.getString("suspect-uuid"),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(configuration.getString("create-date")),
                        configuration.getString("reason"),
                        configuration.getBoolean("approached"),
                        configuration.getString("admin"));
                if (configuration.contains("solve-date")) {
                    report.setSolveDate(configuration.getString("solve-date"));
                }
                if (configuration.contains("admin-comment")) {
                    report.setAdminReply(configuration.getString("admin-comment"));
                }
                list.add(report);
            }
        }
        directory = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "message-reports/" + player.getUniqueId());
        reports = directory.listFiles();
        if (reports != null) {
            for (File file : reports) {
                if (!file.getName().endsWith(".yml"))
                    continue;
                FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                MessageReport report = new MessageReport(file,
                        configuration.getString("reporter"),
                        configuration.getString("reporter-uuid"),
                        configuration.getString("suspect"),
                        configuration.getString("suspect-uuid"),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(configuration.getString("create-date")),
                        configuration.getString("reason"),
                        configuration.getBoolean("approached"),
                        configuration.getString("admin"),
                        configuration.getString("message-content"),
                        configuration.getString("message-id")
                );
                if (configuration.contains("solve-date")) {
                    report.setSolveDate(configuration.getString("solve-date"));
                }
                if (configuration.contains("admin-comment")) {
                    report.setAdminReply(configuration.getString("admin-comment"));
                }
                list.add(report);
            }
        }
        list.sort((report1, report2) -> {
            if (report1.isApproached() && report2.isApproached()) {
                return 0;
            } else if (report1.isApproached()) {
                return 1;
            }
            return -1;
        });
        return list;
    }

    public static List<Report> getReports() {
        try {
            return getReports0();
        } catch (ParseException e) {
            return new ArrayList<>();
        }
    }

    private static List<Report> getReports0() throws ParseException {
        List<Report> list = new ArrayList<>();
        File directory = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "reports");
        File[] reports = directory.listFiles();
        if (reports != null) {
            reports = Arrays.stream(reports).filter(File::isDirectory).toList().toArray(new File[0]);
            for (File directoriesFile : reports) {
                File[] directories = directoriesFile.listFiles();
                if (directories == null)
                    continue;
                for (File file : directories) {
                    if (!file.getName().endsWith(".yml"))
                        continue;
                    FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    Report report = new Report(file,
                            configuration.getString("reporter"),
                            configuration.getString("reporter-uuid"),
                            configuration.getString("suspect"),
                            configuration.getString("suspect-uuid"),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(configuration.getString("create-date")),
                            configuration.getString("reason"),
                            configuration.getBoolean("approached"),
                            configuration.getString("admin"));
                    if (configuration.contains("solve-date")) {
                        report.setSolveDate(configuration.getString("solve-date"));
                    }
                    if (configuration.contains("admin-comment")) {
                        report.setAdminReply(configuration.getString("admin-comment"));
                    }
                    list.add(report);
                }
            }
        }
        directory = new File(DyChatReportExtensionPlugin.getInstance().getDataFolder(), "message-reports");
        reports = directory.listFiles();
        if (reports != null) {
            reports = Arrays.stream(reports).filter(File::isDirectory).toList().toArray(new File[0]);
            for (File directoriesFile : reports) {
                File[] directories = directoriesFile.listFiles();
                if (directories == null)
                    continue;
                for (File file : directories) {
                    if (!file.getName().endsWith(".yml"))
                        continue;
                    FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    MessageReport report = new MessageReport(file,
                            configuration.getString("reporter"),
                            configuration.getString("reporter-uuid"),
                            configuration.getString("suspect"),
                            configuration.getString("suspect-uuid"),
                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(configuration.getString("create-date")),
                            configuration.getString("reason"),
                            configuration.getBoolean("approached"),
                            configuration.getString("admin"),
                            configuration.getString("message-content"),
                            configuration.getString("message-id")
                    );
                    if (configuration.contains("solve-date")) {
                        report.setSolveDate(configuration.getString("solve-date"));
                    }
                    if (configuration.contains("admin-comment")) {
                        report.setAdminReply(configuration.getString("admin-comment"));
                    }
                    list.add(report);
                }
            }
        }
        list.sort((report1, report2) -> {
            if (report1.isApproached() && report2.isApproached()) {
                return 0;
            } else if (report1.isApproached()) {
                return 1;
            }
            return -1;
        });
        return list;
    }

    public static void solveReport(Report report) {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(report.getReportFile());
        if (!report.isApproached()) {
            configuration.set("approached", false);
            configuration.set("admin", "null");
        } else {
            configuration.set("approached", true);
            configuration.set("admin", report.getAdmin());
            configuration.set("solve-date", report.getSolveDateString());
            configuration.set("admin-comment", report.getAdminReply());
        }
        try {
            configuration.save(report.getReportFile());
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(report.getReporterUUID());
            if (offlinePlayer.isOnline() && Config.mentionReporter()) {
                Lang.send((Player) offlinePlayer, "message.mention.player");
            }
        } catch (IOException ignored) {
        }
    }

}
