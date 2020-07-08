package com.versatilemobitech.VeeZee.Model;

import androidx.annotation.NonNull;

public class State {
    private String stateId;
    private String stateName;

    public State(String stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @NonNull
    @Override
    public String toString() {
        return getStateName();
    }
}
