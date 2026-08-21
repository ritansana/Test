package com.example.order4;


import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class LineTaxes {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "LineTax")
    private List<LineTax> lineTaxList;

    public List<LineTax> getLineTaxList() {
        return lineTaxList;
    }

    public void setLineTaxList(List<LineTax> lineTaxList) {
        this.lineTaxList = lineTaxList;
    }
}
