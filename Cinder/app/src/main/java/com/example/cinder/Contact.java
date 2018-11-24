package com.example.cinder;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.cinder.Signin.getRetro;

public class Contact extends AppCompatActivity {
    private static List<Integer> pmatches;
    private boolean group;
    private int offset=0;
    private int[] testViewArray= {R.id.match1,R.id.match2,R.id.match3,R.id.match4,R.id.match5,R.id.match6,
            R.id.match7,R.id.match8,R.id.match9,R.id.match10,R.id.match11,R.id.match12};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        offset=0;
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);
        final int profileID = mpref.getInt("profileID",0);
        getContacts(profileID);
    }

    public void getContacts(int profileID) {
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<Matches> call = apiCalls.getMatches(profileID);
        call.enqueue(new Callback<Matches>() {
            @Override
            public void onResponse(@NonNull Call<Matches> call, @NonNull Response<Matches> response) {
                pmatches=response.body().getMatches();
            }

            @Override
            public void onFailure(@NonNull Call<Matches> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }
    public void waitForAPI(boolean done,List<String> name ){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                while(done){}
                displayContacts(name);
            }
        });
        thread.start();
    }
}
