package com.versatilemobitech.VeeZee.Model;

public class RewardBrand {
    private String category_id, name, logo,selectedLogo;

    public String getSelectedLogo() {
        return selectedLogo;
    }

    public void setSelectedLogo(String selectedLogo) {
        this.selectedLogo = selectedLogo;
    }

    public RewardBrand(String category_id, String name, String logo, String selectedLogo) {
        this.category_id = category_id;
        this.name = name;
        this.logo = logo;
        this.selectedLogo = selectedLogo;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String brand_id)
    {
        this.category_id = brand_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
