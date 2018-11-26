package com.example.cinder.restobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupID {
    @SerializedName("matchid")
    @Expose
    private int matchID;

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }
}
