package com.versatilemobitech.VeeZee.Model;

import java.io.Serializable;

public class PushForCarHandOverDetails implements Serializable {
    String mobile,status,serial_no,body,image,title,token,driver_name,driver_id,parking_id;

    public PushForCarHandOverDetails(String mobile, String status, String serial_no, String image, String title, String token, String driver_name,String driver_id,String parking_id) {
        this.mobile = mobile;
        this.status = status;
        this.serial_no = serial_no;
        this.body = body;
        this.image = image;
        this.title = title;
        this.token = token;
        this.driver_name = driver_name;
        this.driver_id = driver_id;
        this.parking_id = parking_id;
    }

    public String getParking_id() {
        return parking_id;
    }

    public void setParking_id(String parking_id) {
        this.parking_id = parking_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getBody(String body) {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }
}
