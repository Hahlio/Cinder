package com.example.cinder;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.cinder.Signin.getRetro;

public class matchMaking extends AppCompatActivity {

    public static List<Integer> pmatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);
        final Button yesButton = findViewById(R.id.yesButton);
        final Button noButton = findViewById(R.id.noButton);
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);

        final int profileID = mpref.getInt("profileID",0);
        getMatches(profileID);

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                while(pmatches==null){}
                if(!pmatches.isEmpty())
                    showProfile(pmatches.get(0));
                else
                    outOfMatches();
            }
        });
        thread.start();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pmatches.isEmpty()){
                    pmatches.remove(0);
                    if(!pmatches.isEmpty())
                        showProfile(pmatches.get(0));
                    else
                        outOfMatches();
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pmatches.isEmpty()){
                    pmatches.remove(0);
                    if(!pmatches.isEmpty())
                        showProfile(pmatches.get(0));
                    else
                        outOfMatches();
                }
            }
        });







    }
    public void showProfile(int profileID){
        final TextView nameDisplay = findViewById(R.id.nameDisplay);
        final TextView locationDisplay = findViewById(R.id.locationDisplay);
        final TextView courseDisplay = findViewById(R.id.courseDisplay);
        Retrofit retrofit = getRetro();
        restApiCalls apiCalls = retrofit.create(restApiCalls.class);
        Call<Profile> call = apiCalls.getProfile(profileID);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Profile show = response.body();
                nameDisplay.setText(show.getName());
                locationDisplay.setText(show.getLat() +", " + show.getLng());
                courseDisplay.setText(show.getCourses());
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });


    }
    public void outOfMatches() {
        final TextView nameDisplay = findViewById(R.id.nameDisplay);
        final TextView locationDisplay = findViewById(R.id.locationDisplay);
        final TextView courseDisplay = findViewById(R.id.courseDisplay);
        nameDisplay.setText("No More Matches");
        locationDisplay.setText("No More Matches");
        courseDisplay.setText("No More Matches");
    }

    public void addMatch(int userID1, int userID2, boolean accept){
        NewMatch newMatch = new NewMatch();
        newMatch.setAccepted(accept);
        newMatch.setHasMatched(true);
        newMatch.setUser1(userID1);
        newMatch.setUser2(userID2);
        Retrofit retrofit = getRetro();
        restApiCalls apiCalls = retrofit.create(restApiCalls.class);
        Call<NewMatch> call = apiCalls.addMatch(newMatch,userID1);
        call.enqueue(new Callback<NewMatch>() {
            @Override
            public void onResponse(Call<NewMatch> call, Response<NewMatch> response) {
            }
            @Override
            public void onFailure(Call<NewMatch> call, Throwable t) {
            }
        });

    }
    public void getMatches(int profileID) {
        Retrofit retrofit = getRetro();
        restApiCalls apiCalls = retrofit.create(restApiCalls.class);
        Call<Matches> call = apiCalls.getMatches(profileID);
        call.enqueue(new Callback<Matches>() {
            @Override
            public void onResponse(Call<Matches> call, Response<Matches> response) {
                pmatches=response.body().getMatches();
            }

            @Override
            public void onFailure(Call<Matches> call, Throwable t) {
                Log.d("error", "error");
            }
        });
    }

}
