package com.versatilemobitech.VeeZee.Model;

public class HistoryValet {
    String parking_id,tip,name,address,date,picked_by,in_time,dropped_by,out_time,image,vehicle_no;

    public HistoryValet(String parking_id, String tip, String name, String address, String date, String picked_by,
                        String in_time, String dropped_by, String out_time, String image,String vehicle_no) {
        this.parking_id = parking_id;
        this.tip = tip;
        this.name = name;
        this.address = address;
        this.date = date;
        this.picked_by = picked_by;
        this.in_time = in_time;
        this.dropped_by = dropped_by;
        this.out_time = out_time;
        this.image = image;
        this.vehicle_no = vehicle_no;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParking_id() {
        return parking_id;
    }

    public void setParking_id(String parking_id) {
        this.parking_id = parking_id;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
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
