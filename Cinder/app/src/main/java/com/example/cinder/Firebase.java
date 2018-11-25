package com.example.cinder;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Firebase extends FirebaseMessagingService {
    private final String TAG = "JSA-FCM";

    // Chat settings
    private static boolean activateChat = false;
    private static Chat chat;

    // Contact list setting
    private static boolean activateContacts = false;
    private static Contact contact;

    // Global setting
    private static int userInt;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Title: " + remoteMessage.getNotification().getTitle());
            Log.e(TAG, "Body: " + remoteMessage.getNotification().getBody());
            remoteMessage.getNotification().getTitle();
            if(remoteMessage.getNotification().getTitle().equals("New Message")){
                if(activateChat){
                    chat.getMessages(userInt);
                }
            } else if(remoteMessage.getNotification().getTitle().equals("New Match!")){
                if (activateContacts){
                    contact.getContacts(userInt);
                }
            }
        }

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data: " + remoteMessage.getData());
        }
    }

    public static void giveChat(Chat chatObj, int id){
        chat = chatObj;
        userInt = id;
        activateChat = true;
    }

    public static void removeChat(){
        activateChat = false;
    }

    public static void giveContact(Contact contactObj, int id){
        contact = contactObj;
        userInt = id;
        activateContacts = true;
    }

    public static void removeContact(){
        activateContacts = false;
    }
}
