package com.example.order3;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Order {

    @JacksonXmlProperty(isAttribute = true, localName = "DocumentType")
    private String documentType;

    @JacksonXmlProperty(isAttribute = true, localName = "EnterpriseCode")
    private String enterpriseCode;

    @JacksonXmlProperty(localName = "Extn")
    private Extn extn;

    @JacksonXmlProperty(localName = "OrderLines")
    private OrderLines orderLines;

    @JacksonXmlProperty(localName = "PaymentMethods")
    private PaymentMethods paymentMethods;

    @JacksonXmlProperty(localName = "PersonInfoShipTo")
    private PersonInfoShipTo personInfoShipTo;

    @JacksonXmlProperty(localName = "PersonInfoBillTo")
    private PersonInfoBillTo personInfoBillTo;

    // DocumentType

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    // EnterpriseCode

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    // Extn

    public Extn getExtn() {
        return extn;
    }

    public void setExtn(Extn extn) {
        this.extn = extn;
    }

    // OrderLines

    public OrderLines getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(OrderLines orderLines) {
        this.orderLines = orderLines;
    }

    // PaymentMethods

    public PaymentMethods getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(PaymentMethods paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    // ShipTo

    public PersonInfoShipTo getPersonInfoShipTo() {
        return personInfoShipTo;
    }

    public void setPersonInfoShipTo(PersonInfoShipTo personInfoShipTo) {
        this.personInfoShipTo = personInfoShipTo;
    }

    // BillTo

    public PersonInfoBillTo getPersonInfoBillTo() {
        return personInfoBillTo;
    }

    public void setPersonInfoBillTo(PersonInfoBillTo personInfoBillTo) {
        this.personInfoBillTo = personInfoBillTo;
    }
}