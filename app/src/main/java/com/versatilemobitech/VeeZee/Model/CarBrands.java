package com.versatilemobitech.VeeZee.Model;

import androidx.annotation.NonNull;

public class CarBrands {
    String brandId;
    String brandName;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    String logo;

    public CarBrands(String brand_id, String name,String logo) {
        this.brandId = brand_id;
        this.brandName = name;
        this.logo = logo;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @NonNull
    @Override
    public String toString() {
        return brandId;
    }
}
