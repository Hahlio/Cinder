package com.example.cinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
                contacts=response.body().getMatchID();
                displayContacts(name);
            }

            @Override
            public void onFailure(@NonNull Call<GroupID> call, @NonNull Throwable t) {
                Log.d("error", "error");
            }

        });
    }
}
