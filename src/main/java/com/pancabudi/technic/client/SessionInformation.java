package com.pancabudi.technic.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SessionInformation extends Client {
    private ClientInformation clientInformation;

    SessionInformation(Client c) throws IOException {
        super(c);

        if(c.getInformation() != null)
        this.clientInformation = new ObjectMapper().readValue(this.getInformation(), ClientInformation.class);
    }

    public ClientInformation getClientInformation() {
        return clientInformation;
    }

    public void setClientInformation(ClientInformation clientInformation) {
        this.clientInformation = clientInformation;
    }

    @Override
    @JsonIgnore
    public String getInformation() {
        return super.getInformation();
    }

    @Override
    @JsonIgnore
    public String getLocation() {
        return super.getLocation();
    }

    @Override
    @JsonIgnore
    public Long getPid() {
        return super.getPid();
    }
}
