package com.example.cinder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationSwitch {
    @SerializedName("notify")
    @Expose
    private Boolean notify;

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }
}
