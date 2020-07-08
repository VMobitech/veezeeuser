package com.versatilemobitech.VeeZee.events;

import com.versatilemobitech.VeeZee.Model.PartnerModel;

import java.util.ArrayList;

public class PartnerEvent {

    ArrayList<PartnerModel> partnerModelArrayList;
    String title;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<PartnerModel> getPartnerModelArrayList() {
        return partnerModelArrayList;
    }

    public void setPartnerModelArrayList(ArrayList<PartnerModel> partnerModelArrayList) {
        this.partnerModelArrayList = partnerModelArrayList;
    }
}
