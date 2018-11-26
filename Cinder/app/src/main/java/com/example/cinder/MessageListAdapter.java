package com.example.cinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter{
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private Context mContext;
    private List<String> mMessageList;
    private List<String> mUserList;
    private List<String> mTimestamps;
    private List<Integer> mUserID;
    private Integer currentUser;

    public MessageListAdapter(Context context, List<String> messageList, List<String> userList,
                              List<String> timestamps, List<Integer> userID, Integer currentUser) {
        // Stores objects
        mContext = context;
        mMessageList = messageList;
        mUserList = userList;
        mTimestamps = timestamps;
        mUserID = userID;
        this.currentUser = currentUser;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Figures out what type of message
        int id = mUserID.get(position);
        if (id == currentUser) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        // Creates a view depending on what type it is
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Tells the scrolling view what objects are there
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(position);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(position);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        // Creates the sent messages
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }
        // For the view
        void bind(Integer message) {
            messageText.setText(mMessageList.get(message));
            timeText.setText(mTimestamps.get(message));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        // Creates the received messages
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
        }
        // For the view
        void bind(Integer message) {
            messageText.setText(mMessageList.get(message));
            timeText.setText(mTimestamps.get(message));
            nameText.setText(mUserList.get(message));
        }
    }
}
