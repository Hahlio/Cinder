package com.example.cinder;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

interface RestApiCalls {

    @GET("profile/{profileID}")
    Call<Profile> getProfile(@Path("profileID") int profileID);

    @PUT("profile/username/")
    Call<ProfileID> getProfileID(@Body SigninInfo signinInfo);

    @PUT("profile/username/fb/")
    Call<FacebookLoginReturn> facebookLogin(@Body FacebookToken facebookToken);

    @POST("profile/")
    Call<ProfileID> createProfile(@Body Profile profile);

    @PUT("profile/{profileID}")
    Call<Profile> changeProfile(@Body Profile profile,@Path("profileID") int profileID );

    @GET("matchmaking/{profileID}")
    Call<Matches> getMatches(@Path("profileID") int profileID);

    @PUT("matchmaking/{profileID}")
    Call<NewMatch> addMatch(@Body NewMatch newMatch,@Path("profileID") int profileID);

    @GET("matchmaking/{profileID}/contacts")
    Call<ContactInfo> getContacts(@Path("profileID") int profileID);

    @GET("matchmaking/{profileID}/groups")
    Call<GroupInfo> getGroups(@Path("profileID") int profileID);

    @POST("matchmaking/{profileID}/groups")
    Call<GroupID> createNewGroup(@Body GroupName name,@Path("profileID") int profileID);

    @PUT("matchmaking/{profileID}/groups")
    Call<GroupID> addUsersToGroup(@Body GroupAdd groupAdd,@Path("profileID") int profileID);

    @DELETE("matchmaking/{profileID}/groups")
    Call<GroupID> removeFromGroup(@Body GroupID groupID,@Path("profileID") int profileID);

    @PUT("message/{profileID}")
    Call<Message> getMessage(@Body GroupID groupID,@Path("profileID") int profileID);

    @POST("message/{profileID}")
    Call<GroupID> sendMessage(@Body SendMessage SendMessage,@Path("profileID") int profileID);

}
