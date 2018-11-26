package com.example.cinder.restobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacebookLoginReturn {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("hash")
    @Expose
    private String hash;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;



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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
