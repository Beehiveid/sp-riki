package com.beehive.riki.serviceRequestOrder;

import java.util.ArrayList;
import java.util.List;

public class SROConsentFlow {
    private String flow;
    private List<Integer> roles =  new ArrayList<>();

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }

    public SROConsentFlow() {

    }
}
