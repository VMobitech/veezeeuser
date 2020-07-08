package com.versatilemobitech.VeeZee.Model;

public class Contacts {
    String name,mobile,image,share_user_id;

    public Contacts(String name, String mobile, String image,String share_user_id) {
        this.name = name;
        this.mobile = mobile;
        this.image = image;
        this.share_user_id = share_user_id;
    }

    public String getShare_user_id() {
        return share_user_id;
    }

    public void setShare_user_id(String share_user_id) {
        this.share_user_id = share_user_id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
