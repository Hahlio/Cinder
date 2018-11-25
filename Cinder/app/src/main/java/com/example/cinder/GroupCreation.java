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
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.cinder.Signin.getRetro;

public class GroupCreation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);
        final Button confirm = findViewById(R.id.confirmButton);
        final TextView name = findViewById(R.id.groupNameInput);
        final SharedPreferences mpref = getSharedPreferences("IDValue",0);
        final int profileID = mpref.getInt("profileID",0);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = name.getText().toString();
                if(groupName.equals("")){
                    Context context = getApplicationContext();
                    CharSequence text = "Group Name Missing";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }else{
                    createNewGroup(groupName,profileID);
                }
            }
        });
    }
    public void createNewGroup(String name, int profileID) {
        Retrofit retrofit = getRetro();
        RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
        GroupName groupName = new GroupName();
        groupName.setGroupName(name);
        final Context context = this;
        Call<GroupID> call = apiCalls.createNewGroup(groupName,profileID);
        call.enqueue(new Callback<GroupID>() {
            @Override
            public void onResponse(@NonNull Call<GroupID> call, @NonNull Response<GroupID> response) {
                Intent i = new Intent(context, Contact.class);
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
