package com.aryanonline.Model;

import java.io.Serializable;

public class StateModel implements Serializable {

    private String zoneId;
    private String countryId;
    private String name;
    private String code;
    private String status;

    public StateModel(String zoneId, String countryId, String name, String code, String status) {
        this.zoneId = zoneId;
        this.countryId = countryId;
        this.name = name;
        this.code = code;
        this.status = status;
    }


    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
