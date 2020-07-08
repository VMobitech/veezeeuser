package com.versatilemobitech.VeeZee.Model;

public class AdsSub {
    private String sort_order;
    private String banner;

    public AdsSub(String sort_order, String banner) {
        this.sort_order = sort_order;
        this.banner = banner;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
