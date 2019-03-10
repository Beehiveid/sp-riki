package com.pancabudi.technic.client;

import net.sf.uadetector.ReadableUserAgent;

public class ClientInformation {
    private String deviceCategory;
    private String os;
    private String type;
    private String name;
    private String version;

    public ClientInformation(ReadableUserAgent meta) {
        this.deviceCategory = meta.getDeviceCategory().getName();
        this.os = meta.getOperatingSystem().getName() +" "+ meta.getOperatingSystem().getVersionNumber().toVersionString();
        this.type = meta.getTypeName();
        this.name = meta.getName();
        this.version = meta.getVersionNumber().toVersionString();
    }

    public String getDeviceCategory() {
        return deviceCategory;
    }

    public void setDeviceCategory(String deviceCategory) {
        this.deviceCategory = deviceCategory;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ClientInformation() {

    }
}
