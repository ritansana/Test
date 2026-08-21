package com.example.order3;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class OrderLines {

    @JacksonXmlProperty(localName = "OrderLine")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<OrderLine> orderLine;

    public List<OrderLine> getOrderLine() {
        return orderLine;
    }

    public void setOrderLine(List<OrderLine> orderLine) {
        this.orderLine = orderLine;
    }
}