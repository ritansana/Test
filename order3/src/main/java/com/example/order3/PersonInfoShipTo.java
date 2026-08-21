package com.example.order3;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class PersonInfoShipTo {

    @JacksonXmlProperty(
            isAttribute = true,
            localName = "Country")
    private String country;

    @JsonIgnore
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}