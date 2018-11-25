package com.example.cinder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class GroupAdd {

    @SerializedName("userMatchID")
    @Expose
    private int userMatchID;

    @SerializedName("matchID")
    @Expose
    private int matchID;

    public int getUserMatchID() {
        return userMatchID;
    }

    public void setUserMatchID(int userMatchID) {
        this.userMatchID = userMatchID;
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }
}
