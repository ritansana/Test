package com.example.order4;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderLine {

    public OrderLine() {
    }

    @JacksonXmlProperty(isAttribute = true, localName = "OrderedQty")
    private String orderedQty;

    @JacksonXmlProperty(isAttribute = true, localName = "PrimeLineNo")
    private String primeLineNo;

    @JacksonXmlProperty(isAttribute = true, localName = "SubLineNo")
    private String subLineNo;

    @JacksonXmlProperty(isAttribute = true, localName = "DeliveryMethod")
    private String deliveryMethod;

    @JacksonXmlProperty(isAttribute = true, localName = "ShipNode")
    private String shipNode;

    @JacksonXmlProperty(localName = "Item")
    private Item item;

    @JacksonXmlProperty(localName = "LinePriceInfo")
    private LinePriceInfo linePriceInfo;

    @JacksonXmlProperty(localName = "LineCharges")
    private LineCharges lineCharges;

    @JacksonXmlProperty(localName = "LineTaxes")
    private LineTaxes lineTaxes;

	public String getOrderedQty() {
		return orderedQty;
	}

	public void setOrderedQty(String orderedQty) {
		this.orderedQty = orderedQty;
	}

	public String getPrimeLineNo() {
		return primeLineNo;
	}

	public void setPrimeLineNo(String primeLineNo) {
		this.primeLineNo = primeLineNo;
	}

	public String getSubLineNo() {
		return subLineNo;
	}

	public void setSubLineNo(String subLineNo) {
		this.subLineNo = subLineNo;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public String getShipNode() {
		return shipNode;
	}

	public void setShipNode(String shipNode) {
		this.shipNode = shipNode;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public LinePriceInfo getLinePriceInfo() {
		return linePriceInfo;
	}

	public void setLinePriceInfo(LinePriceInfo linePriceInfo) {
		this.linePriceInfo = linePriceInfo;
	}

	public LineCharges getLineCharges() {
		return lineCharges;
	}

	public void setLineCharges(LineCharges lineCharges) {
		this.lineCharges = lineCharges;
	}

	public LineTaxes getLineTaxes() {
		return lineTaxes;
	}

	public void setLineTaxes(LineTaxes lineTaxes) {
		this.lineTaxes = lineTaxes;
	}

    
}