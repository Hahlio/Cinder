package com.example.cinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.cinder.restobjects.Profile;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.cinder.Signin.getRetro;

public class MatchMaking extends AppCompatActivity {

    private static List<Integer> pmatches;
    private String hash;
    private boolean doneAdd;
    private boolean doneGet;

    @Override
    public void onBackPressed() {
        // Does nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_making);
        final Button yesButton = findViewById(R.id.yesButton);
        final Button noButton = findViewById(R.id.noButton);
        final Button settingButton  = findViewById(R.id.settingsButton);
        final Button contactsButton = findViewById(R.id.contactsButton);
        final WebView webview = findViewById(R.id.photoDisplay);
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);

        final int profileID = mpref.getInt("profileID",0);
        hash=mpref.getString("hash","");
        outOfMatches();
        getMatches(profileID);

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl("https://www.google.com/maps/search/?api=1&query=47.5951518,-122.3316393");

        String html =
                "     <iframe width=\"350\" height=\"275\" frameborder=\"0\" src=\"https://www.google.com/maps/embed/v1/place?key=AIzaSyA002TR7ZO-RZ3Gkes-wQEMdffB-GJAu70&q=ubc\" scrolling=\"no\">\n" +
                "     </iframe>";
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadData(html, "text/html", null);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pmatches.isEmpty()){
                    addMatch(profileID,pmatches.get(0),true);
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pmatches.isEmpty()){
                    addMatch(profileID,pmatches.get(0),false);
                }
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToContacts();
            }
        });
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeToSetting();
            }
        });





    }
    public void showProfile(int profileID){
        final TextView nameDisplay = findViewById(R.id.nameDisplay);
        final TextView locationDisplay = findViewById(R.id.locationDisplay);
        final TextView courseDisplay = findViewById(R.id.courseDisplay);
        final WebView webview = findViewById(R.id.photoDisplay);
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<Profile> call = apiCalls.getProfile(profileID);
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(@NonNull Call<Profile> call, @NonNull Response<Profile> response) {
                Profile show = response.body();
                nameDisplay.setText(Objects.requireNonNull(show).getName());
                locationDisplay.setText("Interests: "+show.getInterests().replaceAll(",", " "));
                courseDisplay.setText(show.getCourses().replaceAll(",", " "));
                String school = show.getSchool();
                int width = webview.getWidth();
                int height = webview.getHeight();
                String html =
                        "     <iframe width=\""+width+"\" height=\""+height+"\" frameborder=\"0\" align=middle src=\"https://www.google.com/maps/embed/v1/place?key=AIzaSyA002TR7ZO-RZ3Gkes-wQEMdffB-GJAu70&q="
                                + school
                                +"\" scrolling=\"no\">\n"
                                +"     </iframe>";
                webview.setInitialScale(1);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.getSettings().setLoadWithOverviewMode(true);
                webview.getSettings().setUseWideViewPort(true);
                webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
                webview.setScrollbarFadingEnabled(false);
                webview.loadData(html, "text/html", null);
            }

            @Override
            public void onFailure(@NonNull Call<Profile> call, @NonNull Throwable t) {
                //failure code to be written
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
        newMatch.setUser1(userID1);
        newMatch.setUser2(userID2);
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<NewMatch> call = apiCalls.addMatch(newMatch,userID1);
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);
        final int profileID = mpref.getInt("profileID",0);
        call.enqueue(new Callback<NewMatch>() {
            @Override
            public void onResponse(@NonNull Call<NewMatch> call, @NonNull Response<NewMatch> response) {
                getMatches(profileID);
            }
            @Override
            public void onFailure(@NonNull Call<NewMatch> call, @NonNull Throwable t) {
                //failure code to be written
            }
        });

    }
    public void getMatches(int profileID) {
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<Matches> call = apiCalls.getMatches(profileID);
        call.enqueue(new Callback<Matches>() {
            @Override
            public void onResponse(@NonNull Call<Matches> call, @NonNull Response<Matches> response) {
                pmatches=response.body().getMatches();
                if(!pmatches.isEmpty())
                    showProfile(pmatches.get(0));
                else
                    outOfMatches();
            }

            @Override
            public void onFailure(@NonNull Call<Matches> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }
    public void changeToContacts (){
        Intent intent = new Intent(this, Contact.class);
        startActivity(intent);
    }

    public void changeToSetting(){
        Intent intent = new Intent(this, UserSettings.class);
        startActivity(intent);
    }



}
