package com.beehive.riki.order;

import com.beehive.riki.common.AuditorBase;
import com.beehive.riki.customer.Customer;
import com.beehive.riki.evidence.Evidence;
import com.beehive.riki.salesPerson.SalesPerson;
import com.beehive.riki.services.Services;
import com.beehive.riki.sto.Sto;
import com.beehive.riki.technician.Technician;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Order extends AuditorBase {
    @Column(unique = true)
    private String trackId;

    @Column(unique = true)
    private String scNumber;

    private short inputStatus;
    private String odp;
    private double latitude;
    private double longitude;
    private String barcode;
    private String port;
    private Date closeDate;
    private short status;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne
    @JoinColumn(name = "sales_person_id")
    private SalesPerson salesPerson;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @ManyToOne
    @JoinColumn(name = "sto_id")
    private Sto sto;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services service;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id")
    private List<Evidence> evidences = new ArrayList<>();

    public List<Evidence> getEvidences() {
        return evidences;
    }

    public void setEvidences(List<Evidence> evidences) {
        this.evidences = evidences;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getScNumber() {
        return scNumber;
    }

    public void setScNumber(String scNumber) {
        this.scNumber = scNumber;
    }

    public short getInputStatus() {
        return inputStatus;
    }

    public void setInputStatus(short inputStatus) {
        this.inputStatus = inputStatus;
    }

    public String getOdp() {
        return odp;
    }

    public void setOdp(String odp) {
        this.odp = odp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SalesPerson getSalesPerson() {
        return salesPerson;
    }

    public void setSalesPerson(SalesPerson salesPerson) {
        this.salesPerson = salesPerson;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public Sto getSto() {
        return sto;
    }

    public void setSto(Sto sto) {
        this.sto = sto;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Services getService() {
        return service;
    }

    public void setService(Services service) {
        this.service = service;
    }

    public Order() {
    }
}
