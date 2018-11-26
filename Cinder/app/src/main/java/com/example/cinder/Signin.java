package com.example.cinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.cinder.restobjects.FacebookLoginReturn;
import com.example.cinder.restobjects.FacebookToken;
import com.example.cinder.restobjects.SigninInfo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Signin extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final String BASE_URL = "http://168.62.221.80:8080/";
    public boolean loggedin = false;
    CallbackManager callbackManager;

    protected static Retrofit getRetro() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loggedin = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

         if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }

        final Button signinButton = findViewById(R.id.signinButton);
        final EditText usernameInput = findViewById(R.id.usernameLogin);
        final EditText passwordInput = findViewById(R.id.passwordBox);
        final Button accountButton = findViewById(R.id.createAccount);
        final SharedPreferences mpref = getSharedPreferences("IDValue", 0);
        if (mpref.getBoolean("loggedIn", false))
            changeToMatchMaking();

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!loggedin) {
                        }
                        if (mpref.getBoolean("loggedIn", true))
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
                final String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                SigninInfo info = new SigninInfo();
                info.setUsername(username);
                info.setPassword(password);
                info.setDeviceid(FirebaseInstanceId.getInstance().getToken());
                Call<ProfileID> call = apiCalls.getProfileID(info);
                call.enqueue(new Callback<ProfileID>() {
                    @Override
                    public void onResponse(@NonNull Call<ProfileID> call, Response<ProfileID> response) {
                        int profileID = Objects.requireNonNull(response.body()).getId();
                        if (profileID != -1) {
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putString("hash", response.body().getHash())
                                    .putString("name", "")
                                    .putString("email", username)
                                    .putInt("profileID", profileID).putBoolean("loggedIn", true).apply();

                        } else {
                            SharedPreferences.Editor editor = mpref.edit();
                            editor.putBoolean("loggedIn", false).apply();
                            editor.putString("hash", response.body().getHash())
                                    .putString("name", "")
                                    .putString("email", "")
                                    .putInt("profileID", profileID).apply();
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
        final String EMAIL = "email";
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!loggedin) {
                        }
                        if (mpref.getBoolean("loggedIn", true))
                            changeToMatchMaking();
                        else
                            changeToProfileCreation();

                        }
                });
                thread.start();
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                if (isLoggedIn) {
                    Retrofit retrofit = getRetro();
                    RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
                    FacebookToken fb = new FacebookToken();
                    fb.setDeviceid(FirebaseInstanceId.getInstance().getToken());
                    fb.setToken(accessToken.getToken());
                    Call<FacebookLoginReturn> call = apiCalls.facebookLogin(fb);
                    call.enqueue(new Callback<FacebookLoginReturn>() {
                        @Override
                        public void onResponse(Call<FacebookLoginReturn> call, Response<FacebookLoginReturn> response) {
                            int profileID = Objects.requireNonNull(response.body()).getId();
                            if (profileID != -1) {
                                SharedPreferences.Editor editor = mpref.edit();
                                editor.putString("hash", response.body().getHash())
                                        .putInt("profileID", profileID).putBoolean("loggedIn", true).apply();
                            } else {
                                SharedPreferences.Editor editor = mpref.edit();
                                editor.putBoolean("loggedIn", false)
                                        .putString("name", response.body().getName())
                                        .putString("email", response.body().getEmail()).apply();
                            }
                            loggedin = true;
                        }

                        @Override
                        public void onFailure(Call<FacebookLoginReturn> call, Throwable t) {

                        }
                    });
                }


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

        public void changeToMatchMaking (){
            Intent intent = new Intent(this, MatchMaking.class);
            intent.putExtra("change",false);
            startActivity(intent);
        }
        public void changeToProfileCreation (){
            Intent intent = new Intent(this, ProfileCreation.class);
            startActivity(intent);
        }
    }

