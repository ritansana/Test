package com.example.order4;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class LineCharges {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "LineCharge")
    private List<LineCharge> lineChargeList;

    public List<LineCharge> getLineChargeList() {
        return lineChargeList;
    }

    public void setLineChargeList(List<LineCharge> lineChargeList) {
        this.lineChargeList = lineChargeList;
    }
}
