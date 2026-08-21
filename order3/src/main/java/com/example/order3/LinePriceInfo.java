package com.example.order3;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

public class LinePriceInfo {

    @JacksonXmlProperty(isAttribute = true, localName = "UnitPrice")
    private double unitPrice;

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

    
}