package com.beehive.riki.serviceRequestOrder;

import com.beehive.riki.person.Person;

import javax.persistence.*;
import java.util.Date;
@Entity
public class ServiceRequestOrderHandling {
    @Id
    private String id;

    private Date systemDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="handler")
    private Person handler;

    @Column(nullable = false)
    private String handling;
    private String result;
    private String part;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_request_order_id")
    private ServiceRequestOrder serviceRequestOrder;

    public ServiceRequestOrderHandling(ServiceRequestOrderHandling log) {
        this.id = log.getId();
        this.handler = log.getHandler();
        this.handling = log.getHandling();
        this.part = log.getPart();
        this.result = log.getResult();
        this.serviceRequestOrder = log.getServiceRequestOrder();
        this.systemDate = log.getSystemDate();
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(Date systemDate) {
        this.systemDate = systemDate;
    }

    public Person getHandler() {
        return handler;
    }

    public void setHandler(Person handler) {
        this.handler = handler;
    }

    public String getHandling() {
        return handling;
    }

    public void setHandling(String handling) {
        this.handling = handling;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ServiceRequestOrder getServiceRequestOrder() {
        return serviceRequestOrder;
    }

    public void setServiceRequestOrder(ServiceRequestOrder serviceRequestOrder) {
        this.serviceRequestOrder = serviceRequestOrder;
    }

    public ServiceRequestOrderHandling() {

    }
}
