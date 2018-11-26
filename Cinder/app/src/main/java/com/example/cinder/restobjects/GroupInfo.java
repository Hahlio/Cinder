package com.example.cinder.restobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupInfo {
    @SerializedName("groups")
    @Expose
    private List<String> groups = null;

    @SerializedName("matchID")
    @Expose
    private List<Integer> matchID = null;

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public List<Integer> getMatchID() {
        return matchID;
    }

    public void setMatchID(List<Integer> matchID) {
        this.matchID = matchID;
    }
}
