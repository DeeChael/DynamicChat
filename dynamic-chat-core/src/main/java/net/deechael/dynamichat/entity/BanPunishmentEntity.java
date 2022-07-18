package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.object.Punishment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BanPunishmentEntity implements Punishment {

    private final String id;
    private final String username;
    private final String operator;
    private final Date startDate;
    private final Date endDate;
    private final boolean unbanned;
    private final String reason;

    public BanPunishmentEntity(String id, String username, String operator, String startDate, String endDate, boolean unbanned, String reason) {
        this.id = id;
        this.username = username;
        this.operator = operator;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.startDate = formatter.parse(startDate);
            this.endDate = endDate != null ? formatter.parse(endDate) : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.unbanned = unbanned;
        this.reason = reason;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getOperator() {
        return this.operator;
    }

    @Override
    public Type getType() {
        return Type.BAN;
    }

    @Override
    public Date getStartDate() {
        return this.startDate;
    }

    @Override
    public Date getEndDate() {
        return this.endDate;
    }

    @Override
    public boolean hasUnbanned() {
        return this.unbanned;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public boolean isForever() {
        return this.getEndDate() == null;
    }

}
