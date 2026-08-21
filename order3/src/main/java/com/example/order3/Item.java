package com.example.order3;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

public class Item {

    @JacksonXmlProperty(isAttribute = true, localName = "ItemID")
    private String itemID;

    @JacksonXmlProperty(isAttribute = true, localName = "ProductClass")
    private String productClass;

    @JacksonXmlProperty(isAttribute = true, localName = "UnitOfMeasure")
    private String unitOfMeasure;

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getProductClass() {
		return productClass;
	}

	public void setProductClass(String productClass) {
		this.productClass = productClass;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

    
}