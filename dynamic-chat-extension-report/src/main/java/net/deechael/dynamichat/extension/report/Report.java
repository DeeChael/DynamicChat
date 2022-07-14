package net.deechael.dynamichat.extension.report;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Report {

    private final File file;
    private final String reporterName;
    private final UUID reporterUUID;
    private final String suspectName;
    private final UUID suspectUUID;
    private final Date createDate;
    private final String reason;
    private boolean approached;
    private String admin;

    private Date solveDate;

    private String adminReply;

    public Report(File file, String reporterName, String reporterUUID, String suspectName, String suspectUUID, Date createDate, String reason, boolean approached, String admin) {
        this.file = file;
        this.reporterName = reporterName;
        this.reporterUUID = UUID.fromString(reporterUUID);
        this.suspectName = suspectName;
        this.suspectUUID = UUID.fromString(suspectUUID);
        this.createDate = createDate;
        this.reason = reason;
        this.approached = approached;
        this.admin = admin;
    }

    public void setApproached(boolean approached) {
        this.approached = approached;
    }

    public File getReportFile() {
        return file;
    }

    public String getReporterName() {
        return reporterName;
    }

    public UUID getReporterUUID() {
        return reporterUUID;
    }

    public String getSuspectName() {
        return suspectName;
    }

    public UUID getSuspectUUID() {
        return suspectUUID;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getCreateDateString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getCreateDate());
    }

    public String getReason() {
        return reason;
    }

    public boolean isApproached() {
        return approached;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public void setSolveDate(String solveDate) {
        try {
            this.solveDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(solveDate);
        } catch (ParseException ignored) {
        }
    }

    public void setAdminReply(String adminReply) {
        this.adminReply = adminReply;
    }

    public Date getSolveDate() {
        return solveDate;
    }

    public String getSolveDateString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(solveDate);
    }

    public String getAdminReply() {
        return adminReply;
    }
}
