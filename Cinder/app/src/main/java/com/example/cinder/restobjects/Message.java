package com.example.cinder.restobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Message {
    @SerializedName("users")
    @Expose
    private List<String> users = null;

    @SerializedName("messages")
    @Expose
    private List<String> messages = null;

    @SerializedName("userID")
    @Expose
    private List<Integer> userID = null;

    @SerializedName("timestamps")
    @Expose
    private List<String> timeStamps= null;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = messages;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<Integer> getUserID() {
        return userID;
    }

    public void setUserID(List<Integer> userID) {
        this.userID = userID;
    }

    public List<String> getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(List<String> timeStamps) {
        this.timeStamps = timeStamps;
    }
}
