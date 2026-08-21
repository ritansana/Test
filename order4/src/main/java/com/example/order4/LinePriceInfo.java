package com.example.order4;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

public class LinePriceInfo {

    @JacksonXmlProperty(isAttribute = true, localName = "UnitPrice")
    private String unitPrice;

	public String getUnitPrice() {
		return unitPrice;
	}

	
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
		
	}

    
}