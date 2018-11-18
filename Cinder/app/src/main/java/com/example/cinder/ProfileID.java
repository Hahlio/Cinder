package com.example.cinder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class ProfileID {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("hash")
    @Expose
    private String hash;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

}