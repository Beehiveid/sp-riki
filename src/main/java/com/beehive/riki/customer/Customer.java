package com.beehive.riki.customer;

import com.beehive.riki.common.AuditorBase;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Customer extends AuditorBase {
    @Column(nullable = false,length = 60)
    private String name;

    @Column(columnDefinition = "text")
    private String address;

    @Column(length = 15)
    private String mobile1;

    @Column(length = 15)
    private String mobile2;

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

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public Customer() {

    }
}
