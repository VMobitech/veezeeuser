package com.versatilemobitech.VeeZee.Model;

public class CarStatus {
    String car_status,token_number,driver_number,car_image,car_number,timer,status,car_id;
    boolean isTimerStarted = false;

    public CarStatus(String car_status, String token_number, String driver_number, String car_image, String car_number, String timer, String status,String car_id) {
        this.car_status = car_status;
        this.token_number = token_number;
        this.driver_number = driver_number;
        this.car_image = car_image;
        this.car_number = car_number;
        this.timer = timer;
        this.status = status;
        this.car_id = car_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public boolean isTimerStarted() {
        return isTimerStarted;
    }

    public void setTimerStarted(boolean timerStarted) {
        isTimerStarted = timerStarted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCar_status() {
        return car_status;
    }

    public void setCar_status(String car_status) {
        this.car_status = car_status;
    }

    public String getToken_number() {
        return token_number;
    }

    public void setToken_number(String token_number) {
        this.token_number = token_number;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = driver_number;
    }

    public String getCar_image() {
        return car_image;
    }

    public void setCar_image(String car_image) {
        this.car_image = car_image;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String car_number) {
        this.car_number = car_number;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }
}
