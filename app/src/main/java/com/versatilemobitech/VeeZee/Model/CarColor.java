package com.versatilemobitech.VeeZee.Model;

public class CarColor {
    String car_id,name,image,color;

    public CarColor(String car_id, String name, String image,String color) {
        this.car_id = car_id;
        this.name = name;
        this.image = image;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
