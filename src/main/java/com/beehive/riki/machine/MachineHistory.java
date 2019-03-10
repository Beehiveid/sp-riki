package com.beehive.riki.machine;

import com.beehive.riki.person.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pancabudi.technic.person.Person;
import com.pancabudi.technic.serviceRequestOrder.ServiceRequestOrderHandling;

public class MachineHistory extends ServiceRequestOrderHandling {
    private String technician;

    MachineHistory(ServiceRequestOrderHandling log) {
        super(log);
    }

    public String getTechnician() {
        return super.getHandler().getName();
    }

    @Override
    @JsonIgnore
    public Person getHandler() {
        return super.getHandler();
    }
}
