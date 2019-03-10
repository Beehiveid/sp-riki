package com.beehive.riki.serviceRequestOrder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log_service_request_order")
public class Logger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date systemDate;
    private String sroId;
    private int sroStatus;
    private String notes;

    public Logger(Date systemDate, String sroId, int sroStatus, String notes) {
        this.setNotes(notes);
        this.setSroId(sroId);
        this.setSroStatus(sroStatus);
        this.setSystemDate(systemDate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(Date systemDate) {
        this.systemDate = systemDate;
    }

    public String getSroId() {
        return sroId;
    }

    public void setSroId(String sroId) {
        this.sroId = sroId;
    }

    public int getSroStatus() {
        return sroStatus;
    }

    public void setSroStatus(int sroStatus) {
        this.sroStatus = sroStatus;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Logger() {

    }
}
