package com.example.order4;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class PaymentMethods {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "PaymentMethod")
    private List<PaymentMethod> paymentMethodList;

    public List<PaymentMethod> getPaymentMethodList() {
        return paymentMethodList;
    }

    public void setPaymentMethodList(List<PaymentMethod> paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }
}