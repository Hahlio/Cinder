package com.example.cinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.cinder.Signin.getRetro;


public class profileCreation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        final Button submitButton = findViewById(R.id.submitButton);
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = getRetro();
                restApiCalls apiCalls = retrofit.create(restApiCalls.class);
                Profile newProfile= createProfile();
                Call<ProfileID> call = apiCalls.createProfile(newProfile);
                call.enqueue(new Callback<ProfileID>() {
                    @Override
                    public void onResponse(Call<ProfileID> call, Response<ProfileID> response) {
                        int statusCode = response.code();
                        SharedPreferences.Editor editor = mpref.edit();
                        editor.putInt("profileID",response.body().getId()).apply();
                    }
                    @Override
                    public void onFailure(Call<ProfileID> call, Throwable t) {

                    }
                });
                changeToMatchMaking();
            }
        });
    }
    public void changeToMatchMaking(){
        Intent intent = new Intent(this, matchMaking.class);
        startActivity(intent);
    }

    protected Profile createProfile() {
        final EditText course0 = findViewById(R.id.courseInput0);
        final EditText course1 = findViewById(R.id.courseInput1);
        final EditText course2 = findViewById(R.id.courseInput2);
        final EditText course3 = findViewById(R.id.courseInput3);
        final EditText course4 = findViewById(R.id.courseInput4);
        final EditText course5 = findViewById(R.id.courseInput5);
        final EditText username = findViewById(R.id.usernameInput);
        final EditText uni = findViewById(R.id.uniInput);
        final EditText study = findViewById(R.id.studyLocationInput);
        final EditText interest = findViewById(R.id.interestInput);
        final EditText name = findViewById(R.id.nameInput);
        Profile newProfile = new Profile();
        newProfile.setUsername(username.getText().toString());
        newProfile.setInterests(interest.getText().toString());
        newProfile.setName(name.getText().toString());
        newProfile.setSchool(uni.getText().toString());
        newProfile.setPreferences(study.getText().toString());
        newProfile.setCourses(course0.getText().toString()+","+course1.getText().toString()+
                ","+course2.getText().toString()+","+course3.getText().toString()+
                ","+course4.getText().toString()+","+course5.getText().toString());
        newProfile.setLat((double) 0);
        newProfile.setLng((double) 0);
        return newProfile;
    }

}
