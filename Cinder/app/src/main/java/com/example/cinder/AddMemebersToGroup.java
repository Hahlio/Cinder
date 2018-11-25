package com.example.cinder;

import android.content.Context;
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

public class AddMemebersToGroup extends AppCompatActivity {
    private static List<Integer> contacts;
    private static List<String> name;
    private int[] textViewArray= {R.id.match1,R.id.match2,R.id.match3,R.id.match4,R.id.match5,R.id.match6,
            R.id.match7,R.id.match8,R.id.match9,R.id.match10,R.id.match11,R.id.match12};
    private int offset=0;
    final SharedPreferences mpref = getSharedPreferences("IDValue", 0);
    final int profileID = mpref.getInt("profileID", 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memebers_to_group);
        offset = 0;
        final Button previousButton = findViewById(R.id.previousContactsButton);
        final Button nextButton = findViewById(R.id.nextContactsButton);
        final TextView num= findViewById(R.id.contactsPageNum);
        final Context context = this;
        contacts= getIntent().getExtras().getIntegerArrayList("contacts");
        name = getIntent().getExtras().getStringArrayList("names");

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offset > 0) {
                    offset = offset - 12;
                    displayContacts(name);
                    int display = (offset/12)+1;
                    num.setText(String.valueOf(display));
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset = offset + 12;
                displayContacts(name);
                int display = (offset/12)+1;
                num.setText(String.valueOf(display));

            }
        });
        for (int k = 0; k < 12; k++) {
            TextView nameDisplay = findViewById(textViewArray[k]);
            final int finalK = k;
            nameDisplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToGroup(contacts.get(finalK +offset));
                }
            });
        }


    }

    public void addToGroup(int matchID){
        GroupAdd groupadd = new GroupAdd();
        groupadd.setMatchID(getIntent().getExtras().getInt("matchID"));
        groupadd.setUserMatchID(matchID);
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<GroupID> call = apiCalls.addUsersToGroup(groupadd,profileID);
        call.enqueue(new Callback<GroupID>() {
            @Override
            public void onResponse(@NonNull Call<GroupID> call, @NonNull Response<GroupID> response) {
                //no need for code here
            }

            @Override
            public void onFailure(@NonNull Call<GroupID> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }



    public void displayContacts(List<String> name){
        for(int k = 0; k <12; k++){
            if((k+offset)<name.size()) {
                TextView nameDisplay = findViewById(textViewArray[k]);
                nameDisplay.setText(name.get(k+offset));
            }else{
                TextView nameDisplay = findViewById(textViewArray[k]);
                nameDisplay.setText("");
            }
        }
    }
}
