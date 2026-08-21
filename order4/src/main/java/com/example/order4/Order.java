package com.example.order4;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Order {

    @JacksonXmlProperty(isAttribute = true, localName = "DocumentType")
    private String documentType;

    @JacksonXmlProperty(isAttribute = true, localName = "EnterpriseCode")
    private String enterpriseCode;

    @JacksonXmlProperty(localName = "OrderLines")
    private OrderLines orderLines;

    @JacksonXmlProperty(localName = "PaymentMethods")
    private PaymentMethods paymentMethods;

    @JacksonXmlProperty(localName = "PersonInfoShipTo")
    private PersonInfoShipTo personInfoShipTo;

    @JacksonXmlProperty(localName = "PersonInfoBillTo")
    private PersonInfoBillTo personInfoBillTo;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public OrderLines getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(OrderLines orderLines) {
        this.orderLines = orderLines;
    }

    public PaymentMethods getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(PaymentMethods paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public PersonInfoShipTo getPersonInfoShipTo() {
        return personInfoShipTo;
    }

    public void setPersonInfoShipTo(PersonInfoShipTo personInfoShipTo) {
        this.personInfoShipTo = personInfoShipTo;
    }

    public PersonInfoBillTo getPersonInfoBillTo() {
        return personInfoBillTo;
    }

    public void setPersonInfoBillTo(PersonInfoBillTo personInfoBillTo) {
        this.personInfoBillTo = personInfoBillTo;
    }
}