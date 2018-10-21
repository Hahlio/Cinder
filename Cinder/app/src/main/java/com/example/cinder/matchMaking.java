package com.example.cinder;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    public static List<ProfileID> pmatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);
        final Button yesButton = findViewById(R.id.yesButton);
        final Button noButton = findViewById(R.id.noButton);
        final SharedPreferences mpref = PreferenceManager.getDefaultSharedPreferences(matchMaking.this);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pmatches.remove(0);
                if(!pmatches.isEmpty())
                    showProfile(pmatches.get(0).getId());
                else
                    outOfMatches();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pmatches.remove(0);
                if(!pmatches.isEmpty())
                    showProfile(pmatches.get(0).getId());
                else
                    outOfMatches();
            }
        });

        Retrofit retrofit = getRetro();
        restApiCalls apiCalls = retrofit.create(restApiCalls.class);
        SharedPreferences.Editor editor = mpref.edit();
        int profileID = mpref.getInt("profileID",-1);
        Call<List<ProfileID>> call = apiCalls.getMatches(profileID);
        call.enqueue(new Callback<List<ProfileID>>() {
            @Override
            public void onResponse(Call<List<ProfileID>> call, Response<List<ProfileID>> response) {
                pmatches = response.body();
            }

            @Override
            public void onFailure(Call<List<ProfileID>> call, Throwable t) {
            }
        });
        showProfile(pmatches.get(0).getId());






    }
    public void showProfile(int profileID){
        final TextView nameDisplay = findViewById(R.id.nameDisplay);
        final TextView locationDisplay = findViewById(R.id.locationDisplay);
        final TextView courseDisplay = findViewById(R.id.courseDisplay);
        Profile show = new Profile();
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
}
