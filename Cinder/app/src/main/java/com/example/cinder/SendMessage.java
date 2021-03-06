package com.example.cinder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendMessage {
    @SerializedName("senderid")
    @Expose
    private int senderid;

    @SerializedName("matchid")
    @Expose
    private int matchID;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("isGroup")
    @Expose
    private boolean isGroup;

    public int getSenderid() {
        return senderid;
    }

    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public String getSendMessage() {
        return message;
    }

    public void setSendMessage(String message) {
        this.message = message;
    }

    public boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }
}
