package com.versatilemobitech.VeeZee.Model;

public class HistoryVault {
    String vault_history_id,image,name,address,date,picked_by,in_time,dropped_by,out_time;

    public HistoryVault(String vault_history_id, String image, String name, String address, String date, String picked_by, String in_time, String dropped_by, String out_time) {
        this.vault_history_id = vault_history_id;
        this.image = image;
        this.name = name;
        this.address = address;
        this.date = date;
        this.picked_by = picked_by;
        this.in_time = in_time;
        this.dropped_by = dropped_by;
        this.out_time = out_time;
    }

    public String getVault_history_id() {
        return vault_history_id;
    }

    public void setVault_history_id(String vault_history_id) {
        this.vault_history_id = vault_history_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPicked_by() {
        return picked_by;
    }

    public void setPicked_by(String picked_by) {
        this.picked_by = picked_by;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getDropped_by() {
        return dropped_by;
    }

    public void setDropped_by(String dropped_by) {
        this.dropped_by = dropped_by;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }
}
