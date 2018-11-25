package com.example.cinder;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

import static com.example.cinder.Signin.getRetro;

public class Chat extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<String> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        messageList = new ArrayList<>();
        for(int i = 0; i < 30; i++){
            messageList.add("Test");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setReverseLayout(true);
        mMessageRecycler.setLayoutManager(mLayoutManager);

        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    public void getMessages(int profileID) {
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);
        int matchID = mpref.getInt("matchID",0);
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<MessageInfo> call = apiCalls.getMessages(matchID);
        call.enqueue(new Callback<MessageInfo>() {
            @Override
            public void onResponse(@NonNull Call<MessageInfo> call, @NonNull Response<MessageInfo> response) {
                pmatches=response.body().getMatches();
            }

            @Override
            public void onFailure(@NonNull Call<MessageInfo> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }
}
