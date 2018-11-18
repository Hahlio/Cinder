package com.example.cinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signin extends AppCompatActivity {
    private static final String BASE_URL = "http://168.62.221.80:8080/";
    public boolean loggedin = false;

    protected static Retrofit getRetro(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loggedin = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        final Button signinButton = findViewById(R.id.signinButton);
        final EditText usernameInput  = findViewById(R.id.usernameLogin);
        final EditText passwordInput  = findViewById(R.id.passwordBox);
        final Button accountButton = findViewById(R.id.createAccount);
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);
        if (mpref.getBoolean("loggedIn", false))
            changeToMatchMaking();

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        while(!loggedin) {}
                        if (mpref.getBoolean("loggedIn", false))
                            changeToMatchMaking();
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Incorrect Combination of Username and Password";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            });
                        }
                    }
                });
                thread.start();
                Retrofit retrofit = getRetro();
                RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                SigninInfo info = new SigninInfo();
                info.setUsername(username);
                info.setPassword(password);
                info.setDeviceid(Settings.Secure.ANDROID_ID);
                Call<ProfileID> call = apiCalls.getProfileID(info);
                call.enqueue(new Callback<ProfileID>() {
                    @Override
                    public void onResponse(@NonNull Call<ProfileID> call, Response<ProfileID> response) {
                        int profileID = Objects.requireNonNull(response.body()).getId();
                        if(profileID!=-1){
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putString("hash",response.body().getHash()).apply();
                            editor.putInt("profileID", profileID).putBoolean("loggedIn",true).apply();

                        }else {
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putBoolean("loggedIn",false).apply();
                        }
                        loggedin = true;
                    }

                    @Override
                    public void onFailure(Call<ProfileID> call, Throwable t) {
                        //failure code to be written
                    }
                });


            }
        });
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToProfileCreation();
            }
        });

    }
    public void changeToMatchMaking(){
        Intent intent = new Intent(this, MatchMaking.class);
        startActivity(intent);
    }
    public void changeToProfileCreation(){
        Intent intent = new Intent(this, ProfileCreation.class);
        startActivity(intent);
    }
}
