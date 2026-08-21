package com.example.order4;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class PersonInfoShipTo {

    @JacksonXmlProperty(isAttribute = true, localName = "Country")
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}