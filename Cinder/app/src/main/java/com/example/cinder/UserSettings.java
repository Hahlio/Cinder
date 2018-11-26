package com.example.cinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.facebook.login.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.cinder.Signin.getRetro;

public class UserSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);
        final int profileID = mpref.getInt("profileID",0);
        Switch notiSwitch  = findViewById(R.id.notiSwitch);
        Button changeProfile = findViewById(R.id.changeProfileButton);
        Button signOut = findViewById(R.id.signOutButton);
        boolean notiOn = mpref.getBoolean("noti",true);
        final Context context = this;
        notiSwitch.setChecked(notiOn);
        notiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mpref.edit().putBoolean("noti",b).apply();
                Retrofit retrofit = getRetro();
                RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
                NotificationSwitch notificationSwitch = new NotificationSwitch();
                notificationSwitch.setNotify(b);
                Call<NotificationSwitch> call = apiCalls.setNotification(notificationSwitch,profileID);
                call.enqueue(new Callback<NotificationSwitch>() {
                    @Override
                    public void onResponse(@NonNull Call<NotificationSwitch> call, Response<NotificationSwitch> response) {
                        //no need for code here
                    }
                    @Override
                    public void onFailure(Call<NotificationSwitch> call, Throwable t) {
                        //failure code to be written
                    }
                });
            }
        });
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProfileCreation.class);
                i.putExtra("change",true);
                startActivity(i);
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                SharedPreferences.Editor editor = mpref.edit();
                editor.remove("loggedIn")
                        .remove("hash")
                        .remove("name")
                        .remove("email")
                        .remove("profileID")
                        .remove("noti").apply();
                Intent i = new Intent(context, Signin.class);
                startActivity(i);
            }
        });


    }
}
