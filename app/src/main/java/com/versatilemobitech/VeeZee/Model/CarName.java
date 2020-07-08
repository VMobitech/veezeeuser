package com.versatilemobitech.VeeZee.Model;

public class CarName {
    String carName;
    String carId;
    String carColor;

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    String carImage;

    public CarName(String carName, String carId, String carColor, String image) {
        this.carName = carName;
        this.carId = carId;
        this.carColor = carColor;
        this.carImage = image;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }
}
