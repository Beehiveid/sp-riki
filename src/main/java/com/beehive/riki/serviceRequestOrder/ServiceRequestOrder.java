package com.beehive.riki.serviceRequestOrder;

import com.beehive.riki.complaintType.ComplaintType;
import com.beehive.riki.machine.Machine;
import com.beehive.riki.person.Person;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ServiceRequestOrder {
    @Id
    private String id;

    private Date createdTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="machine_id",nullable = false)
    private Machine machine;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rel_sro_complaint",
            joinColumns = @JoinColumn(name = "sro_id"),
            inverseJoinColumns = @JoinColumn(name = "complaint_type_id"))
    private Set<ComplaintType> complaintTypes = new HashSet<>();

    private String note;
    private Date doneTime;

    @ApiModelProperty(
            value = "[0:Cancel, 1:Done, 9:Idle]"
    )
    private int status;

    @ApiModelProperty(
            value = "[1:Now, 2:In a day, 3:In a week, 4:In a month, 5:Next maintenance]"
    )
    private int priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id")
    private Person requester;

    public ServiceRequestOrder() {
    }

    ServiceRequestOrder(String id) {
        this.id = id;
    }

    public Person getRequester() {
        return requester;
    }

    public void setRequester(Person requester) {
        this.requester = requester;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Set<ComplaintType> getComplaintTypes() {
        return complaintTypes;
    }

    public void setComplaintTypes(Set<ComplaintType> complaintTypes) {
        this.complaintTypes = complaintTypes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(Date doneTime) {
        this.doneTime = doneTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
