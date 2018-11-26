package com.example.cinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cinder.restobjects.Profile;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.cinder.Signin.getRetro;


public class ProfileCreation extends AppCompatActivity {
    public boolean created=false;
    public boolean success=false;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onStart() {
        super.onStart();
        // Start GPS
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.e("Location", location.toString());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // End GPS
        this.locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);
        final Button submitButton = findViewById(R.id.submitButton);
        final SharedPreferences mpref = getSharedPreferences("IDValue", 0);
        String name  = mpref.getString("name", "");
        String email  = mpref.getString("email", "");
        final int oldProfileID = mpref.getInt("profileID",0);
        final boolean changingProfile= getIntent().getExtras().getBoolean("change");

        if(!name.equals("")&&!email.equals("")){
            EditText nameInput = findViewById(R.id.nameInput);
            nameInput.setText(name);
            nameInput.setEnabled(false);
            EditText emailInput = findViewById(R.id.usernameInput);
            emailInput.setText(email);
            emailInput.setEnabled(false);
        }
        if(changingProfile){
            EditText emailInput = findViewById(R.id.usernameInput);
            emailInput.setText(email);
            emailInput.setEnabled(false);
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                created = false;
                success = false;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!created) {
                        }
                        if (success) {
                            changeToMatchMaking();
                            mpref.edit().putString("name",getOutput(R.id.nameInput)).putString("email",getOutput(R.id.usernameInput)).apply();
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Username Already exist";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                }
                            });
                        }

                    }
                });
                thread.start();
                Retrofit retrofit = getRetro();
                RestApiCalls apiCalls = retrofit.create(RestApiCalls.class);
                Profile newProfile = createProfile();
                if (newProfile == null) {
                    return;
                }
                if(!changingProfile) {
                    Call<ProfileID> call = apiCalls.createProfile(newProfile);
                    call.enqueue(new Callback<ProfileID>() {
                        @Override
                        public void onResponse(@NonNull Call<ProfileID> call, Response<ProfileID> response) {
                            SharedPreferences.Editor editor = mpref.edit();
                            int profileid = response.body().getId();
                            if (profileid != -1) {
                                success = true;
                                editor.putString("hash", response.body().getHash())
                                        .putInt("profileID", Objects.requireNonNull(response.body()).getId()).apply();

                            } else
                                success = false;
                            created = true;
                        }

                        @Override
                        public void onFailure(Call<ProfileID> call, Throwable t) {
                            //failure code to be written
                        }
                    });
                }else{
                    Call<Profile> call = apiCalls.changeProfile(newProfile,oldProfileID);
                    call.enqueue(new Callback<Profile>() {
                        @Override
                        public void onResponse(@NonNull Call<Profile> call, Response<Profile> response) {
                            success = true;
                            created = true;
                        }

                        @Override
                        public void onFailure(Call<Profile> call, Throwable t) {
                            //failure code to be written
                        }

                    });
                }

            }
        });
    }

    private void changeToMatchMaking() {
        Intent intent = new Intent(this, MatchMaking.class);
        startActivity(intent);
    }

    protected Profile createProfile(){
        final EditText course0 = findViewById(R.id.courseInput0);
        final EditText course1 = findViewById(R.id.courseInput1);
        final EditText course2 = findViewById(R.id.courseInput2);
        final EditText course3 = findViewById(R.id.courseInput3);
        final EditText course4 = findViewById(R.id.courseInput4);
        final EditText course5 = findViewById(R.id.courseInput5);

        Profile newProfile = new Profile();
        String password = getOutput(R.id.passwordInput);
        if (password == null) {
            return null;
        }
        String username = getOutput(R.id.usernameInput);
        if (username == null) {
            return null;
        }
        String uni = getOutput(R.id.uniInput);
        if (uni == null) {
            return null;
        }
        String study = getOutput(R.id.studyLocationInput);
        if (study == null) {
            return null;
        }
        String interest = getOutput(R.id.interestInput);
        if (interest == null) {
            return null;
        }
        String name = getOutput(R.id.nameInput);
        if (name == null) {
            return null;
        }
        newProfile.setDeviceid(FirebaseInstanceId.getInstance().getToken());
        newProfile.setPassword(password);
        newProfile.setUsername(username);
        newProfile.setInterests(interest);
        newProfile.setName(name);
        newProfile.setSchool(uni);
        newProfile.setPreferences(study);
        newProfile.setCourses(course0.getText().toString() + "," + course1.getText().toString() +
                "," + course2.getText().toString() + "," + course3.getText().toString() +
                "," + course4.getText().toString() + "," + course5.getText().toString());
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        newProfile.setLat(latitude);
        newProfile.setLng(longitude);
        return newProfile;
    }

    protected String getOutput(int location){
        final EditText input = findViewById(location);
        String result = input.getText().toString();
        if(result.equals("")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Context context = getApplicationContext();
                    CharSequence text = "Incomplete Form";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
            return null;
        }
        return result;
    }


}
