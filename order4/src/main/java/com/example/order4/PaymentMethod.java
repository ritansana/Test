package com.example.order4;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class PaymentMethod {

    @JacksonXmlProperty(isAttribute = true, localName = "ChargeSequence")
    private String chargeSequence;

    @JacksonXmlProperty(isAttribute = true, localName = "CreditCardExpDate")
    private String creditCardExpDate;

    @JacksonXmlProperty(isAttribute = true, localName = "CreditCardName")
    private String creditCardName;

    @JacksonXmlProperty(isAttribute = true, localName = "CreditCardNo")
    private String creditCardNo;

    @JacksonXmlProperty(isAttribute = true, localName = "CreditCardType")
    private String creditCardType;

    @JacksonXmlProperty(isAttribute = true, localName = "DisplayCreditCardNo")
    private String displayCreditCardNo;

    @JacksonXmlProperty(isAttribute = true, localName = "PaymentType")
    private String paymentType;

    @JacksonXmlProperty(isAttribute = true, localName = "SvcNo")
    private String svcNo;

    @JacksonXmlProperty(isAttribute = true, localName = "UnlimitedCharges")
    private String unlimitedCharges;

    public String getChargeSequence() {
        return chargeSequence;
    }

    public void setChargeSequence(String chargeSequence) {
        this.chargeSequence = chargeSequence;
    }

    public String getCreditCardExpDate() {
        return creditCardExpDate;
    }

    public void setCreditCardExpDate(String creditCardExpDate) {
        this.creditCardExpDate = creditCardExpDate;
    }

    public String getCreditCardName() {
        return creditCardName;
    }

    public void setCreditCardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }

    public String getDisplayCreditCardNo() {
        return displayCreditCardNo;
    }

    public void setDisplayCreditCardNo(String displayCreditCardNo) {
        this.displayCreditCardNo = displayCreditCardNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getSvcNo() {
        return svcNo;
    }

    public void setSvcNo(String svcNo) {
        this.svcNo = svcNo;
    }

    public String getUnlimitedCharges() {
        return unlimitedCharges;
    }

    public void setUnlimitedCharges(String unlimitedCharges) {
        this.unlimitedCharges = unlimitedCharges;
    }
}


