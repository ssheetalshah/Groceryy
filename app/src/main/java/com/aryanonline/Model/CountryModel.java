package com.aryanonline.Model;

import java.io.Serializable;

public class CountryModel implements Serializable {

    private String countryId;
    private String name;
    private String isoCode2;
    private String isoCode3;
    private String addressFormat;
    private String postcodeRequired;
    private String status;

    public CountryModel(String countryId, String name, String isoCode2, String isoCode3, String addressFormat, String postcodeRequired, String status) {
        this.countryId = countryId;
        this.name = name;
        this.isoCode2 = isoCode2;
        this.isoCode3 = isoCode3;
        this.addressFormat = addressFormat;
        this.postcodeRequired = postcodeRequired;
        this.status = status;
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

    public String getIsoCode2() {
        return isoCode2;
    }

    public void setIsoCode2(String isoCode2) {
        this.isoCode2 = isoCode2;
    }

    public String getIsoCode3() {
        return isoCode3;
    }

    public void setIsoCode3(String isoCode3) {
        this.isoCode3 = isoCode3;
    }

    public String getAddressFormat() {
        return addressFormat;
    }

    public void setAddressFormat(String addressFormat) {
        this.addressFormat = addressFormat;
    }

    public String getPostcodeRequired() {
        return postcodeRequired;
    }

    public void setPostcodeRequired(String postcodeRequired) {
        this.postcodeRequired = postcodeRequired;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
