package com.example.cinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    private Context thisobject;

    @Override
    protected void onStart() {
        super.onStart();
        // bind to Service
        final SharedPreferences mpref = getSharedPreferences("IDValue", 0);
        final int userInt = mpref.getInt("profileID",0);
        Firebase.giveChat(this, userInt);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        Firebase.removeChat();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        messageList = new ArrayList<>();
        mUserList = new ArrayList<>();
        mTimestamps = new ArrayList<>();
        mUserID = new ArrayList<>();

        final int matchInt = getIntent().getExtras().getInt("matchID");
        final boolean group = getIntent().getExtras().getBoolean("group");
        final SharedPreferences mpref = getSharedPreferences("IDValue", 0);
        final int userInt = mpref.getInt("profileID",0);

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

        final Button send = findViewById(R.id.send);
        final EditText messageField = findViewById(R.id.messageInput);
        final Button addGroup = findViewById(R.id.addGroup);
        final Button leave = findViewById(R.id.Leave);

        if(!group){
            addGroup.setVisibility(View.INVISIBLE);
            leave.setText("Unmatch");
            leave.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    unmatch(userInt);
                }
            });
        }else{
            leave.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    leaveGroup(userInt);
                }
            });
        }

        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String content = messageField.getText().toString();
                Log.d("sending Message", content);
                messageField.setText("");
                if(!(content.equals("")||content.equals(null))) {
                    SendMessage toSend = new SendMessage();
                    toSend.setSenderid(userInt);
                    toSend.setIsGroup(group);
                    toSend.setMatchID(matchInt);
                    toSend.setSendMessage(content);
                    sendMsg(toSend, userInt);
                }
            }
        });

        // Defining the context
        final Context context = this;
        thisobject = this;

        addGroup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddMemebersToGroup.class);
                i.putExtra("contacts",getIntent().getExtras().getIntegerArrayList("contacts"));
                i.putExtra("names",getIntent().getExtras().getStringArrayList("names"));
                startActivity(i);
            }
        });
    }

    public void getMessages(int profileID) {
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

    public void sendMsg(SendMessage toSend, final int userInt) {
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<GroupID> call = apiCalls.sendMessage(toSend, userInt);
        call.enqueue(new Callback<GroupID>() {
            @Override
            public void onResponse(@NonNull Call<GroupID> call, @NonNull Response<GroupID> response) {
                getMessages(userInt);
            }

            @Override
            public void onFailure(@NonNull Call<GroupID> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }

    public void unmatch(final int userInt) {
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<GroupID> call = apiCalls.removeFromMatch(groupObj, userInt);
        call.enqueue(new Callback<GroupID>() {
            @Override
            public void onResponse(@NonNull Call<GroupID> call, @NonNull Response<GroupID> response) {
                Intent i = new Intent(thisobject, Contact.class);
                i.putExtra("contacts",getIntent().getExtras().getIntegerArrayList("contacts"));
                i.putExtra("names",getIntent().getExtras().getStringArrayList("names"));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            @Override
            public void onFailure(@NonNull Call<GroupID> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }

    public void leaveGroup(final int userInt) {
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        Call<GroupID> call = apiCalls.removeFromGroup(groupObj, userInt);
        call.enqueue(new Callback<GroupID>() {
            @Override
            public void onResponse(@NonNull Call<GroupID> call, @NonNull Response<GroupID> response) {
                Intent i = new Intent(thisobject, Contact.class);
                i.putExtra("contacts",getIntent().getExtras().getIntegerArrayList("contacts"));
                i.putExtra("names",getIntent().getExtras().getStringArrayList("names"));
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            @Override
            public void onFailure(@NonNull Call<GroupID> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }
}
