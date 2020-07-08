package com.versatilemobitech.VeeZee.Model;

public class StateModel {
    String stateName,StateId;

    public StateModel() {
    }

    public StateModel(String stateName, String stateId) {
        this.stateName = stateName;
        StateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateId() {
        return StateId;
    }

    public void setStateId(String stateId) {
        StateId = stateId;
    }
}
