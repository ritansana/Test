package com.example.order3;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

public class OrderLine {

    @JacksonXmlProperty(isAttribute = true, localName = "OrderedQty")
    private int orderedQty;

    @JacksonXmlProperty(isAttribute = true, localName = "PrimeLineNo")
    private int primeLineNo;

    @JacksonXmlProperty(isAttribute = true, localName = "SubLineNo")
    private int subLineNo;

    @JacksonXmlProperty(isAttribute = true, localName = "DeliveryMethod")
    private String deliveryMethod;

    @JacksonXmlProperty(isAttribute = true, localName = "ShipNode")
    private String shipNode;

    @JacksonXmlProperty(localName = "Item")
    private Item item;

    @JacksonXmlProperty(localName = "LinePriceInfo")
    private LinePriceInfo linePriceInfo;

	public int getOrderedQty() {
		return orderedQty;
	}

	public void setOrderedQty(int orderedQty) {
		this.orderedQty = orderedQty;
	}

	public int getPrimeLineNo() {
		return primeLineNo;
	}

	public void setPrimeLineNo(int primeLineNo) {
		this.primeLineNo = primeLineNo;
	}

	public int getSubLineNo() {
		return subLineNo;
	}

	public void setSubLineNo(int subLineNo) {
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

    
}