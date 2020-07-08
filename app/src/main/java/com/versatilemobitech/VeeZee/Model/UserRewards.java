package com.versatilemobitech.VeeZee.Model;

public class UserRewards {
    private String reward_id, title, voucher_code,
            brand_id, description, credits, image,
            valid_from, valid_to, whats_inside, website, created_by, created_time,
            modified_by, modified_time, status, logo, brand;


    public UserRewards(String reward_id, String title, String voucher_code, String brand_id, String description, String credits, String image, String valid_from, String valid_to, String whats_inside, String website, String created_by, String created_time, String modified_by, String modified_time, String status,String logo
    ,String brand) {
        this.reward_id = reward_id;
        this.title = title;
        this.voucher_code = voucher_code;
        this.brand_id = brand_id;
        this.description = description;
        this.credits = credits;
        this.image = image;
        this.valid_from = valid_from;
        this.valid_to = valid_to;
        this.whats_inside = whats_inside;
        this.website = website;
        this.created_by = created_by;
        this.created_time = created_time;
        this.modified_by = modified_by;
        this.modified_time = modified_time;
        this.status = status;
        this.logo = logo;
        this.brand = brand;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getReward_id() {
        return reward_id;
    }

    public void setReward_id(String reward_id) {
        this.reward_id = reward_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVoucher_code() {
        return voucher_code;
    }

    public void setVoucher_code(String voucher_code) {
        this.voucher_code = voucher_code;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public String getWhats_inside() {
        return whats_inside;
    }

    public void setWhats_inside(String whats_inside) {
        this.whats_inside = whats_inside;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getModified_time() {
        return modified_time;
    }

    public void setModified_time(String modified_time) {
        this.modified_time = modified_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
