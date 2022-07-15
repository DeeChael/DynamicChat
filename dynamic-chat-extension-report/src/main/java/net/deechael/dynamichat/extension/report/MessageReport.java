package net.deechael.dynamichat.extension.report;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class MessageReport extends Report {

    private final String message;

    private final String id;

    public MessageReport(File file, String reporterName, String reporterUUID, String suspectName, String suspectUUID, Date createDate, String reason, boolean approached, String admin, String message, String id) {
        super(file, reporterName, reporterUUID, suspectName, suspectUUID, createDate, reason, approached, admin);
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }
}
