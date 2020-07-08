package com.versatilemobitech.VeeZee.Model;


import android.os.Parcel;
import android.os.Parcelable;

public class UserDetails implements Parcelable {
    private String user_id;
    private String name;
    private String mobile;
    private String email;
    private String image;
    private String dob;
    private String gender;
    private String wallet_amount;
    private String qr;
    private String cars_count;


    public UserDetails(String user_id, String name, String mobile, String email, String image, String dob, String gender, String wallet_amount, String qr,String cars_count) {
        this.user_id = user_id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.image = image;
        this.dob = dob;
        this.gender = gender;
        this.wallet_amount = wallet_amount;
        this.qr = qr;
        this.cars_count = cars_count;
    }


    protected UserDetails(Parcel in) {
        user_id = in.readString();
        name = in.readString();
        mobile = in.readString();
        email = in.readString();
        image = in.readString();
        dob = in.readString();
        gender = in.readString();
        wallet_amount = in.readString();
        qr = in.readString();
        cars_count = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeString(dob);
        dest.writeString(gender);
        dest.writeString(wallet_amount);
        dest.writeString(qr);
        dest.writeString(cars_count);
    }

    public static final Creator<UserDetails> CREATOR = new Creator<UserDetails>() {
        @Override
        public UserDetails createFromParcel(Parcel in) {
            return new UserDetails(in);
        }

        @Override
        public UserDetails[] newArray(int size) {
            return new UserDetails[size];
        }
    };

    public String getCars_count() {
        return cars_count;
    }

    public void setCars_count(String cars_count) {
        this.cars_count = cars_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWallet_amount() {
        return wallet_amount;
    }

    public void setWallet_amount(String wallet_amount) {
        this.wallet_amount = wallet_amount;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
