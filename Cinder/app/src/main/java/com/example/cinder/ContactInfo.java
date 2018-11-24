package com.example.cinder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContactInfo {
    @SerializedName("users")
    @Expose
    private List<String> users = null;

    @SerializedName("matchID")
    @Expose
    private List<Integer> matchID = null;

    public List<String> getusers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<Integer> getMatchID() {
        return matchID;
    }

    public void setMatchID(List<Integer> matchID) {
        this.matchID = matchID;
    }
}
