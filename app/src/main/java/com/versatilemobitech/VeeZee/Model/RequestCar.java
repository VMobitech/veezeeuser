package com.versatilemobitech.VeeZee.Model;

public class RequestCar {
    String status,message,credits,amount;

    public RequestCar( String message, String credits, String amount) {
        this.status = status;
        this.message = message;
        this.credits = credits;
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
