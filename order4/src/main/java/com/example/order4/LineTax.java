package com.example.order4;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

	public class LineTax {

	    @JacksonXmlProperty(isAttribute = true, localName = "ChargeCategory")
	    private String chargeCategory;

	    @JacksonXmlProperty(isAttribute = true, localName = "Tax")
	    private String tax;

	    @JacksonXmlProperty(isAttribute = true, localName = "TaxName")
	    private String taxName;

	    @JacksonXmlProperty(isAttribute = true, localName = "TaxPercentage")
	    private String taxPercentage;

	    public String getChargeCategory() {
	        return chargeCategory;
	    }

	    public void setChargeCategory(String chargeCategory) {
	        this.chargeCategory = chargeCategory;
	    }

	    public String getTax() {
	        return tax;
	    }

	    public void setTax(String tax) {
	        this.tax = tax;
	    }

	    public String getTaxName() {
	        return taxName;
	    }

	    public void setTaxName(String taxName) {
	        this.taxName = taxName;
	    }

	    public String getTaxPercentage() {
	        return taxPercentage;
	    }

	    public void setTaxPercentage(String taxPercentage) {
	        this.taxPercentage = taxPercentage;
	    }
	}

