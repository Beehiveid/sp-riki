package com.beehive.riki.services;

import com.beehive.riki.common.AuditorBase;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Services extends AuditorBase {
    @Column(length = 60)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Services() {
    }
}
