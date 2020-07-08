package com.versatilemobitech.VeeZee.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Rewards implements Parcelable {
    private String reward_id;
    private String title;
    private String voucher_code;
    private String brand_id;
    private String description;
    private String credits;
    private String image;
    private String valid_from;
    private String valid_to;
    private String whats_inside;
    private String website;
    private String created_by;
    private String created_time;
    private String modified_by;
    private String modified_time;
    private String status;
    private String logo;
    private String brand;
    private String howToRedeem;
    private String note;

    public Rewards(String reward_id, String title, String voucher_code, String brand_id, String description, String credits, String image, String valid_from, String valid_to, String whats_inside, String website, String created_by, String created_time, String modified_by, String modified_time, String status, String logo,String brand,String howToRedeem,String note) {
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
        this.note = note;
        this.howToRedeem = howToRedeem;
    }

    protected Rewards(Parcel in) {
        reward_id = in.readString();
        title = in.readString();
        voucher_code = in.readString();
        brand_id = in.readString();
        description = in.readString();
        credits = in.readString();
        image = in.readString();
        valid_from = in.readString();
        valid_to = in.readString();
        whats_inside = in.readString();
        website = in.readString();
        created_by = in.readString();
        created_time = in.readString();
        modified_by = in.readString();
        modified_time = in.readString();
        status = in.readString();
        logo = in.readString();
        brand = in.readString();
        note = in.readString();
        howToRedeem = in.readString();
    }

    public static final Creator<Rewards> CREATOR = new Creator<Rewards>() {
        @Override
        public Rewards createFromParcel(Parcel in) {
            return new Rewards(in);
        }

        @Override
        public Rewards[] newArray(int size) {
            return new Rewards[size];
        }
    };

    public String getHowToRedeem() {
        return howToRedeem;
    }

    public void setHowToRedeem(String howToRedeem) {
        this.howToRedeem = howToRedeem;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reward_id);
        parcel.writeString(title);
        parcel.writeString(voucher_code);
        parcel.writeString(brand_id);
        parcel.writeString(description);
        parcel.writeString(credits);
        parcel.writeString(image);
        parcel.writeString(valid_from);
        parcel.writeString(valid_to);
        parcel.writeString(whats_inside);
        parcel.writeString(website);
        parcel.writeString(created_by);
        parcel.writeString(created_time);
        parcel.writeString(modified_by);
        parcel.writeString(modified_time);
        parcel.writeString(status);
        parcel.writeString(logo);
        parcel.writeString(brand);
        parcel.writeString(note);
        parcel.writeString(howToRedeem);
    }
}
