package com.example.cinder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Matches {

    @SerializedName("Matches")
    @Expose
    private List<Integer> matches = null;

    public List<Integer> getMatches() {
        return matches;
    }

    public void setMatches(List<Integer> matches) {
        this.matches = matches;
    }

}