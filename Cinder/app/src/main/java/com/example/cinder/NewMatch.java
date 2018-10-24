package com.example.cinder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class NewMatch {

    @SerializedName("user1")
    @Expose
    private int user1;
    @SerializedName("user2")
    @Expose
    private int user2;
    @SerializedName("hasMatched")
    @Expose
    private Boolean hasMatched;
    @SerializedName("accepted")
    @Expose
    private Boolean accepted;

    public int getUser1() {
        return user1;
    }

    public void setUser1(int user1) {
        this.user1 = user1;
    }

    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    public Boolean getHasMatched() {
        return hasMatched;
    }

    public void setHasMatched(Boolean hasMatched) {
        this.hasMatched = hasMatched;
    }

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

}