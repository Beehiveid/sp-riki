package com.pancabudi.technic.serviceRequestOrder;

import java.util.Date;

public class SROReportData {
    private Date createdDate;
    private String requester;
    private String complaint;
    private Date doneTime;
    private String handler;
    private String handling;
    private String part;
    private String result;
    private String machine;
    private Date firstResponseDate;

    public String getMachine() {
        return machine;
    }

    public Date getFirstResponseDate() {
        return firstResponseDate;
    }

    public void setFirstResponseDate(Date firstResponseDate) {
        this.firstResponseDate = firstResponseDate;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public Date getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(Date doneTime) {
        this.doneTime = doneTime;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getHandling() {
        return handling;
    }

    public void setHandling(String handling) {
        this.handling = handling;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public SROReportData() {

    }
}
