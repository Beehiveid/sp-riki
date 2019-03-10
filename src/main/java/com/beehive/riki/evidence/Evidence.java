package com.beehive.riki.evidence;

import com.beehive.riki.common.AuditorBase;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Evidence extends AuditorBase {
    @Column(length = 100)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    public Evidence() {
    }

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
}
