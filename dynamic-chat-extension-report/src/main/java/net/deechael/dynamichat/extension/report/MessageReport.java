package net.deechael.dynamichat.extension.report;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class MessageReport extends Report {

    private final String message;

    public MessageReport(File file, String reporterName, String reporterUUID, String suspectName, String suspectUUID, Date createDate, String reason, boolean approached, String admin, String message) {
        super(file, reporterName, reporterUUID, suspectName, suspectUUID, createDate, reason, approached, admin);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
