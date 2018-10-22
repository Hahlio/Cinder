package com.example.cinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signin extends AppCompatActivity {
    public static final String BASE_URL = "http://52.183.2.222/";
    public boolean loggedin = false;

    static Retrofit getRetro(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loggedin = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final Button signinButton = findViewById(R.id.signinButton);
        final EditText usernameInput  = findViewById(R.id.usernameLogin);
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        while(!loggedin) {}
                        if (mpref.getBoolean("loggedIn", false))
                            changeToMatchMaking();
                        else
                            changeToProfileCreation();

                    }
                });
                thread.start();
                Retrofit retrofit = getRetro();
                restApiCalls apiCalls = retrofit.create(restApiCalls.class);
                String username = usernameInput.getText().toString();
                Call<ProfileID> call = apiCalls.getProfileID(username);
                call.enqueue(new Callback<ProfileID>() {
                    @Override
                    public void onResponse(Call<ProfileID> call, Response<ProfileID> response) {
                        int profileID = response.body().getId();
                        if(!(profileID==-1)){
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putInt("profileID", profileID).putBoolean("loggedIn",true).apply();

                        }else {
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putBoolean("loggedIn",false).apply();
                        }
                        loggedin = true;
                    }

                    @Override
                    public void onFailure(Call<ProfileID> call, Throwable t) {

                    }
                });


            }
        });


    }
    public void changeToMatchMaking(){
        Intent intent = new Intent(this, matchMaking.class);
        startActivity(intent);
    }
    public void changeToProfileCreation(){
        Intent intent = new Intent(this, profileCreation.class);
        startActivity(intent);
    }
}
