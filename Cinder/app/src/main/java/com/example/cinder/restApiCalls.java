package com.example.cinder;

import com.example.cinder.restobjects.ContactInfo;
import com.example.cinder.restobjects.FacebookLoginReturn;
import com.example.cinder.restobjects.FacebookToken;
import com.example.cinder.restobjects.GroupID;
import com.example.cinder.restobjects.GroupInfo;
import com.example.cinder.restobjects.GroupName;
import com.example.cinder.restobjects.Message;
import com.example.cinder.restobjects.Profile;
import com.example.cinder.restobjects.SigninInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
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
    Call<GroupID> createNewGroup(@Body GroupName name, @Path("profileID") int profileID);

    @PUT("matchmaking/{profileID}/groups")
    Call<GroupID> addUsersToGroup(@Body GroupAdd groupAdd,@Path("profileID") int profileID);

    @HTTP(method = "DELETE", path = "matchmaking/{profileID}/groups", hasBody = true)
    Call<GroupID> removeFromGroup(@Body GroupID groupID,@Path("profileID") int profileID);

    @HTTP(method = "DELETE", path = "matchmaking/{profileID}/contacts", hasBody = true)
    Call<GroupID> removeFromMatch(@Body GroupID groupID,@Path("profileID") int profileID);

    @PUT("message/{profileID}")
    Call<Message> getMessage(@Body GroupID groupID, @Path("profileID") int profileID);

    @POST("message/{profileID}")
    Call<GroupID> sendMessage(@Body SendMessage SendMessage,@Path("profileID") int profileID);

    @PUT("profile/notification/{profileID}")
    Call<NotificationSwitch> setNotification(@Body NotificationSwitch notificationSwitch,@Path("profileID") int profileID);

    @PUT("matchmaking/{profileID}/groupscontacts")
    Call<ContactInfo> getNonGroupContacts(@Body GroupID groupID,@Path("profileID") int profileID);
}
