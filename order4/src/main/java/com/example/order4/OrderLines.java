package com.example.order4;

	import java.util.List;

	import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
	import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

	public class OrderLines {

	    @JacksonXmlElementWrapper(useWrapping = false)
	    @JacksonXmlProperty(localName = "OrderLine")
	    private List<OrderLine> orderLineList;

	    public List<OrderLine> getOrderLineList() {
	        return orderLineList;
	    }

	    public void setOrderLineList(List<OrderLine> orderLineList) {
	        this.orderLineList = orderLineList;
	    }
	}

