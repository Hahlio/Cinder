package com.example.cinder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

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
        /*if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            Log.e(TAG, "Title: " + title);
            Log.e(TAG, "Body: " + body);

            // The logic to where we update app
            if(title.equals("New Message")){
                if(activateChat){
                    chat.getMessages(userInt);
                } else {
                    // Put toast for new message
                }
            } else if(title.equals("New Match!")){
                if (activateContacts){
                    contact.getContacts(userInt);
                } else {
                    // Put toast for new match
                }
            }
        }*/

        if (remoteMessage.getData().size() > 0) {
            Map<String,String> data = remoteMessage.getData();
            String title = data.get("title");
            final String body = data.get("title");
            if(title.equals("New Message")){
                if(activateChat){
                    chat.getMessages(userInt);
                } else {
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Context context = getApplicationContext();
                            CharSequence text = body;
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    });*/
                }
            } else if(body.equals("New Match!")){
                if (activateContacts){
                    contact.getContacts(userInt);
                } else {
                    /*runOnUi(new Runnable() {
                        @Override
                        public void run() {
                            Context context = getApplicationContext();
                            CharSequence text = body;
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    });*/
                }
            }

        }
    }

    /**
     * Called when we want firebase updates to the chat
     * @param chatObj the Chat object
     * @param id the userID of the current user
     */
    public static void giveChat(Chat chatObj, int id){
        chat = chatObj;
        userInt = id;
        activateChat = true;
    }

    /**
     * Called to tell firebase to stop updating chat
     */
    public static void removeChat(){
        activateChat = false;
    }

    /**
     * Called when we want firebase to update the contacts
     * @param contactObj the actual contact object
     * @param id the userID of the current user
     */
    public static void giveContact(Contact contactObj, int id){
        contact = contactObj;
        userInt = id;
        activateContacts = true;
    }

    /**
     * Called when we want firebase to stop updating contacts
     */
    public static void removeContact(){
        activateContacts = false;
    }
}
