package com.example.order3;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class PersonInfoBillTo {

    private String country;

    @JacksonXmlProperty(
            isAttribute = true,
            localName = "Country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}