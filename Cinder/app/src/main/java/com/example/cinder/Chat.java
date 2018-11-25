package com.example.cinder;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Path;

import static com.example.cinder.Signin.getRetro;

public class Chat extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> messageList;
    private List<String> mUserList;
    private List<String> mTimestamps;
    private List<Integer> mUserID;
    private GroupID groupObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        messageList = new ArrayList<>();
        mUserList = new ArrayList<>();
        mTimestamps = new ArrayList<>();
        mUserID = new ArrayList<>();

        for(int i = 0; i < 30; i++){
            messageList.add("Test");
            mUserList.add("Test");
            mTimestamps.add("Test");
            mUserID.add(10);
        }

        String matchString = getIntent().getExtras().getString("matchID");
        String userString = getIntent().getExtras().getString("userID");
        int matchInt = Integer.parseInt(matchString);
        int userInt = Integer.parseInt(userString);
        groupObj = new GroupID();
        groupObj.setMatchID(matchInt);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        mMessageRecycler.setLayoutManager(mLayoutManager);

        mMessageAdapter = new MessageListAdapter(this, messageList, mUserList, mTimestamps, mUserID, userInt);
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    public void getMessages(int profileID) {
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);
        int matchID = mpref.getInt("matchID",0);
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<Message> call = apiCalls.getMessage(groupObj, profileID);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                messageList = response.body().getMessages();
                mUserList = response.body().getUsers();
                mTimestamps = response.body().getTimeStamps();
                mUserID = response.body().getUserID();
                mMessageAdapter.notifyItemRangeChanged(0, messageList.size());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }
}
