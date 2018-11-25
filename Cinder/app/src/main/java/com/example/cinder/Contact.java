package com.example.cinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class Contact extends AppCompatActivity {
    private static List<Integer> contacts;
    private static List<String> name;
    private boolean group = false;
    private boolean finished = false;
    private int offset=0;
    private int[] textViewArray= {R.id.match1,R.id.match2,R.id.match3,R.id.match4,R.id.match5,R.id.match6,
            R.id.match7,R.id.match8,R.id.match9,R.id.match10,R.id.match11,R.id.match12};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        offset=0;
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);
        final Button groupsButton = findViewById(R.id.groupSwitch);
        final Button previousButton = findViewById(R.id.previousButton);
        final Button nextButton = findViewById(R.id.nextButton);
        final Button createGroupButton = findViewById(R.id.groupCreation);
        final int profileID = mpref.getInt("profileID",0);
        final Context context = this;

        getContacts(profileID);
        groupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                group = !group;
                if(group){
                    getGroups(profileID);
                    waitForAPI(finished);
                }else{
                    getContacts(profileID);
                    waitForAPI(finished);
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(offset > 0) {
                    offset = offset - 12;
                    displayContacts(name);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset = offset + 12;
                displayContacts(name);

            }
        });
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        for(int k=0;k<12;k++){
            TextView nameDisplay = findViewById(textViewArray[k]);
            final int finalK = k;
            nameDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, Chat.class);
                    i.putExtra("matchID",contacts.get(finalK+offset));
                    startActivity(i);
                }
            });
        }


    }

    public void getContacts(int profileID) {
        finished = false;
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<ContactInfo> call = apiCalls.getContacts(profileID);
        call.enqueue(new Callback<ContactInfo>() {
            @Override
            public void onResponse(@NonNull Call<ContactInfo> call, @NonNull Response<ContactInfo> response) {
                contacts=response.body().getMatchID();
                name=response.body().getUsers();
                finished = true;
            }

            @Override
            public void onFailure(@NonNull Call<ContactInfo> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }
    public void getGroups(int profileID) {
        finished = false;
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<GroupInfo> call = apiCalls.getGroups(profileID);
        call.enqueue(new Callback<GroupInfo>() {
            @Override
            public void onResponse(@NonNull Call<GroupInfo> call, @NonNull Response<GroupInfo> response) {
                contacts=response.body().getMatchID();
                name=response.body().getGroups();
                finished = true;
            }

            @Override
            public void onFailure(@NonNull Call<GroupInfo> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }
    public void waitForAPI(final boolean done ){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                //while(done){}
                //displayContacts(name);
            }
        });
        thread.start();
    }
    public void displayContacts(List<String> name){
        for(int k = 0; k <12; k++){
            if(k<name.size()) {
                TextView nameDisplay = findViewById(textViewArray[k]);
                nameDisplay.setText(name.get(k));
            }else{
                TextView nameDisplay = findViewById(textViewArray[k]);
                nameDisplay.setText("");
            }
        }
    }
}
