package com.example.cinder.restobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("school")
    @Expose
    private String school;
    @SerializedName("courses")
    @Expose
    private String courses;
    @SerializedName("preferences")
    @Expose
    private String preferences;
    @SerializedName("interests")
    @Expose
    private String interests;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("deviceid")
    @Expose
    private String deviceid;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCourses() {
        return courses;
    }

    public void setCourses(String courses) {
        this.courses = courses;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

}