package com.example.chatapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapplication.Adapter.MessageAdapter;
import com.example.chatapplication.Model.MessageModel;
import com.example.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private EditText inputEDT;
    private Button  inputBTN;
    private Pusher pusher;
    private Channel channel;
    private Button logoutBTN;
    private TextView userInformationTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapping();
        //checkUser();
        recyclerView.setLayoutManager(layoutManager);
        List<MessageModel> messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        inputBTN.setOnClickListener(this::ButtonClick);
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");
        pusher = new Pusher("ec03aaca0773f446aab4", options);

        channel = pusher.subscribe("chat_application");

        channel.bind("new-message", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(event.getData());
                            Log.d("json return", jsonObject.toString());
                            String content = jsonObject.getString("data");
                            Log.d("content", content);
                            MessageModel message = new MessageModel(content);
                            message.setTextContent(content);
                            messageAdapter.addMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });


        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher","State changed from"+change.getPreviousState()
                        +"to"+change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher","There was a problem connecting! "
                        +"\ncode: "+code
                        +"\nmessage: "+message
                        +"\nexception: "+e
                );
            }
        }, ConnectionState.ALL);
    }

    private void Mapping()
    {
        recyclerView = findViewById(R.id.chatRecyclerView);
        inputEDT = findViewById(R.id.inputEDT);
        inputBTN = findViewById(R.id.inputBTN);
        layoutManager = new LinearLayoutManager(this);
        userInformationTextView = findViewById(R.id.userInformationTV);
        logoutBTN = findViewById(R.id.logoutBTN);
    }
    private void ButtonClick( View view )
    {
       String content = inputEDT.getText().toString();
       Log.d("content:  ", content);
       MessageModel messageModel = new MessageModel(content);
       messageModel.setTextContent(content);

       messageAdapter.addMessage(messageModel);

       inputEDT.setText("");
    }
    private void GetFirebaseInstance()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
    }

    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            //user not logged in
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        }
        else {
            //user logged in
            String displayName = firebaseUser.getDisplayName();
            userInformationTextView.setText(displayName);
            logoutBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    checkUser();
                }
            });
        }
    }
}