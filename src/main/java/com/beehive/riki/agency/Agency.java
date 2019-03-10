package com.beehive.riki.agency;

import com.beehive.riki.common.AuditorBase;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Agency extends AuditorBase {
    @Column(length = 60)
    private String name;

    @Column(columnDefinition = "text")
    private String address;

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

    public Agency(String name) {
        this.name = name;
    }
}
