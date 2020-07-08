package com.versatilemobitech.VeeZee.Model;

public class CreditsHistory {
String wallet_history_id,amount,action,reason,created_time,status;

    public CreditsHistory(String wallet_history_id, String amount, String action, String reason, String created_time, String status) {
        this.wallet_history_id = wallet_history_id;
        this.amount = amount;
        this.action = action;
        this.reason = reason;
        this.created_time = created_time;
        this.status = status;
    }

    public String getWallet_history_id() {
        return wallet_history_id;
    }

    public void setWallet_history_id(String wallet_history_id) {
        this.wallet_history_id = wallet_history_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
