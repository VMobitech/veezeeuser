package com.versatilemobitech.VeeZee.Model;

public class PaytmParams {
    String CUST_ID,MOBILE_NO,CHANNEL_ID,ORDER_ID,TXN_AMOUNT,CALLBACK_URL,MID,INDUSTRY_TYPE_ID,EMAIL,WEBSITE,CHECKSUM;

    public PaytmParams(String CUST_ID, String MOBILE_NO, String CHANNEL_ID, String ORDER_ID, String TXN_AMOUNT, String CALLBACK_URL, String MID, String INDUSTRY_TYPE_ID, String EMAIL, String WEBSITE, String CHECKSUM) {
        this.CUST_ID = CUST_ID;
        this.MOBILE_NO = MOBILE_NO;
        this.CHANNEL_ID = CHANNEL_ID;
        this.ORDER_ID = ORDER_ID;
        this.TXN_AMOUNT = TXN_AMOUNT;
        this.CALLBACK_URL = CALLBACK_URL;
        this.MID = MID;
        this.INDUSTRY_TYPE_ID = INDUSTRY_TYPE_ID;
        this.EMAIL = EMAIL;
        this.WEBSITE = WEBSITE;
        this.CHECKSUM = CHECKSUM;
    }

    public String getCHECKSUM() {
        return CHECKSUM;
    }

    public void setCHECKSUM(String CHECKSUM) {
        this.CHECKSUM = CHECKSUM;
    }

    public String getCUST_ID() {
        return CUST_ID;
    }

    public void setCUST_ID(String CUST_ID) {
        this.CUST_ID = CUST_ID;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(String CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID;
    }

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public String getTXN_AMOUNT() {
        return TXN_AMOUNT;
    }

    public void setTXN_AMOUNT(String TXN_AMOUNT) {
        this.TXN_AMOUNT = TXN_AMOUNT;
    }

    public String getCALLBACK_URL() {
        return CALLBACK_URL;
    }

    public void setCALLBACK_URL(String CALLBACK_URL) {
        this.CALLBACK_URL = CALLBACK_URL;
    }

    public String getMID() {
        return MID;
    }

    public void setMID(String MID) {
        this.MID = MID;
    }

    public String getINDUSTRY_TYPE_ID() {
        return INDUSTRY_TYPE_ID;
    }

    public void setINDUSTRY_TYPE_ID(String INDUSTRY_TYPE_ID) {
        this.INDUSTRY_TYPE_ID = INDUSTRY_TYPE_ID;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getWEBSITE() {
        return WEBSITE;
    }

    public void setWEBSITE(String WEBSITE) {
        this.WEBSITE = WEBSITE;
    }
}
