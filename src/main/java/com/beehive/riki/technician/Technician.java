package com.beehive.riki.technician;

import com.beehive.riki.common.AuditorBase;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Technician extends AuditorBase {
    @Column(length = 60)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Technician(String name) {
        this.name = name;
    }
}
