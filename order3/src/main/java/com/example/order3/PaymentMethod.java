package com.example.order3;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class PaymentMethod {


    @JacksonXmlProperty(isAttribute = true)
    private String ChargeSequence;

    @JacksonXmlProperty(isAttribute = true)
    private String CreditCardExpDate;

    @JacksonXmlProperty(isAttribute = true)
    private String CreditCardName;

    @JacksonXmlProperty(isAttribute = true)
    private String CreditCardNo;

    @JacksonXmlProperty(isAttribute = true)
    private String CreditCardType;

    @JacksonXmlProperty(isAttribute = true)
    private String DisplayCreditCardNo;

    @JacksonXmlProperty(isAttribute = true)
    private String PaymentType;

    @JacksonXmlProperty(isAttribute = true)
    private String SvcNo;

    @JacksonXmlProperty(isAttribute = true)
    private String UnlimitedCharges;

	public String getChargeSequence() {
		return ChargeSequence;
	}

	public void setChargeSequence(String chargeSequence) {
		ChargeSequence = chargeSequence;
	}

	public String getCreditCardExpDate() {
		return CreditCardExpDate;
	}

	public void setCreditCardExpDate(String creditCardExpDate) {
		CreditCardExpDate = creditCardExpDate;
	}

	public String getCreditCardName() {
		return CreditCardName;
	}

	public void setCreditCardName(String creditCardName) {
		CreditCardName = creditCardName;
	}

	public String getCreditCardNo() {
		return CreditCardNo;
	}

	public void setCreditCardNo(String creditCardNo) {
		CreditCardNo = creditCardNo;
	}

	public String getCreditCardType() {
		return CreditCardType;
	}

	public void setCreditCardType(String creditCardType) {
		CreditCardType = creditCardType;
	}

	public String getDisplayCreditCardNo() {
		return DisplayCreditCardNo;
	}

	public void setDisplayCreditCardNo(String displayCreditCardNo) {
		DisplayCreditCardNo = displayCreditCardNo;
	}

	public String getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}

	public String getSvcNo() {
		return SvcNo;
	}

	public void setSvcNo(String svcNo) {
		SvcNo = svcNo;
	}

	public String getUnlimitedCharges() {
		return UnlimitedCharges;
	}

	public void setUnlimitedCharges(String unlimitedCharges) {
		UnlimitedCharges = unlimitedCharges;
	}

   
}