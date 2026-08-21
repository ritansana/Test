package com.example.order3;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

public class Extn {

    @JacksonXmlProperty(isAttribute = true,
            localName = "ExtnWISOrderAmount")
    private String extnWISOrderAmount;

    @JacksonXmlProperty(isAttribute = true,
            localName = "ExtnAffiliateID")
    private String extnAffiliateID;

	public String getExtnWISOrderAmount() {
		return extnWISOrderAmount;
	}

	public void setExtnWISOrderAmount(String extnWISOrderAmount) {
		this.extnWISOrderAmount = extnWISOrderAmount;
	}

	public String getExtnAffiliateID() {
		return extnAffiliateID;
	}

	public void setExtnAffiliateID(String extnAffiliateID) {
		this.extnAffiliateID = extnAffiliateID;
	}

   
}