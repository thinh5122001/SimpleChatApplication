package com.example.chatapplication;

import android.util.Log;

import com.pusher.rest.Pusher;

import org.json.JSONException;
import org.json.JSONObject;

public class SendToServerHandler {
    private static final String PUSHER_APP_ID = "1220940";
    private static final String PUSHER_APP_KEY = "ec03aaca0773f446aab4";
    private static final String PUSHER_APP_SECRET = "56d4aaf1ea8b56a1e547";
    private static final String PUSHER_APP_CLUSTER = "ap1";
    private static final String PUSHER_CHANNEL = "chat_application";
    private static final String PUSHER_EVENT = "new-message";
    Pusher pusher;

    public void SendToPusherServer(String content)
    {
        SetUpPusher();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonFormat = "{data: "+content+"}";
                try {
                    JSONObject jsonObject = new JSONObject(jsonFormat);

                    Log.d("json return", "run: "+jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pusher.trigger(PUSHER_CHANNEL,PUSHER_EVENT,jsonFormat);
            }
        });
        thread.start();
    }
    private void SetUpPusher()
    {
        pusher = new Pusher(PUSHER_APP_ID,PUSHER_APP_KEY,PUSHER_APP_SECRET);
        pusher.setCluster(PUSHER_APP_CLUSTER);
    }
}
