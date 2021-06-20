package com.example.chatapplication.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.Activity.MainActivity;
import com.example.chatapplication.Model.MessageModel;
import com.example.chatapplication.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    @NonNull
    private static final String CHANNEL_NAME ="chat_application";
    private static final String EVENT_NAME ="new-message";
    private static final String TAG = "MessageAdapter";
    private static final int MSG_TYPE_USER_MESSAGE= 1;
    private static final int MSG_TYPE_SERVER_MESSAGE = 2;
    private PusherOptions options;
    private Pusher pusher;
    private Channel channel;
    private String currentUsername;
    private List<MessageModel>messageLists;

    public MessageAdapter(List<MessageModel> messageLists) {
        this.messageLists = messageLists;
    }

    public void addMessage(MessageModel message)
    {
        messageLists.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageLists.get(position);
        String userName = message.getUserName();

        Log.d("userName", "getItemViewType: "+userName);
        if(message.getUserResponse())
        {
            return MSG_TYPE_USER_MESSAGE;
        }
        else
        {
            return MSG_TYPE_SERVER_MESSAGE;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Log.d("view type int", "onCreateViewHolder: "+viewType);

        SetUpPusher();
        Log.d("view type int", "Username return: "+currentUsername);
        View itemView;

        if(viewType == MSG_TYPE_SERVER_MESSAGE) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.server_message_layout, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_message_layout, parent, false);
        }
        return new ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder( MessageAdapter.ViewHolder holder, int position) {
        MessageModel message = messageLists.get(position);
        holder.avatarIMG.setImageResource(R.drawable.ic_man);
        holder.textContentTV.setText(message.getTextContent());
        holder.timeTV.setReferenceTime(System.currentTimeMillis());
    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarIMG;
        TextView textContentTV;
        RelativeTimeTextView timeTV;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            avatarIMG = itemView.findViewById(R.id.avatarIMG);
            textContentTV = itemView.findViewById(R.id.textContentTV);
            timeTV = itemView.findViewById(R.id.timeTV);
        }

    }
    private void SetUpPusher()
    {
        options = new PusherOptions();
        options.setCluster("ap1");
        pusher = new Pusher("ec03aaca0773f446aab4",options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.d(TAG, "onConnectionStateChange: connected");
            }

            @Override
            public void onError(String message, String code, Exception e) {

            }
        });

        channel =  pusher.subscribe(CHANNEL_NAME);
        channel.bind(EVENT_NAME, new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                try {
                    JSONObject jsonObject = new JSONObject(event.getData());
                    Log.d(TAG, "json return: " + jsonObject.toString());
                    currentUsername = jsonObject.getString("userName");
                    Log.d(TAG, "onEvent: current username: "+ currentUsername);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
