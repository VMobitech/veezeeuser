package com.versatilemobitech.VeeZee.Model;

import java.io.Serializable;

public class UserCarModel implements Serializable {
    private String user_id;
    private String user_car_id;
    private String qr;
    private String image;
    private String name;
    private String color;
    private String vehicle_no;
    private String car_status;
    private String is_requested;
    boolean isSelected;
    private String brand_name;
    private String car_name;
    private String brand_image;
    private String direct_scan;
    private String last_used;

    private String car_id;



    public UserCarModel(String user_id, String user_car_id, String qr, String image, String name, String color, String vehicle_no,String car_status,String is_requested, boolean isSelected
    ,String brand_name,String car_name,String brand_image,String direct_scan,String last_used,String car_id) {
        this.user_id = user_id;
        this.user_car_id = user_car_id;
        this.qr = qr;
        this.image = image;
        this.name = name;
        this.color = color;
        this.vehicle_no = vehicle_no;
        this.isSelected = isSelected;
        this.car_status = car_status;
        this.is_requested = is_requested;
        this.brand_name = brand_name;
        this.car_name = car_name;
        this.brand_image = brand_image;
        this.direct_scan = direct_scan;
        this.last_used = last_used;
        this.car_id = car_id;

    }

    public String getLast_used() {
        return last_used;
    }

    public void setLast_used(String last_used) {
        this.last_used = last_used;
    }

    public String getDirect_scan() {
        return direct_scan;
    }

    public void setDirect_scan(String direct_scan) {
        this.direct_scan = direct_scan;
    }

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_status() {
        return car_status;
    }

    public void setCar_status(String car_status) {
        this.car_status = car_status;
    }

    public String getIs_requested() {
        return is_requested;
    }

    public void setIs_requested(String is_requested) {
        this.is_requested = is_requested;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_car_id() {
        return user_car_id;
    }

    public void setUser_car_id(String user_car_id) {
        this.user_car_id = user_car_id;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }
}
