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

        int matchInt = getIntent().getExtras().getInt("matchID");
        final SharedPreferences mpref = getSharedPreferences("IDValue", 0);
        int userInt = mpref.getInt("profileID",0);
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

        getMessages(userInt);
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
                List<String> tempMsg = response.body().getMessages();
                List<String> tempUser = response.body().getUsers();
                List<String> tempTime = response.body().getTimeStamps();
                List<Integer> tempID = response.body().getUserID();

                messageList.clear();
                mUserList.clear();
                mTimestamps.clear();
                mUserID.clear();

                messageList.addAll(tempMsg);
                mUserList.addAll(tempUser);
                mTimestamps.addAll(tempTime);
                mUserID.addAll(tempID);

                mMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }
}
