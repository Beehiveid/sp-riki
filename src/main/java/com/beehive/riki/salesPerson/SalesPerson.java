package com.beehive.riki.salesPerson;

import com.beehive.riki.agency.Agency;
import com.beehive.riki.common.AuditorBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SalesPerson extends AuditorBase {
    @Column(length = 60)
    private String name;

    @Column(columnDefinition = "text")
    private String address;

    @Column(length = 15)
    private String mobile;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public SalesPerson() {
    }
}
