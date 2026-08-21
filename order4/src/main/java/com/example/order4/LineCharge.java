package com.example.order4;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

	public class LineCharge {

	    @JacksonXmlProperty(isAttribute = true, localName = "ChargeCategory")
	    private String chargeCategory;

	    @JacksonXmlProperty(isAttribute = true, localName = "ChargeName")
	    private String chargeName;

	    @JacksonXmlProperty(isAttribute = true, localName = "ChargePerLine")
	    private String chargePerLine;

	    public String getChargeCategory() {
	        return chargeCategory;
	    }

	    public void setChargeCategory(String chargeCategory) {
	        this.chargeCategory = chargeCategory;
	    }

	    public String getChargeName() {
	        return chargeName;
	    }

	    public void setChargeName(String chargeName) {
	        this.chargeName = chargeName;
	    }

	    public String getChargePerLine() {
	        return chargePerLine;
	    }

	    public void setChargePerLine(String chargePerLine) {
	        this.chargePerLine = chargePerLine;
	    }
	}

