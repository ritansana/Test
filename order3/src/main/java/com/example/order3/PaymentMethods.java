package com.example.order3;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;



	public class PaymentMethods {

	    @JacksonXmlProperty(localName = "PaymentMethod")
	    @JacksonXmlElementWrapper(useWrapping = false)
	    private List<PaymentMethod> paymentMethod;

		public List<PaymentMethod> getPaymentMethod() {
			return paymentMethod;
		}

		public void setPaymentMethod(List<PaymentMethod> paymentMethod) {
			this.paymentMethod = paymentMethod;
		}

	    

}