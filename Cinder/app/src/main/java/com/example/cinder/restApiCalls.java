package com.example.cinder;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface restApiCalls {

    @GET("profile/{profileID}")
    Call<Profile> getProfile(@Path("profileID") int profileID);

    @GET("profile/username/{username}")
    Call<ProfileID> getProfileID(@Path("username") String username);

    @POST("profile/")
    Call<ProfileID> createProfile(@Body Profile profile);

    @PUT("profile/{profileID}")
    Call<Profile> changeProfile(@Body Profile profile,@Path("profileID") int profileID );

    @GET("profile/{profileID}/pMatches")
    Call<List<ProfileID>> getMatches(@Path("profileID") int profileID);


}
